/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package io.ismaiel.hasebona.services;

import io.ismaiel.hasebona.configurations.GC;
import io.ismaiel.hasebona.exceptions.ProcessException;
import io.ismaiel.hasebona.models.FinalResponse;
import io.ismaiel.hasebona.models.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

/**
 * @author Ismaiel Nosairat <ismaiel.nosairat@gmail.com>
 */
@Service
public class Core {

    @Autowired
    Session session;

    public void assertSheetExistence() {
        if (this.session == null || this.session.getSheet() == null) {
            throw new ProcessException(GC.Errors.SHEET_NOT_FOUND);
        }
    }

    public void assertUserExistence() {
        if (this.session == null || this.session.getUser() == null) {
            throw new ProcessException(GC.Errors.UNAUTHORIZED);
        }
    }

    public ResponseEntity<?> respondWithDataAndCodeAndMsg(Object data, Integer code, String message) {
        FinalResponse response = new FinalResponse();
        response.code = code;
        response.data = data;
        response.message = message;
        return ResponseEntity.ok(response);
    }

    public ResponseEntity<?> respondWithDataAndCode(Object data, Integer code) {
        return respondWithDataAndCodeAndMsg(data, code, "");
    }

    public ResponseEntity<?> respondWithCodeAndMsg(Integer code, String message) {
        return respondWithDataAndCodeAndMsg(null, code, message);
    }

    public ResponseEntity<?> respondWithData(Object data) {

        return respondWithDataAndCodeAndMsg(data, 200, "");
    }

    public ResponseEntity<?> respondWithEmptyOutput() {
        return respondWithDataAndCodeAndMsg(null, 200, "");
    }


}
