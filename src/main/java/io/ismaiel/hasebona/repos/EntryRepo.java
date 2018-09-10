package io.ismaiel.hasebona.repos;

import io.ismaiel.hasebona.entities.Entry;
import io.ismaiel.hasebona.entities.Member;
import io.ismaiel.hasebona.entities.Sheet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
public interface EntryRepo extends JpaRepository<Entry, Long> {

    public List<Entry> findBySheet(Sheet sheet);

    @Transactional
    Long deleteBySheetId(long id);

    public List<Entry> findByCreditor(Member member);


    @Query(" select e from Entry e " +
            " join fetch e.shares s " +
            " where e.id=?1 ")
    Optional<Entry> findByIdWithShares(long id);

    @Query(" select e from Entry e " +
            " left join fetch e.shares s " +
            " where e.sheet.id=?1 ")
    List<Entry> findAllWithSharesBySheetId(Long id);

}
