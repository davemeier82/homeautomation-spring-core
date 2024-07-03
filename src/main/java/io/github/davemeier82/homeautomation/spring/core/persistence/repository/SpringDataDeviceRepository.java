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

import io.github.davemeier82.homeautomation.core.device.Device;
import io.github.davemeier82.homeautomation.core.device.DeviceId;
import io.github.davemeier82.homeautomation.core.device.DeviceType;
import io.github.davemeier82.homeautomation.core.device.DeviceTypeMapper;
import io.github.davemeier82.homeautomation.core.repositories.DeviceRepository;
import io.github.davemeier82.homeautomation.spring.core.persistence.entity.DeviceEntity;
import io.github.davemeier82.homeautomation.spring.core.persistence.mapper.DeviceEntityMapper;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.Set;

import static java.util.stream.Collectors.toSet;

@Transactional
public class SpringDataDeviceRepository implements DeviceRepository {

  private final JpaDeviceRepository jpaDeviceRepository;
  private final DeviceEntityMapper deviceEntityMapper;
  private final DeviceTypeMapper deviceTypeMapper;

  public SpringDataDeviceRepository(JpaDeviceRepository jpaDeviceRepository, DeviceEntityMapper deviceEntityMapper, DeviceTypeMapper deviceTypeMapper) {
    this.jpaDeviceRepository = jpaDeviceRepository;
    this.deviceEntityMapper = deviceEntityMapper;
    this.deviceTypeMapper = deviceTypeMapper;
  }

  @Override
  public Optional<Device> getByDeviceId(DeviceId deviceId) {
    String deviceType = deviceTypeMapper.map(deviceId.type());
    return jpaDeviceRepository.findByDeviceIdAndDeviceType(deviceId.id(), deviceType).map(deviceEntityMapper::map);
  }

  @Override
  public Set<Device> getDevices() {
    return jpaDeviceRepository.findAll().stream().map(deviceEntityMapper::map).collect(toSet());
  }

  @Override
  public void save(Device device) {
    String deviceType = deviceTypeMapper.map(device.getType());
    DeviceEntity entity = jpaDeviceRepository.findByDeviceIdAndDeviceType(device.getId(), deviceType)
                                             .map(e -> deviceEntityMapper.update(e, device))
                                             .orElseGet(() -> deviceEntityMapper.map(device));
    jpaDeviceRepository.save(entity);
  }

  @Override
  public void delete(DeviceId deviceId) {
    jpaDeviceRepository.deleteByDeviceIdAndDeviceType(deviceId.id(), deviceId.type().getTypeName());
  }

  @Override
  public <T> Set<? extends T> getDeviceByType(DeviceType deviceType, Class<T> clazz) {
    return jpaDeviceRepository.findAllByDeviceType(deviceType.getTypeName()).stream()
                              .map(deviceEntityMapper::map)
                              .map(clazz::cast)
                              .collect(toSet());
  }
}
