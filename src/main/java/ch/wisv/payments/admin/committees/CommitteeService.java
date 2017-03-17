package ch.wisv.payments.admin.committees;

import ch.wisv.payments.model.Committee;
import ch.wisv.payments.model.CommitteeEnum;

import java.util.List;

public interface CommitteeService {

    List<Committee> getAllCommittees();

    void addCommittee(Committee committee);

    Committee getByNameAndYear(CommitteeEnum name, Integer year);

    Committee getById(int id);
}
