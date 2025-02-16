package io.springbatch.exercise.batch.classifier;

import io.springbatch.exercise.batch.domain.ApiRequestVO;
import io.springbatch.exercise.batch.domain.ProductVO;
import lombok.Setter;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.classify.Classifier;

import java.util.HashMap;
import java.util.Map;

@Setter
public class ProcessorClassifier<C, T> implements Classifier<C, T> {

    private Map<String, ItemProcessor<ProductVO, ApiRequestVO>> processorMap = new HashMap<>();

    @Override
    public T classify(C classifiable) {
        return (T) processorMap.get(((ProductVO) classifiable).getType());
    }

}
