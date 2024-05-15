package org.gbif.wrangler.discovery;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
public class InstanceDetails implements Serializable {

    private String name;

    private String version;

    private String address;

    private String port;

    @Builder
    public InstanceDetails(String name, String version, String address, String port) {
        this.name = name;
        this.version = version;
        this.address = address;
        this.port = port;
    }
}
