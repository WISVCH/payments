package ch.wisv.payments.admin.committees;

import ch.wisv.payments.model.Committee;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping(value = "/committees")
public class CommitteeController {

    private CommitteeService committeeService;

    @Autowired
    public CommitteeController(CommitteeService committeeService) {
        this.committeeService = committeeService;
    }

    @GetMapping
    String committees(Model model) {
        model.addAttribute("committees", committeeService.getAllCommittees());
        model.addAttribute("committee", new Committee());
        return "committees";
    }

    @PostMapping(value = "/add")
    String addCommittee(@ModelAttribute Committee committee) {

        committeeService.addCommittee(committee);

        return "redirect:/committees";
    }

    @ExceptionHandler(DuplicateKeyException.class)
    String handleDuplicateKeyException(DuplicateKeyException exception) {
        return "redirect:/committees";
    }
}
