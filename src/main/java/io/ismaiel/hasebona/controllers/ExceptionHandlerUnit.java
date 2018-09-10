/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package io.ismaiel.hasebona.controllers;

import io.ismaiel.hasebona.configurations.GC;
import io.ismaiel.hasebona.exceptions.ProcessException;
import io.ismaiel.hasebona.services.Core;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

/**
 * @author Ismaiel Nosairat <ismaiel.nosairat@gmail.com>
 */
@ControllerAdvice()
public class ExceptionHandlerUnit {

    @Autowired
    Core core;

    private Logger LOGGER = LoggerFactory.getLogger(ExceptionHandlerUnit.class);


    @ExceptionHandler(ProcessException.class)
    public ResponseEntity<?> handleProcessException(
            ProcessException ex, WebRequest request) {
        return core.respondWithCodeAndMsg(ex.getError().getValue(), ex.getError().name());
    }

    @ExceptionHandler({Exception.class})
    public ResponseEntity<?> handleUnknownException(
            Exception ex, WebRequest request) {
        LOGGER.error(ex.getMessage());
        return core.respondWithCodeAndMsg(GC.Errors.UNKNOWN_ERROR.getValue(), GC.Errors.UNKNOWN_ERROR.name());
    }

}
