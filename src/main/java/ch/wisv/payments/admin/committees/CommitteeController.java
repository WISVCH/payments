package ch.wisv.payments.admin.committees;

import ch.wisv.payments.model.Committee;
import ch.wisv.payments.model.Order;
import ch.wisv.payments.rest.OrderService;
import ch.wisv.payments.rest.OrderServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping(value = "/committees")
public class CommitteeController {

    private CommitteeService committeeService;
    private OrderService orderService;

    @Autowired
    public CommitteeController(CommitteeService committeeService, OrderService orderService) {
        this.committeeService = committeeService;
        this.orderService = orderService;
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

    @GetMapping("/{committeeId}")
    public String committeInfoSelected(@PathVariable int committeeId, Model model) {

        Committee committee = committeeService.getById(committeeId);

        List<Order> ordersByCommittee = orderService.getOrdersByCommittee(committee);
        model.addAttribute("selected", true);
        model.addAttribute("committees", committeeService.getAllCommittees());
        model.addAttribute("committee", committee);
        model.addAttribute("orders", ordersByCommittee);
        model.addAttribute("transactionCost", orderService.getTransactionCostByCommittee(committee));

        return "committeeinfo";

    }

    @ExceptionHandler(DuplicateKeyException.class)
    String handleDuplicateKeyException(DuplicateKeyException exception) {
        return "redirect:/committees";
    }
}
