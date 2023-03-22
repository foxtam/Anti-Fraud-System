package net.foxtam.antifraudsystem.persistance;

import net.foxtam.antifraudsystem.model.Card;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface CardRepository extends CrudRepository<Card, Long> {
    @Transactional
    long deleteByNumber(String number);

    boolean existsByNumber(String number);

    List<Card> getByOrderByIdAsc();
}
