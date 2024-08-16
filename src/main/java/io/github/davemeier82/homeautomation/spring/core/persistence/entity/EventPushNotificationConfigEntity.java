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
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;

import java.util.Objects;
import java.util.UUID;

@Entity(name = "event_push_notification_config")
public class EventPushNotificationConfigEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  private UUID id;
  @OneToOne(fetch = FetchType.EAGER)
  @JoinColumn(name = "device_id")
  private DeviceEntity device;
  @OneToOne(fetch = FetchType.EAGER)
  @JoinColumn(name = "device_property_id")
  private DevicePropertyEntity deviceProperty;
  private String propertyValueType;
  private boolean onChangeOnly;
  private String serviceIds;
  private Boolean booleanValueFilter;

  public EventPushNotificationConfigEntity() {
  }

  public EventPushNotificationConfigEntity(DeviceEntity device, DevicePropertyEntity deviceProperty, String propertyValueType, boolean onChangeOnly, String serviceIds, Boolean booleanValueFilter) {
    this.device = device;
    this.deviceProperty = deviceProperty;
    this.propertyValueType = propertyValueType;
    this.onChangeOnly = onChangeOnly;
    this.serviceIds = serviceIds;
    this.booleanValueFilter = booleanValueFilter;
  }

  public UUID getId() {
    return id;
  }

  public DeviceEntity getDevice() {
    return device;
  }

  public void setDevice(DeviceEntity device) {
    this.device = device;
  }

  public DevicePropertyEntity getDeviceProperty() {
    return deviceProperty;
  }

  public void setDeviceProperty(DevicePropertyEntity deviceProperty) {
    this.deviceProperty = deviceProperty;
  }

  public String getPropertyValueType() {
    return propertyValueType;
  }

  public void setPropertyValueType(String propertyValueType) {
    this.propertyValueType = propertyValueType;
  }

  public boolean isOnChangeOnly() {
    return onChangeOnly;
  }

  public void setOnChangeOnly(boolean onChangeOnly) {
    this.onChangeOnly = onChangeOnly;
  }

  public String getServiceIds() {
    return serviceIds;
  }

  public void setServiceIds(String serviceIds) {
    this.serviceIds = serviceIds;
  }

  public Boolean getBooleanValueFilter() {
    return booleanValueFilter;
  }

  public void setBooleanValueFilter(Boolean booleanValueFilter) {
    this.booleanValueFilter = booleanValueFilter;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    EventPushNotificationConfigEntity that = (EventPushNotificationConfigEntity) o;
    return Objects.equals(id, that.id);
  }

  @Override
  public int hashCode() {
    return getClass().hashCode();
  }
}
