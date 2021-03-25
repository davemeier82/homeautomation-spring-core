/*
 * Copyright 2021-2021 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.github.davemeier82.homeautomation.spring.core.config;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class DevicesConfig {

  public static final String CURRENT_VERSION = "1.0";

  private final String version;
  private final List<DeviceConfig> devices;

  @JsonCreator
  public DevicesConfig(@JsonProperty("version") String version, @JsonProperty("devices") List<DeviceConfig> devices) {
    this.version = version;
    this.devices = devices;
  }

  public String getVersion() {
    return version;
  }

  public List<DeviceConfig> getDevices() {
    return devices;
  }
}
