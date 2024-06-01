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

package io.github.davemeier82.homeautomation.spring.core.persistence.repository;

import io.github.davemeier82.homeautomation.core.device.DeviceId;
import io.github.davemeier82.homeautomation.core.device.DeviceTypeMapper;
import io.github.davemeier82.homeautomation.core.device.property.DeviceProperty;
import io.github.davemeier82.homeautomation.core.device.property.DevicePropertyId;
import io.github.davemeier82.homeautomation.core.device.property.DevicePropertyType;
import io.github.davemeier82.homeautomation.core.repositories.DevicePropertyRepository;
import io.github.davemeier82.homeautomation.spring.core.persistence.entity.DeviceEntity;
import io.github.davemeier82.homeautomation.spring.core.persistence.entity.DevicePropertyEntity;
import io.github.davemeier82.homeautomation.spring.core.persistence.mapper.DevicePropertyEntityMapper;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import static java.util.stream.Collectors.toSet;

public class SpringDataDevicePropertyRepository implements DevicePropertyRepository {

  private final JpaDevicePropertyRepository jpaDevicePropertyRepository;
  private final JpaDeviceRepository jpaDeviceRepository;
  private final DevicePropertyEntityMapper devicePropertyEntityMapper;
  private final DeviceTypeMapper deviceTypeMapper;

  public SpringDataDevicePropertyRepository(JpaDevicePropertyRepository jpaDevicePropertyRepository,
                                            JpaDeviceRepository jpaDeviceRepository,
                                            DevicePropertyEntityMapper devicePropertyEntityMapper,
                                            DeviceTypeMapper deviceTypeMapper
  ) {
    this.jpaDevicePropertyRepository = jpaDevicePropertyRepository;
    this.jpaDeviceRepository = jpaDeviceRepository;
    this.devicePropertyEntityMapper = devicePropertyEntityMapper;
    this.deviceTypeMapper = deviceTypeMapper;
  }

  @Override
  public Optional<DeviceProperty> findByDevicePropertyId(DevicePropertyId devicePropertyId) {
    DeviceId deviceId = devicePropertyId.deviceId();
    String deviceType = deviceTypeMapper.map(deviceId.type());
    return jpaDevicePropertyRepository.findByDevicePropertyIdAndDevice_DeviceIdAndDevice_DeviceType(devicePropertyId.id(), deviceId.id(), deviceType).map(devicePropertyEntityMapper::map);
  }

  @Override
  public List<DeviceProperty> findByDeviceId(DeviceId deviceId) {
    String deviceType = deviceTypeMapper.map(deviceId.type());
    return jpaDeviceRepository.findByDeviceIdAndDeviceType(deviceId.id(), deviceType)
                              .map(entity -> jpaDevicePropertyRepository.findAllByDeviceId(entity.getId()).stream().map(devicePropertyEntityMapper::map).toList())
                              .orElseGet(List::of);


  }

  @Override
  public Set<DeviceProperty> findByType(DevicePropertyType type) {
    return jpaDevicePropertyRepository.findAllByType(type.getTypeName()).stream().map(devicePropertyEntityMapper::map).collect(toSet());
  }

  @Override
  public void save(DeviceProperty deviceProperty) {
    DeviceId deviceId = deviceProperty.getId().deviceId();
    String deviceType = deviceTypeMapper.map(deviceId.type());
    DeviceEntity deviceEntity = jpaDeviceRepository.findByDeviceIdAndDeviceType(deviceId.id(), deviceType).orElseThrow();
    Optional<DevicePropertyEntity> devicePropertyEntity = jpaDevicePropertyRepository.findByDevicePropertyIdAndDeviceId(deviceProperty.getId().id(), deviceEntity.getId());
    if (devicePropertyEntity.isPresent()) {
      devicePropertyEntityMapper.update(devicePropertyEntity.get(), deviceProperty);
    } else {
      jpaDevicePropertyRepository.save(devicePropertyEntityMapper.map(deviceProperty, deviceEntity));
    }
  }

  @Override
  public void delete(DevicePropertyId devicePropertyId) {
    DeviceId deviceId = devicePropertyId.deviceId();
    String deviceType = deviceTypeMapper.map(deviceId.type());
    DeviceEntity deviceEntity = jpaDeviceRepository.findByDeviceIdAndDeviceType(deviceId.id(), deviceType).orElseThrow();
    jpaDevicePropertyRepository.deleteByDevicePropertyIdAndDevice(devicePropertyId.id(), deviceEntity);
  }

}
