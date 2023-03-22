package net.foxtam.antifraudsystem.persistance;

import net.foxtam.antifraudsystem.model.IP;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface IPRepository extends CrudRepository<IP, Long> {
    boolean existsByIp(String ip);

    List<IP> getByOrderByIdAsc();
    
    @Transactional
    long deleteByIp(String ip);
    
}
