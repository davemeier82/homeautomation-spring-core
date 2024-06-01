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

import io.github.davemeier82.homeautomation.core.device.DeviceType;
import io.github.davemeier82.homeautomation.core.device.DeviceTypeMapper;
import io.github.davemeier82.homeautomation.core.device.property.DeviceProperty;
import io.github.davemeier82.homeautomation.core.device.property.DevicePropertyFactory;
import io.github.davemeier82.homeautomation.core.device.property.DevicePropertyType;
import io.github.davemeier82.homeautomation.core.device.property.DevicePropertyTypeMapper;
import io.github.davemeier82.homeautomation.spring.core.persistence.entity.DeviceEntity;
import io.github.davemeier82.homeautomation.spring.core.persistence.entity.DevicePropertyEntity;

public class DevicePropertyEntityMapper {

  private final DevicePropertyFactory devicePropertyFactory;
  private final DevicePropertyTypeMapper devicePropertyTypeMapper;
  private final DeviceTypeMapper deviceTypeMapper;

  public DevicePropertyEntityMapper(DevicePropertyFactory devicePropertyFactory, DevicePropertyTypeMapper devicePropertyTypeMapper, DeviceTypeMapper deviceTypeMapper) {
    this.devicePropertyFactory = devicePropertyFactory;
    this.devicePropertyTypeMapper = devicePropertyTypeMapper;
    this.deviceTypeMapper = deviceTypeMapper;
  }

  public DeviceProperty map(DevicePropertyEntity entity) {
    DevicePropertyType devicePropertyType = devicePropertyTypeMapper.map(entity.getType());
    DeviceEntity deviceEntity = entity.getDevice();
    DeviceType deviceType = deviceTypeMapper.map(deviceEntity.getDeviceType());
    return devicePropertyFactory.createDeviceProperty(entity.getDevicePropertyId(), devicePropertyType, deviceEntity.getDeviceId(), deviceType, entity.getDisplayName());
  }

  public DevicePropertyEntity map(DeviceProperty deviceProperty, DeviceEntity deviceEntity) {
    return new DevicePropertyEntity(deviceEntity, devicePropertyTypeMapper.map(deviceProperty.getType()), deviceProperty.getId().id(), deviceProperty.getDisplayName());
  }

  public DevicePropertyEntity update(DevicePropertyEntity entity, DeviceProperty deviceProperty) {
    entity.setDisplayName(deviceProperty.getDisplayName());
    entity.setType(devicePropertyTypeMapper.map(deviceProperty.getType()));
    return entity;
  }
}
