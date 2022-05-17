package com.assets.manager.broker;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

import static com.assets.manager.util.UriUtil.getUri;

@RestController
@RequestMapping("/api/v1/broker")
public class BrokerController {

    private final BrokerService brokerService;

    public BrokerController(BrokerService brokerService) {
        this.brokerService = brokerService;
    }


    @GetMapping("/{id}")
    public ResponseEntity get(@PathVariable Long id){
        return ResponseEntity.ok(new BrokerDTO(brokerService.getBrokerById(id)));
    }

    @GetMapping()
    public ResponseEntity<List<BrokerDTO>> get(){
        List<BrokerDTO> brokers = brokerService.getBrokers();
        return brokers.iterator().hasNext()?
                ResponseEntity.ok(brokers) :
                ResponseEntity.notFound().build();
    }


    @PostMapping()
    @Secured({"ROLE_ADMIN"})
    public ResponseEntity post(@RequestBody BrokerDTO brokerDTO){
        BrokerDTO savedBroker = brokerService.insert(brokerDTO);
        URI location = getUri(savedBroker.getId());
        return ResponseEntity.created(location).build();
    }
}
