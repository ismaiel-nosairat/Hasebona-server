/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package io.ismaiel.hasebona.exceptions;

import io.ismaiel.hasebona.configurations.GC;
import lombok.Getter;
import lombok.Setter;

/**
 * @author Ismaiel Nosairat <ismaiel.nosairat@gmail.com>
 */
@Getter
@Setter
public class ProcessException extends RuntimeException {
    private GC.Errors error;
    private String message;
    private Exception original;
    private Object data;

    public ProcessException(GC.Errors error) {
        this.error = error;
    }

    public ProcessException(GC.Errors error, String message, Exception original, Object data) {
        this.error = error;
        this.message = message;
        this.original = original;
        this.data = data;
    }


}
