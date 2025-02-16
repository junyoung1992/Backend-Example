package io.springbatch.exercise.batch.partition;

import io.springbatch.exercise.batch.domain.ProductVO;
import io.springbatch.exercise.batch.job.api.QueryGenerator;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.partition.support.Partitioner;
import org.springframework.batch.item.ExecutionContext;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

@RequiredArgsConstructor
public class ProductPartitioner implements Partitioner {

    private final DataSource dataSource;

    @Override
    public Map<String, ExecutionContext> partition(int gridSize) {
        Map<String, ExecutionContext> result = new HashMap<>();

        ProductVO[] productList = QueryGenerator.getProductList(dataSource);
        for (int i = 0; i < productList.length; i++) {
            ExecutionContext value = new ExecutionContext();
            result.put("partition" + i, value);
            value.putString("productType", productList[i].getType());
        }

        return result;
    }

}
