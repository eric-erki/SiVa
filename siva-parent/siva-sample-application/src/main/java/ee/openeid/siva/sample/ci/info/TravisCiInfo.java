package ee.openeid.siva.sample.ci.info;

import lombok.Data;

@Data
public class TravisCiInfo {
    String buildUrl;
    String buildNumber;
}