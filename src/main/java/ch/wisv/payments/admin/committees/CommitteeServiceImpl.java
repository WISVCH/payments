package ch.wisv.payments.admin.committees;

import ch.wisv.payments.model.Committee;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CommitteeServiceImpl implements CommitteeService {

    private CommitteeRepository committeeRepository;

    @Autowired
    public CommitteeServiceImpl(CommitteeRepository committeeRepository) {
        this.committeeRepository = committeeRepository;
    }

    @Override
    public List<Committee> getAllCommittees() {
        return committeeRepository.findAll();
    }

    @Override
    public void addCommittee(Committee committee) {
        if (committeeRepository.findOneByNameAndYear(committee.getName(), committee.getYear()).isPresent()) {
            throw new DuplicateKeyException("Commitee already exists!");
        }
        committeeRepository.save(committee);
    }
}
