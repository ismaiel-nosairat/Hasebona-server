package io.ismaiel.hasebona.services;

import io.ismaiel.hasebona.configurations.GC;
import io.ismaiel.hasebona.dtos.MemberDTOs.*;
import io.ismaiel.hasebona.entities.Member;
import io.ismaiel.hasebona.entities.Share;
import io.ismaiel.hasebona.exceptions.ProcessException;
import io.ismaiel.hasebona.models.Session;
import io.ismaiel.hasebona.repos.DB;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Service
@Transactional
public class MemberServices {
    @Autowired
    private DB db;
    @Autowired
    private Core core;
    @Autowired
    private Session session;

    public List<ListMemberItem> listMembers() {

        List<Member> members = db.memberRepo.findWithUsersBySheet(session.getSheet());
        List<ListMemberItem> response = new ArrayList<>();
        members.forEach(member -> {
            response.add(
                    new ListMemberItem(
                            member.getId(),
                            member.getName(),
                            member.getUser() == null ?
                                    null :
                                    new UserItem(
                                            member.getUser().getId(),
                                            member.getUser().getDisplayName(),
                                            member.getUser().getFirstName(),
                                            member.getUser().getLastName(),
                                            member.getUser().getImageUrl(),
                                            member.getUser().getEmail()
                                    ))
            );
        });
        return (response);
    }

    public ResponseEntity addMember(AddMemberIn addMemberIn) {
        addMemberIn.validate();
        // check duplicate
        Integer count;
        if (addMemberIn.userId != null)
            count = db.memberRepo.countByNameOrUser(addMemberIn.name, addMemberIn.userId,session.getSheet().getId());
        else
            count = db.memberRepo.countByName(addMemberIn.name,session.getSheet().getId());

        if (count > 0) {
            throw new ProcessException(GC.Errors.DUPLICATED_MEMBER);
        }
        Member member = new Member();
        member.setName(addMemberIn.name);
        member.setUser(
                addMemberIn.userId == null ?
                        null :
                        db.userRepo.findById(addMemberIn.userId)
                                .orElseThrow(() -> new ProcessException(GC.Errors.USER_NOT_FOUND))
        );

        member.setSheet(session.getSheet());
        member = db.memberRepo.save(member);
        return core.respondWithEmptyOutput();
    }

    public ResponseEntity deleteMember(Long memberId) {

        Member member = db.memberRepo.findBySheetAndId(session.getSheet(), memberId);
        if (member == null) {
            throw new ProcessException(GC.Errors.NOT_FOUND);
        }
        Set<Share> shares = member.getShares();
        BigDecimal balance = BigDecimal.ZERO;
        if (shares != null) {
            balance = shares
                    .stream()
                    .map(Share::getAmount)
                    .reduce(balance, BigDecimal::add);
        }
        if (balance.compareTo(BigDecimal.ZERO) > 0) {
            throw new ProcessException(GC.Errors.MEMBER_HAS_NON_ZERO_BALANCE);
        }
        db.memberRepo.deleteById(memberId);
        return core.respondWithEmptyOutput();
    }

    public ResponseEntity viewMember(Long memberId) {
        Member member = db.memberRepo.findBySheetAndIdWithEntries(session.getSheet(), memberId);
        if (member == null) {
            throw new ProcessException(GC.Errors.NOT_FOUND);
        }
        BigDecimal balance = BigDecimal.ZERO;
        for (Share share : member.getShares()) {
            balance = balance.add(share.getAmount());
        }
        ViewMemberOut response = new ViewMemberOut();
        response.memberInfo.id = memberId;
        response.memberInfo.name = member.getName();
        response.memberInfo.userInfo =
                member.getUser() == null ? null :
                        new UserItem(
                                member.getUser().getId(),
                                member.getUser().getDisplayName(),
                                member.getUser().getFirstName(),
                                member.getUser().getLastName(),
                                member.getUser().getImageUrl(),
                                member.getUser().getEmail()
                        );

        response.balance = balance.doubleValue();
        member.getShares().forEach(share -> {
            response.entries.add(
                    new EntryItem(
                            share.getEntry().getId(),
                            share.getEntry().getName(),
                            share.getAmount().doubleValue()
                    ));
        });

        return core.respondWithData(response);
    }


}
