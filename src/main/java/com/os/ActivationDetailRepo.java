package com.os;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.query.CriteriaDefinition;
import lombok.Data;

public interface ActivationDetailRepo {
    /*
    @Aggregation({
        "{$match:{'entitlementInfo.purchaseOrder' : ?0}}",
        "{$group:{ _id: '$machineId'}}",
        "{$count: 'count'}"
            }
    )
    List<ActivationDetailDocument> searchMachineId(String po);
    */
    
    long countMachineByFilter(CriteriaDefinition c);
    
    Page<AggregateResponse> searchMachineByFilter(CriteriaDefinition c, Pageable pageable);
    
    @Data
    public static final class AggregateResponse {
        
        @Field("_id")
        private String machineId;   
        
        private String productLineDescription;
        
        private String machineName;
    }
    
    @Data
    public static final class CountIdentifier {
        private long count;
    }
}
