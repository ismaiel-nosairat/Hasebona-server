package io.ismaiel.hasebona.controllers;

import io.ismaiel.hasebona.dtos.SheetDTOs;
import io.ismaiel.hasebona.entities.Sheet;
import io.ismaiel.hasebona.services.Core;
import io.ismaiel.hasebona.services.SheetServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * @Author Ismaiel Nosairat <ismaiel.nosairat@gmail.com>
 */
@CrossOrigin
@RestController
@RequestMapping("/sheet")
public class SheetController {
    @Autowired
    Core core;
    @Autowired
    SheetServices sheetServices;

    @PostMapping("/list")
    public ResponseEntity<?> listUserSheets() {
        return core.respondWithData(sheetServices.listUserSheets());
    }

    @PostMapping("/view")
    public ResponseEntity<?> viewSheet() {
        return core.respondWithData(sheetServices.viewSheet());
    }

    @PostMapping("/add")
    public ResponseEntity<?> createSheet(@RequestBody SheetDTOs.AddSheetIn sheet) {
        return sheetServices.addSheet(sheet);
    }

    @PostMapping("/report")
    public ResponseEntity<?> reportSheet() {
        return core.respondWithData(sheetServices.reportSheet());
    }

    @PostMapping("/loadAll")
    public ResponseEntity<?> loadAll() {
        return sheetServices.loadAll();
    }

}

