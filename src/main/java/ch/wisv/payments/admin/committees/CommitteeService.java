package ch.wisv.payments.admin.committees;

import ch.wisv.payments.model.Committee;

import java.util.List;

public interface CommitteeService {

    List<Committee> getAllCommittees();

    void addCommittee(Committee committee);
}
