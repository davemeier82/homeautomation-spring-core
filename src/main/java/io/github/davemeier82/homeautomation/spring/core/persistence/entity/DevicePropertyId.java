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

import java.util.Objects;

@Embeddable
public class DevicePropertyId {
  private String deviceId;
  private String deviceType;
  private String devicePropertyId;
  private String devicePropertyValueType;

  public DevicePropertyId() {
  }

  public DevicePropertyId(String deviceId, String deviceType, String devicePropertyId, String devicePropertyValueType) {
    this.deviceId = deviceId;
    this.deviceType = deviceType;
    this.devicePropertyId = devicePropertyId;
    this.devicePropertyValueType = devicePropertyValueType;
  }

  public String getDeviceId() {
    return deviceId;
  }

  public String getDeviceType() {
    return deviceType;
  }

  public String getDevicePropertyId() {
    return devicePropertyId;
  }

  public String getDevicePropertyValueType() {
    return devicePropertyValueType;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    DevicePropertyId that = (DevicePropertyId) o;
    return Objects.equals(deviceId, that.deviceId) && Objects.equals(deviceType, that.deviceType) && Objects.equals(devicePropertyId,
        that.devicePropertyId) && Objects.equals(devicePropertyValueType, that.devicePropertyValueType);
  }

  @Override
  public int hashCode() {
    return Objects.hash(deviceId, deviceType, devicePropertyId, devicePropertyValueType);
  }
}
