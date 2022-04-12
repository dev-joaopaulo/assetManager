package com.assets.manager.broker;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

import static com.assets.manager.util.UriUtil.getUri;

@RestController
@RequestMapping("/api/v1/broker")
public class BrokerController {

    @Autowired
    private BrokerService brokerService;

    @GetMapping("/{id}")
    public ResponseEntity get(@PathVariable Long id){
        return ResponseEntity.ok(new BrokerDTO(brokerService.getBrokerById(id)));
    }

    @PostMapping()
    @Secured({"ROLE_ADMIN"})
    public ResponseEntity post(@RequestBody BrokerDTO brokerDTO){
        BrokerDTO savedBroker = brokerService.insert(brokerDTO);
        URI location = getUri(savedBroker.getId());
        return ResponseEntity.created(location).build();
    }
}
