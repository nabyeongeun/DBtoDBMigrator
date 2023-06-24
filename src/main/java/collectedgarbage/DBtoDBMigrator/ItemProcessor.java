package collectedgarbage.DBtoDBMigrator;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ItemProcessor implements org.springframework.batch.item.ItemProcessor<Sample, Sample> {
    @Override
    public Sample process(Sample sample) {
        final Long sampleKey = sample.getSampleKey();
        final String sampleData = sample.getSampleData();

        final Sample convertedSample = new Sample(sampleKey, sampleData);

//        log.info("Converting (" + sample + ") into (" + convertedSample + ")");

        return convertedSample;
    }
}
