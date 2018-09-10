/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package io.ismaiel.hasebona.models;

import io.ismaiel.hasebona.entities.Sheet;
import io.ismaiel.hasebona.entities.User;
import lombok.Data;

/**
 * @author Ismaiel Nosairat <ismaiel.nosairat@gmail.com>
 */
@Data
public class Session {
    private User user;
    private Sheet sheet;
}
