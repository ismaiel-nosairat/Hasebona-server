/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package io.ismaiel.hasebona.repos;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 * @author Ismaiel Nosairat <ismaiel.nosairat@gmail.com>
 */
@Service
public class DB {

    @Autowired
    public UserRepo userRepo;

    @Autowired
    public PermissionRepo permissionRepo;

    @Autowired
    public SheetRepo sheetRepo;

    @Autowired
    public EntryRepo entryRepo;

    @Autowired
    public ShareRepo shareRepo;

    @Autowired
    public MemberRepo memberRepo;

    @PersistenceContext
    public EntityManager em;

}
