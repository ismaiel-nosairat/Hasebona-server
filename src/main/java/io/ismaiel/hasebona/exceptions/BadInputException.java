/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package io.ismaiel.hasebona.exceptions;

/**
 * @author Ismaiel
 */
public class BadInputException extends RuntimeException {


    public BadInputException(String msg, Exception e) {
        super(msg);
    }

}
