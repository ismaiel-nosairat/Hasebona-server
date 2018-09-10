package io.ismaiel.hasebona.models;


import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class Report {
    public long id;
    public List<String> members;
    public List<BigDecimal> balances;
    public BigDecimal total;
    public Boolean isThereData;

    public Report() {
        members = new ArrayList<>();
        balances = new ArrayList<>();
    }

    public Report(long id, List<String> members, List<BigDecimal> balances, BigDecimal total, boolean isThereData) {
        this.id = id;
        this.members = members;
        this.balances = balances;
        this.total = total;
        this.isThereData = isThereData;
    }


}
