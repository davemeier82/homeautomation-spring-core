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

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;

@Entity(name = "device")
public class DeviceEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  private UUID id;
  private String deviceType;
  private String deviceId;
  private String displayName;

  @OneToMany(
      mappedBy = "device",
      cascade = CascadeType.ALL,
      orphanRemoval = true
  )
  private Set<DeviceParameterEntity> deviceParameters = new HashSet<>();
  @OneToMany(
      mappedBy = "device",
      cascade = CascadeType.ALL,
      orphanRemoval = true
  )
  private Set<CustomIdentifierEntity> customIdentifiers = new HashSet<>();

  public DeviceEntity() {
  }

  public DeviceEntity(String deviceType, String deviceId, String displayName) {
    this.deviceType = deviceType;
    this.deviceId = deviceId;
    this.displayName = displayName;
  }

  public UUID getId() {
    return id;
  }

  public String getDeviceType() {
    return deviceType;
  }

  public String getDeviceId() {
    return deviceId;
  }

  public String getDisplayName() {
    return displayName;
  }

  public void setDisplayName(String displayName) {
    this.displayName = displayName;
  }

  public void addDeviceParameter(DeviceParameterEntity entity) {
    deviceParameters.add(entity);
    entity.setDevice(this);
  }

  public void removeDeviceParameter(DeviceParameterEntity entity) {
    deviceParameters.remove(entity);
    entity.setDevice(null);
  }

  public Set<DeviceParameterEntity> getDeviceParameters() {
    return deviceParameters;
  }

  public void setDeviceParameters(Set<DeviceParameterEntity> deviceParameters) {
    this.deviceParameters.clear();
    deviceParameters.forEach(this::addDeviceParameter);
  }

  public void addCustomIdentifier(CustomIdentifierEntity entity) {
    customIdentifiers.add(entity);
    entity.setDevice(this);
  }

  public void removeCustomIdentifier(CustomIdentifierEntity entity) {
    customIdentifiers.remove(entity);
    entity.setDevice(null);
  }

  public void setCustomIdentifiers(Set<CustomIdentifierEntity> customIdentifiers) {
    this.customIdentifiers.clear();
    customIdentifiers.forEach(this::addCustomIdentifier);
  }

  public Set<CustomIdentifierEntity> getCustomIdentifiers() {
    return customIdentifiers;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    DeviceEntity that = (DeviceEntity) o;
    return Objects.equals(id, that.id);
  }

  @Override
  public int hashCode() {
    return getClass().hashCode();
  }
}
