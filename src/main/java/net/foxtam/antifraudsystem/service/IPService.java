package net.foxtam.antifraudsystem.service;

import net.foxtam.antifraudsystem.exceptions.AlreadyExistsException;
import net.foxtam.antifraudsystem.exceptions.NotFoundException;
import net.foxtam.antifraudsystem.model.IP;
import net.foxtam.antifraudsystem.persistance.IPRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class IPService {
    private final IPRepository ipRepository;

    @Autowired
    public IPService(IPRepository ipRepository) {
        this.ipRepository = ipRepository;
    }

    public void addIP(IP ip) throws AlreadyExistsException {
        boolean exists = ipRepository.existsByIp(ip.getIp());
        if (exists) throw new AlreadyExistsException(ip.getIp());
        ipRepository.save(ip);
    }

    public void deleteIP(IP ip) throws NotFoundException {
        long deleted = ipRepository.deleteByIp(ip.getIp());
        if (deleted == 0) throw new NotFoundException(ip.getIp());
    }

    public List<IP> getAllIPs() {
        return ipRepository.getByOrderByIdAsc();
    }

    public boolean hasIP(IP ip) {
        return ipRepository.existsByIp(ip.getIp());
    }
}
