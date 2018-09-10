package io.ismaiel.hasebona.entities;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import javax.persistence.*;
import java.util.Date;
import java.util.LinkedHashSet;
import java.util.Set;

@Setter
@Getter
@Entity
@Accessors(chain = true)
public class Sheet {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sheetGenerator")
    @SequenceGenerator(name = "sheetGenerator", sequenceName = "sheet_seq")
    private Long id;

    @Column(nullable = false, length = 50)
    private String name;

    @ManyToOne
    @JoinColumn(nullable = false)
    private User creator;

    //@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
    @Column(nullable = false)
    private Date creationDate;

    @Column(length = 255)
    private String notes;

    @OneToMany(mappedBy = "sheet", cascade = CascadeType.ALL)
    private Set<Member> members = new LinkedHashSet<>();

    @OneToMany(mappedBy = "sheet", cascade = CascadeType.ALL)
    private Set<Entry> entries = new LinkedHashSet<>();

    @OneToMany(mappedBy = "sheet", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<Permission> permissions = new LinkedHashSet<>();

    @Column(nullable = false)
    private Boolean isArchived = false;

    public Sheet(String name, User creator, Date creationDate, String notes) {
        this.name = name;
        this.creator = creator;
        this.creationDate = creationDate;
        this.notes = notes;
        this.isArchived = false;
    }

    public Sheet() {
    }

}
