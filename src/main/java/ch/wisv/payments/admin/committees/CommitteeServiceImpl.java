package ch.wisv.payments.admin.committees;

import ch.wisv.payments.exception.CommitteeNotFoundException;
import ch.wisv.payments.model.Committee;
import ch.wisv.payments.model.CommitteeEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;

import javax.swing.text.html.Option;
import java.util.List;
import java.util.Optional;

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
            throw new DuplicateKeyException("Committee already exists!");
        }
        committeeRepository.save(committee);
    }

    @Override
    public Committee getCommitteeById(int committeeId) {
        return committeeRepository.findOne(committeeId);
    }

    @Override
    public Committee getCommittee(CommitteeEnum committeeEnum, int year) {
        return committeeRepository.findOneByNameAndYear(committeeEnum, year)
                .orElseThrow(CommitteeNotFoundException::new);
    }
}
