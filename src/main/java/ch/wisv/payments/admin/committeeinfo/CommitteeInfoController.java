package ch.wisv.payments.admin.committeeinfo;

import ch.wisv.payments.admin.committees.CommitteeService;
import ch.wisv.payments.model.Committee;
import ch.wisv.payments.model.Order;
import ch.wisv.payments.rest.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("committeeinfo")
public class CommitteeInfoController {

    private OrderService orderService;
    private CommitteeService committeeService;

    @Autowired
    public CommitteeInfoController(OrderService orderService, CommitteeService committeeService) {
        this.orderService = orderService;
        this.committeeService = committeeService;
    }

    @GetMapping
    public String commiteeInfoIndex(Model model) {
        model.addAttribute("committees", committeeService.getAllCommittees());

        return "committeeinfo";
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
}
