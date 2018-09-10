
package io.ismaiel.hasebona.repos;


import io.ismaiel.hasebona.entities.Share;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ShareRepo extends JpaRepository<Share, Long> {

}
