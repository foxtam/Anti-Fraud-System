package net.foxtam.antifraudsystem.controller;

import net.foxtam.antifraudsystem.model.Payment;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
public class CheckController {
    @PostMapping("/api/antifraud/transaction")
    public Response evaluateAmount(@Valid @RequestBody Payment payment) {
        if (payment.getAmount() <= 200) {
            return new Response("ALLOWED");
        } else if (payment.getAmount() <= 1500) {
            return new Response("MANUAL_PROCESSING");
        } else {
            return new Response("PROHIBITED");
        }
    }

    record Response(String result) {
    }
}
