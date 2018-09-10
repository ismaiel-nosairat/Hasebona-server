/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package io.ismaiel.hasebona.repos;

import io.ismaiel.hasebona.entities.Permission;
import io.ismaiel.hasebona.entities.Sheet;
import io.ismaiel.hasebona.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author Ismaiel
 */
@Repository
public interface PermissionRepo extends JpaRepository<Permission, Long> {

    public Permission findByUserAndSheet(User loggedUser, Sheet sheet);

    @Query("select p from Permission p where p.user.id=?1 and p.sheet.id=?2 ")
    public Permission findByUserIdAndSheetId(Long userId, Long sheetId);

    @Query("select p from Permission p  join fetch p.user where p.sheet=?1 ")
    List<Permission> findBySheet(Sheet sheet);

}
