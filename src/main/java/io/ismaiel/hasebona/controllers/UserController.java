/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package io.ismaiel.hasebona.controllers;

import io.ismaiel.hasebona.dtos.Login;
import io.ismaiel.hasebona.dtos.PermissionRequest;
import io.ismaiel.hasebona.services.UserServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * /**
 *
 * @author Ismaiel Nosairat <ismaiel.nosairat@gmail.com>
 */
@CrossOrigin
@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    UserServices userServices;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Login login) {
        return userServices.loginUser(login);
    }

    @PostMapping("/addPermission")
    public ResponseEntity<?> addPermission(@RequestBody PermissionRequest permissionRequest) {
        return userServices.addPermission(permissionRequest);
    }

    @PostMapping("/deletePermission")
    public ResponseEntity<?> deletePermission(@RequestBody PermissionRequest permissionRequest) {
        return userServices.removePermission(permissionRequest);
    }

    @PostMapping("/find")
    public ResponseEntity<?> findUser(@RequestBody String userEmail) {
        return userServices.findUserByEmail(userEmail);
    }


}
