package io.ismaiel.hasebona.dtos;

import io.ismaiel.hasebona.models.BusinessObject;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

public class MemberDTOs {

    @AllArgsConstructor
    @NoArgsConstructor
    public static class ListMemberItem {
        public Long id;
        public String name;
        public UserItem userInfo=new UserItem();
    }

    @AllArgsConstructor
    @NoArgsConstructor
    public static class AddMemberIn extends BusinessObject {
        @NotNull
        public String name;
        public Long userId;
    }

    @AllArgsConstructor
    @NoArgsConstructor
    public static class AddMemberOut {
        public Long id;
        public String name;
        public String imageUrl;
    }

    @AllArgsConstructor
    @NoArgsConstructor
    public static class ViewMemberOut {
        public ListMemberItem memberInfo=new ListMemberItem();
        public double balance;
        public List<EntryItem> entries = new ArrayList<>();
    }

    @AllArgsConstructor
    @NoArgsConstructor
    public static class EntryItem {
        public Long id;
        public String name;
        public double amount;
    }


    @AllArgsConstructor
    @NoArgsConstructor
    public static class UserItem {
        public Long id;
        public String displayName;
        public String firstName;
        public String lastName;
        public String imageUrl;
        public String email;
    }
}
