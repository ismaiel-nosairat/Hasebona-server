/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package io.ismaiel.hasebona.entities;

import io.ismaiel.hasebona.dtos.Login;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 * @author Ismaiel
 */
@Setter
@Getter
@Entity
public class User implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "userGenerator")
    @SequenceGenerator(name = "userGenerator", sequenceName = "user_seq")
    private Long id;

    @Column(length = 50)
    private String firstName;

    @Column(length = 50)
    private String lastName;

    @Column(length = 50)
    private String displayName;

    @Column(nullable = false, length = 50)
    private String email;

    @Column(nullable = false, length = 255)
    private String googleId;

    @Column(nullable = false)
    private String imageUrl;

    private String accessToken;

    private String idToken;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<Permission> permissions = new LinkedHashSet<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<Member> members = new LinkedHashSet<>();

    @ManyToOne(fetch = FetchType.LAZY)
    private Sheet defaultSheet;

    public User() {
    }

    public User(Login login) {
        this.email = login.getEmail();
        this.firstName = login.getFirstName();
        this.lastName = login.getLastName();
        this.googleId = login.getGoogleId();
        this.imageUrl = login.getImageUrl();
        this.displayName=login.getDisplayName();
        this.accessToken=login.getAccessToken();
        this.idToken=login.getIdToken();
    }

}
