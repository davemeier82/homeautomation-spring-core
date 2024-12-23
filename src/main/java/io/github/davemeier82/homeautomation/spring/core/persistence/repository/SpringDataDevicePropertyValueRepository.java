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

import io.github.davemeier82.homeautomation.core.device.DeviceTypeMapper;
import io.github.davemeier82.homeautomation.core.device.property.DevicePropertyId;
import io.github.davemeier82.homeautomation.core.device.property.DevicePropertyValueType;
import io.github.davemeier82.homeautomation.core.event.DataWithTimestamp;
import io.github.davemeier82.homeautomation.core.repositories.DevicePropertyValueRepository;
import io.github.davemeier82.homeautomation.spring.core.persistence.entity.DevicePropertyValueEntity;
import io.github.davemeier82.homeautomation.spring.core.persistence.mapper.DevicePropertyValueEntityMapper;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.OffsetDateTime;
import java.util.Optional;

@Transactional
public class SpringDataDevicePropertyValueRepository implements DevicePropertyValueRepository {

  private static final Logger log = LoggerFactory.getLogger(SpringDataDevicePropertyValueRepository.class);
  private final JpaDevicePropertyValueRepository devicePropertyValueRepository;
  private final JpaDevicePropertyRepository devicePropertyRepository;
  private final DeviceTypeMapper deviceTypeMapper;
  private final DevicePropertyValueEntityMapper devicePropertyValueEntityMapper;

  public SpringDataDevicePropertyValueRepository(JpaDevicePropertyValueRepository devicePropertyValueRepository,
                                                 JpaDevicePropertyRepository devicePropertyRepository,
                                                 DeviceTypeMapper deviceTypeMapper,
                                                 DevicePropertyValueEntityMapper devicePropertyValueEntityMapper
  ) {
    this.devicePropertyValueRepository = devicePropertyValueRepository;
    this.devicePropertyRepository = devicePropertyRepository;
    this.deviceTypeMapper = deviceTypeMapper;
    this.devicePropertyValueEntityMapper = devicePropertyValueEntityMapper;
  }

  @Override
  public void insert(DevicePropertyId devicePropertyId, DevicePropertyValueType devicePropertyValueType, String displayName, Object value, OffsetDateTime time) {
    String deviceType = deviceTypeMapper.map(devicePropertyId.deviceId().type());
    devicePropertyRepository.findByDevicePropertyIdAndDevice_DeviceIdAndDevice_DeviceType(devicePropertyId.id(), devicePropertyId.deviceId().id(),
                                                                   deviceType)
                            .map(e -> {
                              DevicePropertyValueEntity dpve = devicePropertyValueEntityMapper.map(e.getId(), devicePropertyValueType, value, time);
                              if (devicePropertyValueRepository.findByDevicePropertyIdAndTypeAndTimestamp(e.getId(), dpve.getType(), time).isEmpty()) {
                                return dpve;
                              }
                              log.info("value {} for {} at {} already saved", value, devicePropertyId, time);
                              return null;
                            }).ifPresentOrElse(devicePropertyValueRepository::save, () -> log.info("value {} for {} at {}  saved", value, devicePropertyId, time));
  }

  @Override
  @org.springframework.transaction.annotation.Transactional(readOnly = true)
  public <T> Optional<DataWithTimestamp<T>> findLatestValue(DevicePropertyId devicePropertyId, DevicePropertyValueType devicePropertyValueType, Class<T> clazz) {
    String deviceType = deviceTypeMapper.map(devicePropertyId.deviceId().type());
    return devicePropertyRepository.findByDevicePropertyIdAndDevice_DeviceIdAndDevice_DeviceType(devicePropertyId.id(), devicePropertyId.deviceId().id(), deviceType)
                                   .flatMap(entity -> devicePropertyValueRepository.findTopByDevicePropertyIdAndTypeOrderByTimestampDesc(entity.getId(), devicePropertyValueType.getTypeName()))
                                   .flatMap(dp -> devicePropertyValueEntityMapper.map(dp, clazz));
  }

  @Override
  @org.springframework.transaction.annotation.Transactional(readOnly = true)
  public Optional<OffsetDateTime> lastTimeValueMatched(DevicePropertyId devicePropertyId, DevicePropertyValueType devicePropertyValueType, Object value) {
    String deviceType = deviceTypeMapper.map(devicePropertyId.deviceId().type());
    return devicePropertyRepository.findByDevicePropertyIdAndDevice_DeviceIdAndDevice_DeviceType(devicePropertyId.id(), devicePropertyId.deviceId().id(), deviceType)
                                   .flatMap(entity -> devicePropertyValueRepository.findTopByDevicePropertyIdAndTypeAndValueOrderByTimestampDesc(entity.getId(), devicePropertyValueType.getTypeName(),
                                       DevicePropertyValueEntityMapper.valueToString(value)))
                                   .map(DevicePropertyValueEntity::getTimestamp);
  }

  public void deleteAllBefore(OffsetDateTime timestamp) {
    devicePropertyValueRepository.deleteAllByTimestampBefore(timestamp);
  }


}
