package net.foxtam.antifraudsystem.controller;

import net.foxtam.antifraudsystem.exceptions.AlreadyExistsException;
import net.foxtam.antifraudsystem.exceptions.NotFoundException;
import net.foxtam.antifraudsystem.exceptions.WrongFormatException;
import net.foxtam.antifraudsystem.model.Card;
import net.foxtam.antifraudsystem.service.CardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.WebRequest;

import java.util.Map;

@RestController
public class CardController {

    private final CardService cardService;

    @Autowired
    public CardController(CardService cardService) {
        this.cardService = cardService;
    }

    @PostMapping("/api/antifraud/stolencard")
    public ResponseEntity<?> addCart(@RequestBody Card card) {
        try {
            cardService.addCard(card);
            return ResponseEntity.ok(card);
        } catch (AlreadyExistsException e) {
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }
    }

    @DeleteMapping("/api/antifraud/stolencard/{number}")
    public ResponseEntity<?> deleteCard(@PathVariable String number) {
        try {
            cardService.deleteCard(new Card(number));
            String cardInfo = "Card %s successfully removed!".formatted(number);
            return ResponseEntity.ok(Map.of("status", cardInfo));
        } catch (NotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/api/antifraud/stolencard")
    public ResponseEntity<?> getAllCards() {
        return ResponseEntity.ok(cardService.getAllCards());
    }
}
