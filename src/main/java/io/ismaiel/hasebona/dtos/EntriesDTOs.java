package io.ismaiel.hasebona.dtos;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.ismaiel.hasebona.models.BusinessObject;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @Author Ismaiel Nosairat <ismaiel.nosairat@gmail.com>
 */
public class EntriesDTOs {

    @AllArgsConstructor
    @NoArgsConstructor
    public static class ListEntriesIn {
        public int maxResult;
        public int pageNumber;
    }

    @Deprecated
    @AllArgsConstructor
    @NoArgsConstructor
    public static class ListEntriesOutItem {
        public Long id;
        public String creator;
        public Double amount;
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
        public Date creationDate;
    }

    @AllArgsConstructor
    @NoArgsConstructor
    public static class ViewEntryOut {
        public Long id;
        public String name;
        public Long creditorId;
        public Double amount;
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
        public Date creationDate;
        public List<MemberItem> members = new ArrayList<>();
    }


    @AllArgsConstructor
    @NoArgsConstructor
    public static class MemberItem {
        public Long id;
        public double amount;
    }

    @AllArgsConstructor
    @NoArgsConstructor
    public static class AddEntryIn extends BusinessObject {
        @NotNull
        public Long creditorId;
        @NotNull
        public String name;
        @NotNull
        public BigDecimal amount;
        @NotNull
        public Date creationDate;
        @NotNull
        public List<Long> members = new ArrayList<>();
    }
}
