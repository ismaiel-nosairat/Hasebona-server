/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package io.ismaiel.hasebona.services;

import io.ismaiel.hasebona.configurations.GC;
import io.ismaiel.hasebona.entities.*;
import io.ismaiel.hasebona.repos.DB;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author Ismaiel Nosairat <ismaiel.nosairat@gmail.com>
 */
@Service
public class Intializer {

    @Autowired
    DB db;

    @Transactional
    @PostConstruct
    public void init() {
        fillPermissionsTable();

        User user1 = new User();
        user1.setEmail("user1@test.com");
        user1.setGoogleId("g1");
        user1.setFirstName("oqlaa");
        user1.setLastName("3neze");
        user1.setImageUrl("https://ionicframework.com/dist/preview-app/www/assets/img/avatar-ts-woody.png");
        db.userRepo.save(user1);
        User user2 = new User();
        user2.setEmail("user2@test.com");
        user2.setGoogleId("g2");
        user2.setFirstName("hamada");
        user2.setLastName("nos");
        user2.setImageUrl("https://ionicframework.com/dist/preview-app/www/assets/img/marty-avatar.png");
        db.userRepo.save(user2);
        User user3 = new User();
        user3.setEmail("user3@test.com");
        user3.setGoogleId("g3");
        user3.setFirstName("monna");
        user3.setLastName("bahubali");
        user3.setImageUrl("https://ionicframework.com/dist/preview-app/www/assets/img/ian-avatar.png");
        db.userRepo.save(user3);

        Sheet sheet1 = new Sheet("first sheet", user1, new Date(), "note1");
        db.sheetRepo.save(sheet1);
        Permission permission1 = new Permission(user1, sheet1, Permission.PermissionType.OWNER);
        db.permissionRepo.save(permission1);

        Permission permission2 = new Permission(user2, sheet1, Permission.PermissionType.FULL);
        db.permissionRepo.save(permission2);
/*
        Permission permission3 = new Permission(user3, sheet1, Permission.PermissionType.VIEW);
        db.permissionRepo.save(permission3);
*/
        Member m1 = new Member(user1.getFirstName(), sheet1);
        m1.setUser(user1);
        Member m2 = new Member(user2.getFirstName(), sheet1);
        m2.setUser(user2);
        Member m3 = new Member("Ibrahem", sheet1);
        Member m4 = new Member("Bassam", sheet1);
        m1 = db.memberRepo.save(m1);
        m2 = db.memberRepo.save(m2);
        m3 = db.memberRepo.save(m3);
        m4 = db.memberRepo.save(m4);
        Entry e1 = new Entry("Banana", m1, BigDecimal.valueOf(1000), new Date(), sheet1);//
        Share sh1 = new Share(m1, BigDecimal.valueOf(500));
        Share sh2 = new Share(m2, BigDecimal.valueOf(-500));
        e1.addShare(sh1).addShare(sh2);
        e1 = db.entryRepo.save(e1);

        Entry e2 = new Entry("Potatos", m1, BigDecimal.valueOf(2000), new Date(), sheet1);
        Share sh3 = new Share(m2, BigDecimal.valueOf(-1000));
        Share sh4 = new Share(m1, BigDecimal.valueOf(1000));
        e2.addShare(sh3).addShare(sh4);
        e2 = db.entryRepo.save(e2);
        List<String> ran = new ArrayList<String>() {{
            add("Drink");
            add("Food");
            add("Basket and kitchen stuffs");
            add("Medical Instruments");
            add("Bread and Milk");
        }};
        for (int i = 0; i < 50; i++) {
            Entry e3 = new Entry(ran.get(i % 5), m2, BigDecimal.valueOf(8000), new Date(), sheet1);
            e3.addShare(new Share(m1, BigDecimal.valueOf(-2000)));
            e3.addShare(new Share(m3, BigDecimal.valueOf(-2000)));
            e3.addShare(new Share(m2, BigDecimal.valueOf(6000)));
            e3.addShare(new Share(m4, BigDecimal.valueOf(-2000)));
            e3 = db.entryRepo.save(e3);
        }
    }

    void fillPermissionsTable() {
        GC.PERMISSION_TABLE.put("/user/login", Permission.PermissionType.NONE);
        GC.PERMISSION_TABLE.put("/user/addPermission", Permission.PermissionType.FULL);
        GC.PERMISSION_TABLE.put("/user/deletePermission", Permission.PermissionType.FULL);
        GC.PERMISSION_TABLE.put("/user/find", Permission.PermissionType.FULL);

        GC.PERMISSION_TABLE.put("/sheet/list", Permission.PermissionType.NONE);
        GC.PERMISSION_TABLE.put("/sheet/view", Permission.PermissionType.VIEW);
        GC.PERMISSION_TABLE.put("/sheet/add", Permission.PermissionType.NONE);
        GC.PERMISSION_TABLE.put("/sheet/report", Permission.PermissionType.VIEW);
        GC.PERMISSION_TABLE.put("/sheet/loadAll", Permission.PermissionType.VIEW);

        GC.PERMISSION_TABLE.put("/entry/list", Permission.PermissionType.VIEW);
        GC.PERMISSION_TABLE.put("/entry/add", Permission.PermissionType.VIEW);
        GC.PERMISSION_TABLE.put("/entry/view", Permission.PermissionType.VIEW);
        GC.PERMISSION_TABLE.put("/entry/delete", Permission.PermissionType.FULL);

        GC.PERMISSION_TABLE.put("/member/list", Permission.PermissionType.VIEW);
        GC.PERMISSION_TABLE.put("/member/add", Permission.PermissionType.FULL);
        GC.PERMISSION_TABLE.put("/member/view", Permission.PermissionType.VIEW);
        GC.PERMISSION_TABLE.put("/member/delete", Permission.PermissionType.FULL);


        //GC.PERMISSION_TABLE.put("/console", GC.PERMISSION_TYPE_NONE);
    }

}
