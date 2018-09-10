package io.ismaiel.hasebona.services;

import io.ismaiel.hasebona.configurations.GC;
import io.ismaiel.hasebona.dtos.EntriesDTOs;
import io.ismaiel.hasebona.dtos.MemberDTOs;
import io.ismaiel.hasebona.dtos.SheetDTOs.*;
import io.ismaiel.hasebona.entities.Member;
import io.ismaiel.hasebona.entities.Permission;
import io.ismaiel.hasebona.entities.Sheet;
import io.ismaiel.hasebona.exceptions.ProcessException;
import io.ismaiel.hasebona.models.Report;
import io.ismaiel.hasebona.models.Session;
import io.ismaiel.hasebona.repos.DB;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.Query;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

@Service
@Transactional
public class SheetServices {
    @Autowired
    DB db;
    @Autowired
    Core core;
    @Autowired
    private Session session;
    @Autowired
    private EntryServices entryServices;
    @Autowired
    private MemberServices memberServices;

    public List<SheetListItem> listUserSheets() {
        core.assertUserExistence();
        List<Sheet> sheets = db.sheetRepo.findByPermissions_User(session.getUser().getId());
        List<SheetListItem> output = new ArrayList<>();
        sheets.forEach(s -> {
            output.add(new SheetListItem(s.getId(), s.getName(), s.getCreationDate(), s.getNotes()));
        });
        return output;
    }

    public ResponseEntity<?> addSheet(AddSheetIn request) {
        //validate input
        request.validate();
        core.assertUserExistence();

        //validate that name is unique
        List<Sheet> userSheets = db.sheetRepo.findByPermissions_User(session.getUser().getId());
        if (!userSheets.isEmpty()) {
            for (Sheet sh : userSheets) {
                if (sh.getName().equals(request.name)) {
                    throw new ProcessException(GC.Errors.DUPLICATED_SHEET_NAME);
                }
            }
        }
        //create sheet
        Sheet created = new Sheet()
                .setCreator(session.getUser())
                .setCreationDate(new Date())
                .setIsArchived(false)
                .setName(request.name)
                .setNotes(request.notes);


        created = db.sheetRepo.save(created);
        Permission permission = new Permission(session.getUser(), created, Permission.PermissionType.OWNER);
        permission = db.permissionRepo.save(permission);
        Member member = new Member()
                .setUser(session.getUser())
                .setName(session.getUser().getFirstName())
                .setSheet(created);
        db.memberRepo.save(member);
        AddSheetOut response = new AddSheetOut();
        response.id = created.getId();
        response.name = created.getName();
        response.notes = created.getNotes();
        response.permissionsList.add(
                new PermissionItem(
                        permission.getId(),
                        session.getUser().getDisplayName(),
                        session.getUser().getFirstName(),
                        session.getUser().getLastName(),
                        session.getUser().getImageUrl(),
                        session.getUser().getEmail(),
                        permission.getType().name())
        );
        return core.respondWithData(response);
    }

    public ViewSheetOut viewSheet() {
        //validate that the logged use has permission on sheet
        List<Permission> permissions = db.permissionRepo.findBySheet(session.getSheet());
        ViewSheetOut response = new ViewSheetOut();
        response.id = session.getSheet().getId();
        response.name = session.getSheet().getName();
        response.creationDate = session.getSheet().getCreationDate();
        response.notes = session.getSheet().getNotes();
        permissions.forEach(permission -> {
            response.permissionsList.add(
                    new PermissionItem(
                            permission.getId(),
                            session.getUser().getDisplayName(),
                            permission.getUser().getFirstName(),
                            permission.getUser().getLastName(),
                            permission.getUser().getImageUrl(),
                            permission.getUser().getEmail(),
                            permission.getType().name())
            );
        });

        return response;
    }

    public Report reportSheet() {
        Report report = new Report();
        String x = GC.reportBalanceQuery;
        Query q = db.em.createNativeQuery(x);
        q.setParameter(1, session.getSheet().getId());
        List<Object[]> list = q.getResultList();

        for (Object[] ob : list) {
            report.balances.add(((BigDecimal) ob[0]).setScale(2, BigDecimal.ROUND_CEILING));
            report.members.add(ob[2].toString());
        }
        report.id = session.getSheet().getId();
        Query q2 = db.em.createNativeQuery(GC.reportTotalQuery);
        q2.setParameter(1, session.getSheet().getId());
        List resultList = q2.getResultList();
        Iterator<BigDecimal> itr = resultList.iterator();
        ArrayList<BigDecimal> items = new ArrayList<>();
        while (itr.hasNext()) {
            items.add(itr.next());
        }
        if (items.size() > 0) {
            report.total = items.get(0).setScale(2, BigDecimal.ROUND_CEILING);
        } else {
            report.total = BigDecimal.ONE;
        }
        return (report);
    }

    public ResponseEntity<?> loadAll() {
        ViewSheetOut sheet = this.viewSheet();
        List<SheetListItem> sheetListItems = listUserSheets();
        Report reportSheet = this.reportSheet();
        List<MemberDTOs.ListMemberItem> listMemberItems = memberServices.listMembers();
        List<EntriesDTOs.ViewEntryOut> viewEntryOuts = entryServices.listEntries(new EntriesDTOs.ListEntriesIn(-1, 0));
        AllDto all = new AllDto();
        all.sheet = sheet;
        all.entries = viewEntryOuts;
        all.members = listMemberItems;
        all.report = reportSheet;
        all.userSheets = sheetListItems;
        all.userPermission = db.permissionRepo.findByUserAndSheet(session.getUser(), session.getSheet()).getType();
        return core.respondWithData(all);
    }
}
