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

import java.io.Closeable;
import java.io.IOException;
import java.net.InetAddress;
import java.util.Optional;

import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.curator.x.discovery.ServiceDiscovery;
import org.apache.curator.x.discovery.ServiceDiscoveryBuilder;
import org.apache.curator.x.discovery.ServiceInstance;

import lombok.Builder;
import lombok.Data;
import lombok.SneakyThrows;

@Data
public class DiscoveryClient implements Closeable {

    private final CuratorFramework client;

    private final ServiceDiscovery<InstanceDetails> serviceDiscovery;

    private final ServiceInstance<InstanceDetails> serviceInstance;

    private static final int BASE_SLEEP_TIME_MS = 1000;

    private static final int MAX_RETRIES = 3;

    @SneakyThrows
    @Builder
    public DiscoveryClient(String connectionString, String zkPath, InstanceDetails instanceDetails, Integer baseSleepTimeMs, Integer maxRetries) {
        client = CuratorFrameworkFactory.newClient(connectionString, exponentialBackoffRetry(baseSleepTimeMs,maxRetries));
        client.start();
        serviceDiscovery = serviceDiscovery(client, zkPath);
        serviceInstance = serviceInstance(instanceDetails);
        serviceDiscovery.registerService(serviceInstance);
    }

    @SneakyThrows
    private static ServiceInstance<InstanceDetails> serviceInstance(InstanceDetails instanceDetails) {
        return ServiceInstance.<InstanceDetails>builder().name(instanceDetails.getName())
                .address(InetAddress.getLocalHost().getCanonicalHostName()) // Service information
                .payload(instanceDetails)
                .build();
    }

    private static ServiceDiscovery<InstanceDetails> serviceDiscovery(CuratorFramework client, String zkPath) {
        return ServiceDiscoveryBuilder.builder(InstanceDetails.class)
                .client(client)
                .basePath(zkPath)
                .build();
    }

    private static RetryPolicy exponentialBackoffRetry(Integer baseSleepTimeMs, Integer maxRetries) {
        return new ExponentialBackoffRetry(Optional.ofNullable(baseSleepTimeMs).orElse(BASE_SLEEP_TIME_MS),
                                           Optional.ofNullable(maxRetries).orElse(MAX_RETRIES));
    }

    @Override
    public void close() throws IOException {
        serviceDiscovery.close();
        client.close();
    }
}
