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

import io.github.davemeier82.homeautomation.core.device.Device;
import io.github.davemeier82.homeautomation.core.device.DeviceFactory;
import io.github.davemeier82.homeautomation.core.device.DeviceType;
import io.github.davemeier82.homeautomation.core.device.DeviceTypeMapper;
import io.github.davemeier82.homeautomation.spring.core.persistence.entity.CustomIdentifierEntity;
import io.github.davemeier82.homeautomation.spring.core.persistence.entity.CustomIdentifierId;
import io.github.davemeier82.homeautomation.spring.core.persistence.entity.DeviceEntity;
import io.github.davemeier82.homeautomation.spring.core.persistence.entity.DeviceParameterEntity;
import io.github.davemeier82.homeautomation.spring.core.persistence.entity.DeviceParameterId;

import java.util.Map;
import java.util.Set;
import java.util.function.Function;

import static java.util.stream.Collectors.toMap;
import static org.springframework.util.CollectionUtils.isEmpty;

public class DeviceEntityMapper {

  private final Map<DeviceType, DeviceFactory> deviceTypeToFactory;
  private final DeviceTypeMapper deviceTypeMapper;

  public DeviceEntityMapper(Set<DeviceFactory> deviceFactories, DeviceTypeMapper deviceTypeMapper) {
    deviceTypeToFactory = deviceFactories.stream()
                                         .flatMap(deviceFactory -> deviceFactory.getSupportedDeviceTypes().stream().collect(toMap(Function.identity(),
                                             (type) -> deviceFactory)).entrySet().stream())
                                         .collect(toMap(Map.Entry::getKey, Map.Entry::getValue));
    this.deviceTypeMapper = deviceTypeMapper;
  }

  private static void mapParametersAndIdentifiers(DeviceEntity entity, Device device) {
    if (!isEmpty(device.getParameters())) {
      device.getParameters().forEach((key, value) -> entity.addDeviceParameter(new DeviceParameterEntity(new DeviceParameterId(null, key), value)));
    }
    if (!isEmpty(device.getCustomIdentifiers())) {
      device.getCustomIdentifiers().forEach((key, value) -> entity.addCustomIdentifier(new CustomIdentifierEntity(new CustomIdentifierId(null, key), value)));
    }
  }

  public Device map(DeviceEntity entity) {
    DeviceType deviceType = deviceTypeMapper.map(entity.getDeviceType());
    Map<String, String> deviceParameters = entity.getDeviceParameters().stream().collect(toMap(p -> p.getId().getName(), DeviceParameterEntity::getValue));
    Map<String, String> customIdentifier = entity.getCustomIdentifiers().stream().collect(toMap(p -> p.getId().getName(), CustomIdentifierEntity::getValue));
    return deviceTypeToFactory.get(deviceType).createDevice(deviceType, entity.getDeviceId(), entity.getDisplayName(), deviceParameters, customIdentifier).orElseThrow();
  }

  public DeviceEntity map(Device device) {
    String deviceType = deviceTypeMapper.map(device.getType());
    DeviceEntity entity = new DeviceEntity(deviceType, device.getId(), device.getDisplayName());
    mapParametersAndIdentifiers(entity, device);
    return entity;
  }

  public DeviceEntity update(DeviceEntity entity, Device device) {
    entity.setDisplayName(device.getDisplayName());

    entity.clearDeviceParameters();
    entity.clearCustomIdentifiers();
    mapParametersAndIdentifiers(entity, device);

    return entity;
  }

}