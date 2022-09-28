package com.os;

import java.util.Objects;
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

    private final MongoTemplate template;

    @Override
    public long countMachineByFilter(CriteriaDefinition c) {
        var aggregation = Aggregation.newAggregation(Aggregation.match(c),
                Aggregation.group("machineId"),
                Aggregation.count().as("count"));
        var result = template.aggregate(aggregation, ActivationDetailDocument.class, CountIdentifier.class)
                .getUniqueMappedResult();
        return Objects.isNull(result) ? 0 : result.getCount();
    }

    @Override
    public Page<GroupIdentifier> searchMachineByFilter(CriteriaDefinition c, Pageable pageable) {
        var count = countMachineByFilter(c);
        var size = pageable.getPageSize();
        var doneItems = Long.valueOf((pageable.getPageNumber() * size));
        var aggregation = Aggregation.newAggregation(Aggregation.match(c),
                Aggregation.group("machineId"),
                Aggregation.skip(doneItems),
                Aggregation.limit(size));
        var result = template.aggregate(aggregation, ActivationDetailDocument.class, GroupIdentifier.class)
                .getMappedResults();
        return new PageImpl<>(result, pageable, count);
    }
}
