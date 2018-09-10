package io.ismaiel.hasebona.controllers;

import io.ismaiel.hasebona.dtos.EntriesDTOs;
import io.ismaiel.hasebona.dtos.EntriesDTOs.*;
import io.ismaiel.hasebona.services.Core;
import io.ismaiel.hasebona.services.EntryServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * @Author Ismaiel Nosairat <ismaiel.nosairat@gmail.com>
 */
@CrossOrigin
@RestController
@RequestMapping("/entry")
public class EntryController {
    @Autowired
    private EntryServices entryServices;
    @Autowired
    Core core;

    @PostMapping("/list")
    public ResponseEntity listEntries(@RequestBody ListEntriesIn req) {
        return core.respondWithData(entryServices.listEntries(req));
    }

    @PostMapping("/add")
    public ResponseEntity addEntry(@RequestBody AddEntryIn entry) {
        return entryServices.addEntry(entry);
    }

    @PostMapping("/delete")
    public ResponseEntity deleteEntry(@RequestBody Long entryId) {
        return entryServices.deleteEntry(entryId);
    }

    @PostMapping("/view")
    public ResponseEntity deleteEntry(@RequestBody long id) {
        return entryServices.viewEntry(id);
    }


}

