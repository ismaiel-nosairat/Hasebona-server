/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package io.ismaiel.hasebona.entities;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

/**
 * @author Ismaiel
 */
@Entity
@Getter
@Setter
public class Permission {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "permissionGenerator")
    @SequenceGenerator(name = "permissionGenerator", sequenceName = "permission_seq")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false)
    private Sheet sheet;

    @Column(nullable = false)
    private Integer type;

    public enum PermissionType {
        OWNER(3),
        VIEW(1),
        FULL(2),
        NONE(0);
        private int value;

        private PermissionType(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }

        public static PermissionType parse(Integer code) {
            for (PermissionType type : PermissionType.values()) {
                if (type.value == code) {
                    return type;
                }
            }
            return null;
        }
    }


    public PermissionType getType() {
        if (this.type == null) {
            return null;
        }
        return PermissionType.parse(this.type);
    }

    public void setType(PermissionType type) {
        this.type = type.value;
    }

    public Permission(User user, Sheet sheet, PermissionType type) {
        this.user = user;
        this.sheet = sheet;
        this.type = type.getValue();
    }

    public Permission() {
    }

}
