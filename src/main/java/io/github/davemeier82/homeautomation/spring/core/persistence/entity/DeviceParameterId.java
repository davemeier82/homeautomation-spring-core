/*
 * Copyright 2021-2024 the original author or authors.
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

package io.github.davemeier82.homeautomation.spring.core.persistence.entity;


import jakarta.persistence.Embeddable;

import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;

@Embeddable
public class DeviceParameterId implements Serializable {

  private UUID deviceId;
  private String name;

  public DeviceParameterId() {
  }

  public DeviceParameterId(UUID deviceId, String name) {
    this.deviceId = deviceId;
    this.name = name;
  }

  public UUID getDeviceId() {
    return deviceId;
  }

  public void setDeviceId(UUID deviceId) {
    this.deviceId = deviceId;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    DeviceParameterId that = (DeviceParameterId) o;
    return Objects.equals(deviceId, that.deviceId) && Objects.equals(name, that.name);
  }

  @Override
  public int hashCode() {
    return Objects.hash(deviceId, name);
  }
}
