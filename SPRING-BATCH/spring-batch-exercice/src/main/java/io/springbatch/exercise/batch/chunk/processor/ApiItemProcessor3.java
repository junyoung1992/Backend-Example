package io.springbatch.exercise.batch.chunk.processor;

import io.springbatch.exercise.batch.domain.ApiRequestVO;
import io.springbatch.exercise.batch.domain.ProductVO;
import org.springframework.batch.item.ItemProcessor;

public class ApiItemProcessor3 implements ItemProcessor<ProductVO, ApiRequestVO> {

    @Override
    public ApiRequestVO process(ProductVO item) throws Exception {
        return ApiRequestVO.builder()
                .id(item.getId())
                .productVO(item)
                .build();
    }

}
