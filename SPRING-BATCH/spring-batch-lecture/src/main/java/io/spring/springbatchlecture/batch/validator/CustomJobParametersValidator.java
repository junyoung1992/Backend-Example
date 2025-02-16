package io.spring.springbatchlecture.batch.validator;

import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.JobParametersValidator;
import org.springframework.util.StringUtils;

public class CustomJobParametersValidator implements JobParametersValidator {

    @Override
    public void validate(JobParameters parameters) throws JobParametersInvalidException {
        if (parameters == null) {
            throw new JobParametersInvalidException("parameters is not found");
        }

        if (!StringUtils.hasText(parameters.getString("name"))) {
            throw new JobParametersInvalidException("name parameters is not found");
        }
    }

}
