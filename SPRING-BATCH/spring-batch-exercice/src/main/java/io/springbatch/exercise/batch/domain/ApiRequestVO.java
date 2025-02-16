package io.springbatch.exercise.batch.domain;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class ApiRequestVO {
    private final Long id;
    private final ProductVO productVO;
}
