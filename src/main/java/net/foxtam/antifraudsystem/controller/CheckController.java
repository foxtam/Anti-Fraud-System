package net.foxtam.antifraudsystem.controller;

import net.foxtam.antifraudsystem.service.CardService;
import net.foxtam.antifraudsystem.service.IPService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.ArrayList;

@RestController
public class CheckController {
    private final IPService ipService;
    private final CardService cardService;

    @Autowired
    public CheckController(IPService ipService, CardService cardService) {
        this.ipService = ipService;
        this.cardService = cardService;
    }

    @PostMapping("/api/antifraud/transaction")
    public Response evaluateAmount(@Valid @RequestBody Payment payment) {
        var prohibitedReasons = new ArrayList<String>();
        if (payment.getAmount() > 1500) prohibitedReasons.add("amount");
        if (cardService.hasCard(payment.getCard())) prohibitedReasons.add("card-number");
        if (ipService.hasIP(payment.getIp())) prohibitedReasons.add("ip");
        if (!prohibitedReasons.isEmpty()) {
            String reasons = String.join(", ", prohibitedReasons);
            return new Response("PROHIBITED", reasons);
        }

        if (payment.getAmount() <= 200) {
            return new Response("ALLOWED", "none");
        } else if (payment.getAmount() <= 1500) {
            return new Response("MANUAL_PROCESSING", "amount");
        } else {
            throw new IllegalStateException();
        }
    }

    record Response(String result, String info) {
    }
}
