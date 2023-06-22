package collectedgarbage.DBtoDBMigrator;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Sample {

    private Long sampleKey;
    private String sampleData;

    public Sample() {}

    public Sample(Long sampleKey, String sampleData) {
        this.sampleKey = sampleKey;
        this.sampleData = sampleData;
    }


}
