
package io.ismaiel.hasebona.entities;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import javax.persistence.*;
import java.util.LinkedHashSet;
import java.util.Set;

@Accessors(chain = true)
@Setter
@Getter
@Entity
public class Member {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "memberGenerator")
    @SequenceGenerator(name = "memberGenerator", sequenceName = "member_seq")
    private Long id;

    @Column(nullable = false, length = 50)
    private String name;

    @ManyToOne
    @JoinColumn(nullable = false)
    private Sheet sheet;

    @ManyToOne
    private User user;

    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<Share> shares = new LinkedHashSet<>();

    public Member(String name, Sheet sheet, User user) {

        this.name = name;
        this.sheet = sheet;
        this.user = user;
    }

    public Member(String name, Sheet sheet) {
        this.name = name;
        this.sheet = sheet;
    }

    public Member() {
    }


}
