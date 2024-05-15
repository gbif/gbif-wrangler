/*
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.gbif.wrangler.discovery;

import java.util.Collection;

import org.apache.curator.test.TestingServer;
import org.apache.curator.x.discovery.ServiceInstance;
import org.junit.jupiter.api.*;

import lombok.SneakyThrows;

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
