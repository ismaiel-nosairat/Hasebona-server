/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package io.ismaiel.hasebona.services;

import io.ismaiel.hasebona.configurations.GC;
import io.ismaiel.hasebona.configurations.GV;
import io.ismaiel.hasebona.dtos.Login;
import io.ismaiel.hasebona.dtos.LoginResponse;
import io.ismaiel.hasebona.dtos.PermissionRequest;
import io.ismaiel.hasebona.dtos.SheetDTOs;
import io.ismaiel.hasebona.entities.Permission;
import io.ismaiel.hasebona.entities.Sheet;
import io.ismaiel.hasebona.entities.User;
import io.ismaiel.hasebona.exceptions.ProcessException;
import io.ismaiel.hasebona.models.Session;
import io.ismaiel.hasebona.repos.DB;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Ismaiel Nosairat <ismaiel.nosairat@gmail.com>
 */
@Service
@Transactional
public class UserServices {

    @Autowired
    DB db;
    @Autowired
    Core core;
    @Autowired
    private Session session;

    public ResponseEntity<?> loginUser(Login login) {
        login.validate();
        LoginResponse res = new LoginResponse();
        //find user in db
        User user = db.userRepo.findByEmail(login.getEmail());
        // in case of new user
        if (user == null) {
            user = new User(login);
            user = db.userRepo.save(user);
        } else {
            // Update info if changed between login actions
            user.setFirstName(login.getFirstName());
            user.setLastName(login.getLastName());
            user.setImageUrl(login.getImageUrl());
            user.setDisplayName(login.getDisplayName());
            user.setAccessToken(login.getAccessToken());
            user.setIdToken(login.getIdToken());

            // check the permission on the default page
            if (user.getDefaultSheet() != null) {
                Permission permission = db.permissionRepo.findByUserAndSheet(user, user.getDefaultSheet());
                if (permission == null || permission.getType() == Permission.PermissionType.NONE) {
                    user.setDefaultSheet(null);
                } else {
                    res.setDefaultSheetId(user.getDefaultSheet().getId());
                }
            }
            user = db.userRepo.save(user);
        }
        Map<String, Object> jwtTokenData = new HashMap<>();
        jwtTokenData.put("userId", user.getId());
        jwtTokenData.put("loginDate", new Date());
        String token = GV.symClient.createJWT(jwtTokenData);

        //res.setToken("eyJhbGciOiJIUzI1NiJ9.eyJsb2dpbkRhdGUiOjE1MzA5OTUxMDI4NjksInVzZXJJZCI6MjY4fQ.0rBvRBmFDCsIJ9bWDjR6sypLcBIgEix8-NLYVLUq3NY");
        res.setToken(token);

        return core.respondWithData(res);

    }

    public ResponseEntity<?> addPermission(PermissionRequest req) {

        //check if target user exists
        User targetUser = db.userRepo.findByEmail(req.getUserEmail());
        if (targetUser == null) {
            throw new ProcessException(GC.Errors.GRANTED_USER_NOT_FOUND);
        }
        // check whether target has a permission already
        Permission oldRelation = db.permissionRepo.findByUserAndSheet(targetUser, session.getSheet());
        if (oldRelation != null) {
            throw new ProcessException(GC.Errors.USER_HAS_ALREADY_PERMISSION);
        }
        // add permission
        Permission newRelation = new Permission(targetUser, session.getSheet(), req.getType());
        db.permissionRepo.save(newRelation);
        return core.respondWithData("");
    }

    public ResponseEntity<?> removePermission(PermissionRequest req) {
        // check if sheet exits
        core.assertSheetExistence();
        //check if target user exists
        User targetUser = db.userRepo.findByEmail(req.getUserEmail());
        if (targetUser == null) {
            throw new ProcessException(GC.Errors.GRANTED_USER_NOT_FOUND);
        }
        // check whether target has a permission already
        Permission oldRelation = db.permissionRepo.findByUserAndSheet(targetUser, session.getSheet());
        if (oldRelation == null) {
            throw new ProcessException(GC.Errors.NO_PERMISSION_TO_REMOVE);
        }
        // remove permission
        db.permissionRepo.delete(oldRelation);
        return core.respondWithData("");
    }

    public ResponseEntity<?> findUserByEmail(String email) {
        Map<String, Object> result = new HashMap();
        User foundUser = db.userRepo.findByEmail(email);
        if (foundUser == null) {
            result.put("status", "notFound");
            result.put("statusCode", -1);
        } else {
            Sheet currentSheet = db.sheetRepo.findById(session.getSheet().getId())
                    .orElseThrow(() -> new ProcessException(GC.Errors.SHEET_NOT_FOUND));
            Permission oldRelation = db.permissionRepo.findByUserAndSheet(foundUser, currentSheet);
            if (oldRelation != null) {
                result.put("status", "alreadyHashPermission");
                result.put("statusCode", 0);
            } else {
                result.put("status", "found");
                result.put("statusCode", 1);
                result.put("data", new SheetDTOs.PermissionItem(
                        foundUser.getId(),
                        foundUser.getDisplayName(),
                        foundUser.getFirstName(),
                        foundUser.getLastName(),
                        foundUser.getImageUrl(),
                        foundUser.getEmail(),
                        null
                ));
            }
        }
        return core.respondWithData(result);
    }

}
