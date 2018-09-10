/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package io.ismaiel.hasebona.dtos;

import io.ismaiel.hasebona.models.BusinessObject;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @author Ismaiel Nosairat <ismaiel.nosairat@gmail.com>
 */
@Data
public class Login extends BusinessObject {

    @NotNull
    private String firstName;
    @NotNull
    private String lastName;
    @NotNull
    private String email;
    @NotNull
    private String googleId;
    @NotNull
    private String displayName;
    private String imageUrl;
    private String accessToken;
    private String idToken;
}
