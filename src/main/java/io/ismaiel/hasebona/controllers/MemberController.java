package io.ismaiel.hasebona.controllers;

import io.ismaiel.hasebona.dtos.MemberDTOs;
import io.ismaiel.hasebona.dtos.MemberDTOs.*;
import io.ismaiel.hasebona.services.Core;
import io.ismaiel.hasebona.services.MemberServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * @Author Ismaiel Nosairat <ismaiel.nosairat@gmail.com>
 */
@CrossOrigin
@RestController
@RequestMapping("/member")
public class MemberController {
    @Autowired
    Core core;
    @Autowired
    private MemberServices memberServices;

    @PostMapping("/list")
    public ResponseEntity listMembers() {
        return core.respondWithData(memberServices.listMembers());
    }

    @PostMapping("/add")
    public ResponseEntity addMember(@RequestBody AddMemberIn member) {
        return memberServices.addMember(member);
    }

    @PostMapping("/delete")
    public ResponseEntity deleteMember(@RequestBody long memberId) {
        return memberServices.deleteMember(memberId);
    }

    @PostMapping("/view")
    public ResponseEntity viewMember(@RequestBody long memberId) {
        return memberServices.viewMember(memberId);
    }

}
