package io.ismaiel.hasebona.entities;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity
@Setter
@Getter
public class Share {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "shareGenerator")
    @SequenceGenerator(name = "shareGenerator", sequenceName = "share_seq")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false)
    private Member member;

    @ManyToOne
    @JoinColumn(nullable = false)
    private Entry entry;

    @Column(nullable = false)
    private BigDecimal amount;

    public Share(Member member, BigDecimal amount) {
        this.member = member;
        this.amount = amount;
    }

    public Share() {
    }


}
