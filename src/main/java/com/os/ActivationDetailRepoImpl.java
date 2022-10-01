package com.os;

import static org.springframework.data.mongodb.core.aggregation.Fields.UNDERSCORE_ID;
import java.util.Arrays;
import java.util.Objects;
import org.bson.Document;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.query.CriteriaDefinition;
import org.springframework.stereotype.Repository;
import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class ActivationDetailRepoImpl implements ActivationDetailRepo {

    private static final String MACHINE_COLLECTION = "Machine";
    private static final String MACHINE_ID_F = "machineId";
    private static final String LINE_DESC_F = "productLineDescription";

    private final MongoTemplate template;

    @Override
    public long countMachineByFilter(CriteriaDefinition c) {
        var aggregation = Aggregation.newAggregation(Aggregation.match(c),
                Aggregation.group(MACHINE_ID_F),
                Aggregation.count().as("count"));
        var result = template.aggregate(aggregation, ActivationDetailDocument.class, CountIdentifier.class)
                .getUniqueMappedResult();
        return Objects.isNull(result) ? 0 : result.getCount();
    }

    @Override
    public Page<AggregateResponse> searchMachineByFilter(CriteriaDefinition c, Pageable pageable) {

        var count = countMachineByFilter(c);
        var size = pageable.getPageSize();
        var doneItems = Long.valueOf((pageable.getPageNumber() * size));

        /**
          { "$and" : 
            [
                { "entitlementInfo.dlfEntitlementId" : "24723871"}, 
                { "entitlementInfo.purchaseOrder" : "4502229319"}
            ]
          }
         */
        var matchStage = Aggregation.match(c);
        /**
         {
          _id: "$machineId",
          productLineDescription: {
          $first: '$productInfo.productLineDescription',
         }
        }*/
        var groupStage = Aggregation.group(MACHINE_ID_F)
                .first("productInfo.productLineDescription")
                .as(LINE_DESC_F);

        /**
           {
              from: "Machine",
              localField: "_id",
              foreignField: "_id",
              as: "machineList"
           }
         */
        var lookupStage = Aggregation.lookup(MACHINE_COLLECTION, UNDERSCORE_ID, UNDERSCORE_ID, "MachineList");

        /**
         * {
              "_id": "$_id",
              "productLineDescription": "$productLineDescription",
              "machineName": { $arrayElemAt: ["$machines.machineName", 0] }
 
           }
         */
        var projectStage = Aggregation.project(UNDERSCORE_ID, LINE_DESC_F)
                .and(t -> new Document("$arrayElemAt", Arrays.asList("$MachineList.machineName", 0))).as("machineName");

        var aggregation = Aggregation.newAggregation(matchStage,
                groupStage,
                Aggregation.skip(doneItems),
                Aggregation.limit(size),
                lookupStage,
                projectStage);

        var result = template.aggregate(aggregation, ActivationDetailDocument.class, AggregateResponse.class)
                .getMappedResults();
        return new PageImpl<>(result, pageable, count);
    }
}
