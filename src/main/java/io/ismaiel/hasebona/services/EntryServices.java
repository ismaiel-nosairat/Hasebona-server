package io.ismaiel.hasebona.services;

import io.ismaiel.hasebona.configurations.GC;
import io.ismaiel.hasebona.dtos.EntriesDTOs.*;
import io.ismaiel.hasebona.entities.Entry;
import io.ismaiel.hasebona.entities.Member;
import io.ismaiel.hasebona.entities.Share;
import io.ismaiel.hasebona.exceptions.ProcessException;
import io.ismaiel.hasebona.models.Session;
import io.ismaiel.hasebona.repos.DB;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.Query;
import java.math.BigDecimal;
import java.util.*;

/**
 * @Author Ismaiel Nosairat <ismaiel.nosairat@gmail.com>
 */
@Service
@Transactional
public class EntryServices {
    @Autowired
    private DB db;
    @Autowired
    private Core core;
    @Autowired
    private Session session;

    public List<ViewEntryOut> listEntries(ListEntriesIn request) {

        Query query = db.em.createNamedQuery("ENTRY.LIST");
        if (request.maxResult != -1) {
            query.setFirstResult(request.pageNumber * request.maxResult);
            query.setMaxResults(request.maxResult);
        }
        query.setParameter(1, session.getSheet().getId());
        List<Entry> out = query.getResultList();
        List<ViewEntryOut> response = new ArrayList<>();
        out.forEach(e -> {
            List<MemberItem> memberItems = new ArrayList<>();
            e.getShares().forEach(sh -> {
                memberItems.add(
                        new MemberItem(
                                sh.getMember().getId(),
                                sh.getAmount().setScale(2, BigDecimal.ROUND_FLOOR).doubleValue()
                        ));
            });
            response.add(
                    new ViewEntryOut(
                            e.getId(),
                            e.getName(),
                            e.getCreditor().getId(),
                            e.getAmount().doubleValue(),
                            e.getCreationDate(),
                            memberItems));

        });
        return (response);
    }

    public ResponseEntity viewEntry(long id) {
        Entry entry = db.entryRepo.findByIdWithShares(id)
                .orElseThrow(() -> new ProcessException(GC.Errors.NOT_FOUND));
        if (entry.getSheet().getId().longValue() != session.getSheet().getId().longValue()) {
            throw new ProcessException(GC.Errors.ACCESS_DENIED);
        }

        ViewEntryOut response = new ViewEntryOut();
        response.id = entry.getId();
        response.creditorId = entry.getCreditor().getId();
        response.amount = entry.getAmount().doubleValue();
        response.creationDate = entry.getCreationDate();
        entry.getShares().forEach(share -> {
            response.members.add(
                    new MemberItem(
                            share.getMember().getId(),
                            share.getAmount().setScale(2, BigDecimal.ROUND_FLOOR).doubleValue()
                    ));
        });
        return core.respondWithData(response);
    }

    public ResponseEntity deleteEntry(Long entryId) {
        Entry entry = db.entryRepo.findById(entryId)
                .orElseThrow(() -> new ProcessException(GC.Errors.NOT_FOUND));
        if (entry.getSheet().getId().longValue() != session.getSheet().getId().longValue()) {
            throw new ProcessException(GC.Errors.ACCESS_DENIED);
        }
        db.entryRepo.deleteById(entryId);
        return core.respondWithEmptyOutput();
    }

    public ResponseEntity addEntry(AddEntryIn request) {

        // validate annotations
        request.validate();
        // check if shares number is less that 1
        if (request.members.size() < 1) {
            throw new ProcessException(GC.Errors.BAD_MEMBER_LIST_SIZE);
        }

        // check entry amount
        if (request.amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new ProcessException(GC.Errors.BAD_ENTRY_AMOUNT);
        }
        // Validate members
        Set<Long> sentMembers = new HashSet<>(request.members);
        if (sentMembers.size() != request.members.size()) {
            throw new ProcessException(GC.Errors.DUPLICATED_MEMBER);
        }
        Set<Member> members = db.memberRepo.findBySheet(session.getSheet());
        Set<Long> sheetMembersIds = new HashSet<>();
        members.forEach(m -> {
            sheetMembersIds.add(m.getId());
        });
        if (!sheetMembersIds.contains(request.creditorId)) {
            throw new ProcessException(GC.Errors.MEMBER_NOT_FOUND);
        }
        request.members.forEach(m -> {
            if (!sheetMembersIds.contains(m)) {
                throw new ProcessException(GC.Errors.MEMBER_NOT_FOUND);
            }
        });

        Entry entry = new Entry();
        entry.setName(request.name);
        entry.setAmount(request.amount);
        entry.setCreationDate(request.creationDate);
        entry.setCreditor(
                members.stream().
                        filter(member -> member.getId().longValue() == request.creditorId.longValue())
                        .findFirst()
                        .orElseThrow(() -> new ProcessException(GC.Errors.MEMBER_NOT_FOUND))
        );
        entry.setSheet(session.getSheet());
        for (Long dtoMemberId : request.members) {
            Share share = new Share();
            share.setEntry(entry);
            share.setMember(
                    members.stream()
                            .filter(sheetMember -> sheetMember.getId().longValue() == dtoMemberId.longValue())
                            .findFirst()
                            .orElseThrow(() -> new ProcessException(GC.Errors.MEMBER_NOT_FOUND))
            );
            share.setAmount(
                    (entry.getAmount().
                            divide(
                                    new BigDecimal(request.members.size()),
                                    6,
                                    BigDecimal.ROUND_FLOOR
                            )).multiply(BigDecimal.valueOf(-1L))
            );
            if (share.getMember().getId().longValue() == request.creditorId) {
                share.setAmount(
                        share.getAmount().add(request.amount)
                );
            }
            entry.getShares().add(share);

        }

        Optional<Share> creditorShare = entry.getShares()
                .stream()
                .filter(sh -> sh.getMember().getId().longValue() == request.creditorId)
                .findAny();
        if (!creditorShare.isPresent()) {
            Share share = new Share();
            share.setEntry(entry);
            share.setMember(
                    members.stream()
                            .filter(sheetMember -> sheetMember.getId().longValue() == request.creditorId.longValue())
                            .findFirst()
                            .orElseThrow(() -> new ProcessException(GC.Errors.MEMBER_NOT_FOUND))
            );
            share.setAmount(request.amount);
            entry.getShares().add(share);
        }

        entry = db.entryRepo.save(entry);
        ViewEntryOut response = new ViewEntryOut();
        response.id = entry.getId();
        response.name = entry.getName();
        response.creditorId = entry.getCreditor().getId();
        response.amount = entry.getAmount().doubleValue();
        response.creationDate = entry.getCreationDate();
        entry.getShares().forEach(share -> {
            response.members.add(
                    new MemberItem(
                            share.getMember().getId(),
                            share.getAmount().setScale(2, BigDecimal.ROUND_FLOOR).doubleValue()
                    ));
        });
        return core.respondWithData(response);

//        return core.respondWithEmptyOutput();
    }


}
