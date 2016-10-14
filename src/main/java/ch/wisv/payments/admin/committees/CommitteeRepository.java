package ch.wisv.payments.admin.committees;

import ch.wisv.payments.model.Committee;
import ch.wisv.payments.model.CommitteeEnum;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CommitteeRepository extends JpaRepository<Committee, Integer> {

    Optional<Committee> findOneByNameAndYear(CommitteeEnum committeeEnum, int year);

}
