package net.foxtam.antifraudsystem.service;

import net.foxtam.antifraudsystem.exceptions.AlreadyExistsException;
import net.foxtam.antifraudsystem.exceptions.NotFoundException;
import net.foxtam.antifraudsystem.model.Card;
import net.foxtam.antifraudsystem.persistance.CardRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CardService {

    private final CardRepository cardRepository;

    @Autowired
    public CardService(CardRepository cardRepository) {
        this.cardRepository = cardRepository;
    }

    public void addCard(Card card) throws AlreadyExistsException {
        boolean exists = cardRepository.existsByNumber(card.getNumber());
        if (exists) throw new AlreadyExistsException(card.getNumber());
        cardRepository.save(card);
    }

    public void deleteCard(Card card) throws NotFoundException {
        long deleted = cardRepository.deleteByNumber(card.getNumber());
        if (deleted == 0) throw new NotFoundException(card.getNumber());
    }

    public List<Card> getAllCards() {
        return cardRepository.getByOrderByIdAsc();
    }

    public boolean hasCard(Card card) {
        return cardRepository.existsByNumber(card.getNumber());
    }
}
