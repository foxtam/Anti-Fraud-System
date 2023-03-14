package net.foxtam.antifraudsystem.persistance;

import net.foxtam.antifraudsystem.model.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends CrudRepository<User, Long> {
    Optional<User> findByUsernameIgnoreCase(String username);
    boolean existsByUsernameIgnoreCase(String username);
    List<User> getByOrderByIdAsc();
    @Transactional
    long deleteByUsernameIgnoreCase(String username);
}
