package collectedgarbage.DBtoDBMigrator.batch.sample2;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Sample2 {

    private Long sampleKey;
    private String sampleData;

    public Sample2() {}

    public Sample2(Long sampleKey, String sampleData) {
        this.sampleKey = sampleKey;
        this.sampleData = sampleData;
    }


}
