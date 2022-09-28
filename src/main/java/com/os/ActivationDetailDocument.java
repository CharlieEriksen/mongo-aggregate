package com.os;

import java.io.Serializable;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import lombok.Data;

@Data
@Document("ActivationDetail")
public class ActivationDetailDocument implements Serializable {

    private static final long serialVersionUID = 1L;
    
    @Id
    @Field("_id")
    private Integer id;

    private String machineId;
    
    private EntitlementInfo entitlementInfo;
    
    private CompanyInfo companyInfo;
    
    private ProductInfo productInfo;

    @Data
    public static final class EntitlementInfo implements Serializable {
        
        private static final long serialVersionUID = 1L;
        
        private String dlfEntitlementId;
        private String lac;
        private String purchaseOrder;
        private String salesOrderId;

    }
    
    @Data
    public static final class CompanyInfo implements Serializable {
        
        private static final long serialVersionUID = 1L;
        
        private String companyName;
        private String companyDuns;
    }
    
    @Data
    public static final class ProductInfo implements Serializable {
        
        private static final long serialVersionUID = 1L;
        private String skuDescription;
    }
}
