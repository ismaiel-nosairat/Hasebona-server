package io.ismaiel.hasebona.repos;

import io.ismaiel.hasebona.entities.Member;
import io.ismaiel.hasebona.entities.Sheet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;

@Repository
public interface MemberRepo extends JpaRepository<Member, Long> {

    public Set<Member> findBySheet(Sheet sheet);

    @Transactional
    Long deleteBySheetId(long id);

    @Query(" select m from Member m left join fetch m.user where m.sheet=?1 ")
    List<Member> findWithUsersBySheet(Sheet sheet);

    @Query(" select count(m) from Member m left join  User u " +
            " on m.user.id=u.id " +
            " where ( m.name=?1 or u.id=?2 )" +
            " and m.sheet.id=?3 ")
    Integer countByNameOrUser(String name, Long userId, Long sheetId);

    @Query(" select count(m) from Member m " +
            " where m.name=?1 " +
            " and m.sheet.id=?2 ")
    Integer countByName(String name, Long sheetId);

    Member findBySheetAndId(Sheet sheet, Long memberId);

    @Query(" select m from Member m " +
            " left join fetch m.shares s " +
            " left join fetch s.entry " +
            " where m.id=?2 " +
            " and m.sheet=?1")
    Member findBySheetAndIdWithEntries(Sheet sheet, Long memberId);
}
