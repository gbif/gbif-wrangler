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

import java.io.Serializable;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

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
