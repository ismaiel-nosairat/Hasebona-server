/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package io.ismaiel.hasebona.dtos;

import lombok.Getter;
import lombok.Setter;

/**
 * @author Ismaiel Nosairat <ismaiel.nosairat@gmail.com>
 */
@Getter
@Setter
public class LoginResponse {
    private String token;
    private Long defaultSheetId;
}
