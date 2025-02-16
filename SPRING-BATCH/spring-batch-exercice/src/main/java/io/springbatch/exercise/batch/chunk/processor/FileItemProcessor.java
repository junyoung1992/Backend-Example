package io.springbatch.exercise.batch.chunk.processor;

import io.springbatch.exercise.batch.domain.Product;
import io.springbatch.exercise.batch.domain.ProductMapper;
import io.springbatch.exercise.batch.domain.ProductVO;
import org.springframework.batch.item.ItemProcessor;

public class FileItemProcessor implements ItemProcessor<ProductVO, Product> {

    @Override
    public Product process(ProductVO item) throws Exception {
        return ProductMapper.INSTANCE.toProduct(item);
    }

}
