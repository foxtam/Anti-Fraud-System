package net.foxtam.antifraudsystem.controller;

import net.foxtam.antifraudsystem.exceptions.AlreadyExistsException;
import net.foxtam.antifraudsystem.exceptions.NotFoundException;
import net.foxtam.antifraudsystem.exceptions.WrongFormatException;
import net.foxtam.antifraudsystem.model.IP;
import net.foxtam.antifraudsystem.service.IPService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.WebRequest;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;

@RestController
public class IPController {
    private static final Logger log = LoggerFactory.getLogger(IPController.class);
    private final IPService ipService;

    @Autowired
    public IPController(IPService ipService) {
        this.ipService = ipService;
    }

    @PostMapping("/api/antifraud/suspicious-ip")
    public ResponseEntity<IP> addIP(@Valid @RequestBody IP ip) {
        try {
            ipService.addIP(ip);
            return ResponseEntity.ok(ip);
        } catch (AlreadyExistsException e) {
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }
    }

    @DeleteMapping("/api/antifraud/suspicious-ip/{ip}")
    public ResponseEntity<?> deleteIP(@PathVariable String ip) {
        IP anIP = new IP(ip);
        try {
            ipService.deleteIP(anIP);
            String ipInfo = "IP %s successfully removed!".formatted(anIP.getIp());
            Map<String, String> status = Map.of("status", ipInfo);
            return ResponseEntity.ok(status);
        } catch (NotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/api/antifraud/suspicious-ip")
    public ResponseEntity<List<IP>> getAllIPs() {
        return ResponseEntity.ok(ipService.getAllIPs());
    }
}
