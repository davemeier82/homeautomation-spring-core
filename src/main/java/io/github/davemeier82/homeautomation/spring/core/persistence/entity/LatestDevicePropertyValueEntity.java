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

import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

import java.time.OffsetDateTime;
import java.util.Objects;

@Entity
@Table(name = "latest_device_property_value")
public class LatestDevicePropertyValueEntity {
  @EmbeddedId
  private DevicePropertyId id;
  private String deviceDisplayName;
  private String devicePropertyType;
  private String devicePropertyDisplayName;
  private String value;
  private OffsetDateTime timestamp;

  public LatestDevicePropertyValueEntity() {
  }

  public DevicePropertyId getId() {
    return id;
  }

  public String getDeviceDisplayName() {
    return deviceDisplayName;
  }

  public String getDevicePropertyType() {
    return devicePropertyType;
  }

  public String getDevicePropertyDisplayName() {
    return devicePropertyDisplayName;
  }

  public String getValue() {
    return value;
  }

  public OffsetDateTime getTimestamp() {
    return timestamp;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    LatestDevicePropertyValueEntity that = (LatestDevicePropertyValueEntity) o;
    return Objects.equals(id, that.id);
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(id);
  }
}
