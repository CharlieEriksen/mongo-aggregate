package com.os;

import java.time.Duration;
import java.time.Instant;
import java.util.Map;
import org.springframework.data.domain.PageRequest;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@EnableScheduling
@RequiredArgsConstructor
public class Tester {
    
    private final ActivationDetailRepo repo;

    @Scheduled(initialDelay = 1000, fixedDelay = 900000000)
    public void startTest() {
        
        var c = new FilterBuilder(Map.of("entitlementId", "24723871", 
                "purchaseOrder", "4502229319", "salesOrderId", "310925562"))
                .getCriteria();
        
        
        var start = Instant.now();
        var response = repo.searchMachineByFilter(c, PageRequest.of(0, 3));
        log.info("Time taken {}", Duration.between(start, Instant.now()).toMillis());
        log.info("Data response size {}", response.getContent().size());
        log.info("Data response {}", response.getContent());
    }
}
