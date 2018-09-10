package io.ismaiel.hasebona.dtos;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.ismaiel.hasebona.entities.Permission;
import io.ismaiel.hasebona.models.BusinessObject;
import io.ismaiel.hasebona.models.Report;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class SheetDTOs {


    @AllArgsConstructor
    @NoArgsConstructor
    public static class SheetListItem {
        public Long id;
        public String name;
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
        public Date creationDate;
        public String notes;

    }

    @AllArgsConstructor
    @NoArgsConstructor
    public static class AddSheetIn extends BusinessObject {
        @NotNull
        public String name;
        public String notes;

    }

    @AllArgsConstructor
    @NoArgsConstructor
    public static class AddSheetOut {
        public Long id;
        public String name;
        public String notes;
        public List<PermissionItem> permissionsList = new ArrayList<>();
    }

    @AllArgsConstructor
    @NoArgsConstructor
    public static class PermissionItem {
        public Long id;
        public String displayName;
        public String firstName;
        public String lastName;
        public String imageUrl;
        public String email;
        public String permission;
    }

    @AllArgsConstructor
    @NoArgsConstructor
    public static class ViewSheetOut {
        public Long id;
        public String name;
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
        public Date creationDate;
        public String notes;
        public List<PermissionItem> permissionsList = new ArrayList<>();

    }

    @AllArgsConstructor
    @NoArgsConstructor
    public static class AllDto {
        public List<SheetListItem> userSheets = new ArrayList<>();
        public ViewSheetOut sheet = new ViewSheetOut();
        public List<MemberDTOs.ListMemberItem> members = new ArrayList<>();
        public List<EntriesDTOs.ViewEntryOut> entries = new ArrayList<>();
        public Report report = new Report();
        public Permission.PermissionType userPermission;

    }


}
