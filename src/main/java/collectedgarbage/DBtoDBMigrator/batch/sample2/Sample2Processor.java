package collectedgarbage.DBtoDBMigrator.batch.sample2;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class Sample2Processor implements org.springframework.batch.item.ItemProcessor<Sample2, Sample2> {
    @Override
    public Sample2 process(Sample2 sample) {
        final Long sampleKey = sample.getSampleKey();
        final String sampleData = sample.getSampleData();

        final Sample2 convertedSample = new Sample2(sampleKey, sampleData);

//        log.info("Converting (" + sample + ") into (" + convertedSample + ")");

        return convertedSample;
    }
}
