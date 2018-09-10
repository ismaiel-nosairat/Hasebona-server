
package io.ismaiel.hasebona.repos;


import io.ismaiel.hasebona.entities.Sheet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SheetRepo extends JpaRepository<Sheet, Long> {

    @Query("select s from Sheet s " +
            "inner join Permission p on p.sheet.id=s.id " +
            " where p.user.id=?1")
    public List<Sheet> findByPermissions_User(Long userId);


}
