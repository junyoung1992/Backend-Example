package io.springbatch.exercise.batch.classifier;

import io.springbatch.exercise.batch.domain.ApiRequestVO;
import lombok.Setter;
import org.springframework.batch.item.ItemWriter;
import org.springframework.classify.Classifier;

import java.util.HashMap;
import java.util.Map;

@Setter
public class WriterClassifier<C, T> implements Classifier<C, T> {

    private Map<String, ItemWriter<ApiRequestVO>> writerMap = new HashMap<>();

    @Override
    public T classify(C classifiable) {
        return (T) writerMap.get(((ApiRequestVO) classifiable).getProductVO().getType());
    }

}
