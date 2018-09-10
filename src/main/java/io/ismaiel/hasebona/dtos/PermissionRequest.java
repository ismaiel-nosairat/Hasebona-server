/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package io.ismaiel.hasebona.dtos;

import io.ismaiel.hasebona.entities.Permission;
import io.ismaiel.hasebona.models.BusinessObject;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;

/**
 * @author Ismaiel Nosairat <ismaiel.nosairat@gmail.com>
 */
@Getter
@Setter
public class PermissionRequest extends BusinessObject {
    @NotNull
    private String userEmail;
    @NotNull
    private Permission.PermissionType type;
}
