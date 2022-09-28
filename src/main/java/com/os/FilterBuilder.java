package com.os;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.springframework.data.mongodb.core.query.Criteria;

public class FilterBuilder {

    private final List<Criteria> criterias = new ArrayList<>();

    public FilterBuilder(Map<String, String> requestParam) {

        if (requestParam.containsKey("entitlementId")) {
            var c = Criteria.where("entitlementInfo.dlfEntitlementId").is(requestParam.get("entitlementId"));
            criterias.add(c);
        }
        if (requestParam.containsKey("purchaseOrder")) {
            var c = Criteria.where("entitlementInfo.purchaseOrder").is(requestParam.get("purchaseOrder"));
            criterias.add(c);
        }
        if (requestParam.containsKey("salesOrder")) {
            var c = Criteria.where("entitlementInfo.salesOrder").is(requestParam.get("salesOrder"));
            criterias.add(c);
        }
        if (requestParam.containsKey("companyName")) {
            var c = Criteria.where("companyInfo.companyName").is(requestParam.get("companyName"));
            criterias.add(c);
        }
    }
    
    public Criteria getCriteria() {
        if (criterias.isEmpty()) {
            return null;
        }
        var it = criterias.iterator();
        var cri = it.next();
        while (it.hasNext()) {
            cri.andOperator(it.next());
        }
        return cri;
    }
}
