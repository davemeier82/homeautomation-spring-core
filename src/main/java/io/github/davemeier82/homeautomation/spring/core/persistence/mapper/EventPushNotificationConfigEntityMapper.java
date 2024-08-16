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

package io.github.davemeier82.homeautomation.spring.core.persistence.mapper;

import io.github.davemeier82.homeautomation.core.device.DeviceId;
import io.github.davemeier82.homeautomation.core.device.DeviceType;
import io.github.davemeier82.homeautomation.core.device.DeviceTypeMapper;
import io.github.davemeier82.homeautomation.core.device.property.DevicePropertyId;
import io.github.davemeier82.homeautomation.core.device.property.DevicePropertyValueTypeFactory;
import io.github.davemeier82.homeautomation.core.notification.EventPushNotificationConfig;
import io.github.davemeier82.homeautomation.spring.core.persistence.entity.EventPushNotificationConfigEntity;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class EventPushNotificationConfigEntityMapper {
  public static final String SERVICE_ID_SEPARATOR = ";";
  private final DeviceTypeMapper deviceTypeMapper;
  private final DevicePropertyValueTypeFactory devicePropertyValueTypeFactory;

  public EventPushNotificationConfigEntityMapper(DeviceTypeMapper deviceTypeMapper, DevicePropertyValueTypeFactory devicePropertyValueTypeFactory) {
    this.deviceTypeMapper = deviceTypeMapper;
    this.devicePropertyValueTypeFactory = devicePropertyValueTypeFactory;
  }

  public EventPushNotificationConfig map(EventPushNotificationConfigEntity entity) {
    DeviceId deviceId = null;

    if (entity.getDevice() != null) {
      DeviceType deviceType = deviceTypeMapper.map(entity.getDevice().getDeviceType());
      deviceId = new DeviceId(entity.getDevice().getDeviceId(), deviceType);
    }


    DevicePropertyId devicePropertyId = null;
    if (entity.getDeviceProperty() != null) {
      String dpId = entity.getDeviceProperty().getDevicePropertyId();
      if (dpId != null) {
        devicePropertyId = new DevicePropertyId(deviceId, dpId);
      }
    }

    List<String> serviceIds = Optional.ofNullable(entity.getServiceIds()).map(ids -> Arrays.stream(ids.split(SERVICE_ID_SEPARATOR)).toList()).orElse(List.of());

    return new EventPushNotificationConfig(deviceId, devicePropertyId, devicePropertyValueTypeFactory.createDevicePropertyValueType(entity.getPropertyValueType()).orElse(null), serviceIds);
  }
}
