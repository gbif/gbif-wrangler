package org.gbif.wrangler.discovery;

import lombok.SneakyThrows;
import org.apache.curator.test.TestingServer;
import org.apache.curator.x.discovery.ServiceInstance;
import org.gbif.wrangler.discovery.DiscoveryClient;
import org.gbif.wrangler.discovery.InstanceDetails;
import org.junit.jupiter.api.*;

import java.util.Collection;

public class DiscoveryTest {

    private static TestingServer testingServer;
    @BeforeAll
    public static void setup() throws Exception {
        testingServer = new TestingServer(true);
        testingServer.start();
    }

    @AfterAll
    public static void teardown() throws Exception {
        testingServer.stop();
    }

    @Test
    @SneakyThrows
    public void testRegisterService() {
        try(DiscoveryClient discoveryClient = DiscoveryClient.builder()
                .connectionString(testingServer.getConnectString())
                .zkPath("/services/test")
                .instanceDetails(InstanceDetails.builder().name("testService").build())
                .build()) {
            Collection<ServiceInstance<InstanceDetails>> instances = discoveryClient.getServiceDiscovery().queryForInstances("testService");
            Assertions.assertFalse(instances.isEmpty(), "Registered instances is empty");

        }
    }

}
