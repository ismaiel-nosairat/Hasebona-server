package io.ismaiel.hasebona.entities;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;
import java.util.LinkedHashSet;
import java.util.Set;

@NamedQueries({
        @NamedQuery(name = "ENTRY.LIST", query = "select distinct(en) from Entry en left join fetch en.shares sh  where en.sheet.id=?1 order by en.id DESC"),
        //@NamedQuery(name = "ENTRY.BALANCE_QUERY", query = "SELECT SUM( share.amount ) AS A, member.id AS B, member.name AS C FROM SHARE , MEMBER, SHEET WHERE share.member_id = member.id AND sheet.id = member.sheet_id AND sheet.id = ?1  GROUP BY member.id  HAVING A!=0"),
        //@NamedQuery(name = "ENTRY.TOTAL_QUERY", query = "SELECT SUM( entry.amount ) AS A FROM ENTRY, SHEET WHERE sheet.id = entry.sheet_id AND sheet.id =?1  GROUP BY sheet.id "),

})
@Entity
@Setter
@Getter
public class Entry {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "entryGenerator")
    @SequenceGenerator(name = "entryGenerator", sequenceName = "entry_seq")
    private Long id;

    @Column(nullable = false)
    private BigDecimal amount;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
    @Column(nullable = false)
    private Date creationDate;

    @Column(nullable = false, length = 50)
    private String name;

    @ManyToOne
    @JoinColumn(nullable = false)
    private Member creditor;

    @ManyToOne
    @JoinColumn(nullable = false)
    private Sheet sheet;

    @OneToMany(mappedBy = "entry", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<Share> shares = new LinkedHashSet<>();

    public Entry(String name, Member creditor, BigDecimal amount, Date creationDate, Sheet sheet) {
        this.amount = amount;
        this.creationDate = creationDate;
        this.name = name;
        this.creditor = creditor;
        this.sheet = sheet;

    }

    public Entry addShare(Share share) {
        share.setEntry(this);
        this.shares.add(share);
        return this;
    }

    public Entry() {
    }

}
