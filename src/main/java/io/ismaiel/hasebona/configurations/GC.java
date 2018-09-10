/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package io.ismaiel.hasebona.configurations;

import io.ismaiel.hasebona.entities.Permission;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Ismaiel Nosairat <ismaiel.nosairat@gmail.com>
 */
public class GC {

    public static Map<String, Permission.PermissionType> PERMISSION_TABLE = new HashMap<>();

    public enum Errors {
        SHEET_NOT_FOUND(0),
        GRANTED_USER_NOT_FOUND(1),
        USER_HAS_ALREADY_PERMISSION(2),
        UNKNOWN_SERVICE(3),
        ACCESS_DENIED(4),
        UNKNOWN_ERROR(5),
        NO_PERMISSION_TO_REMOVE(6),
        DUPLICATED_SHEET_NAME(7),
        DUPLICATED_MEMBER(8),
        USER_NOT_FOUND(9),
        NOT_FOUND(10),
        MEMBER_HAS_NON_ZERO_BALANCE(11),
        BAD_MEMBER_LIST_SIZE(12),
        BAD_ENTRY_AMOUNT(13), MEMBER_NOT_FOUND(14), UNAUTHORIZED(15);


        private int code;

        public Integer getValue() {
            return this.code;
        }

        private Errors(int code) {
            this.code = code;
        }

    }

    public static String reportBalanceQuery = "SELECT SUM( share.amount ) AS A, member.id AS B, member.name AS C\n"
            + "FROM SHARE , MEMBER, SHEET\n"
            + "WHERE share.member_id = member.id\n"
            + "AND sheet.id = member.sheet_id\n"
            + "AND sheet.id = ?1  \n"
            + "GROUP BY member.id  HAVING A!=0";

    public static String reportTotalQuery = "SELECT SUM( entry.amount ) AS A\n"
            + "FROM ENTRY, SHEET\n"
            + "WHERE sheet.id = entry.sheet_id\n"
            + "AND sheet.id =?1 \n"
            + "GROUP BY sheet.id";

    public static String reportMemberBalance = "select e.id as ent ,e.name as name, s.id as sha, s.amount as amount\n"
            + " from entry e, share s\n"
            + " where s.entry_id=e.id and s.member_id=?1";
}
