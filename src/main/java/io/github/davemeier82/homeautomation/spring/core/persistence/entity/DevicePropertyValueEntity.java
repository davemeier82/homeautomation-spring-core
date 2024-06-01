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

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

import java.time.OffsetDateTime;
import java.util.Objects;
import java.util.UUID;

@Entity(name = "device_property_value")
public class DevicePropertyValueEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  private UUID id;
  private UUID devicePropertyId;
  private String type;
  private String value;
  private OffsetDateTime timestamp;

  public DevicePropertyValueEntity() {
  }

  public DevicePropertyValueEntity(UUID devicePropertyId, String type, String value, OffsetDateTime timestamp) {
    this.devicePropertyId = devicePropertyId;
    this.type = type;
    this.value = value;
    this.timestamp = timestamp;
  }

  public UUID getId() {
    return id;
  }

  public UUID getDevicePropertyId() {
    return devicePropertyId;
  }

  public String getType() {
    return type;
  }

  public String getValue() {
    return value;
  }

  public void setValue(String value) {
    this.value = value;
  }

  public OffsetDateTime getTimestamp() {
    return timestamp;
  }

  public void setTimestamp(OffsetDateTime timestamp) {
    this.timestamp = timestamp;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    DevicePropertyValueEntity that = (DevicePropertyValueEntity) o;
    return Objects.equals(id, that.id);
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(id);
  }
}
