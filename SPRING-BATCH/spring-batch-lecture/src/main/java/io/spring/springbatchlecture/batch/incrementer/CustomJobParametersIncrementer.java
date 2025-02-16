package io.spring.springbatchlecture.batch.incrementer;

import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.JobParametersIncrementer;

import java.text.SimpleDateFormat;
import java.util.Date;

public class CustomJobParametersIncrementer implements JobParametersIncrementer {

    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyyMMddHHmmss");

    @Override
    public JobParameters getNext(JobParameters parameters) {
        String id = DATE_FORMAT.format(new Date());

        return new JobParametersBuilder()
                .addString("run.id", id)
                .toJobParameters();
    }

}
