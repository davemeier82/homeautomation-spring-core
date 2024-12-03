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
import io.github.davemeier82.homeautomation.core.event.DevicePropertyEvent;
import io.github.davemeier82.homeautomation.core.event.DevicePropertyUpdatedEvent;
import io.github.davemeier82.homeautomation.core.notification.EventPushNotificationConfig;
import io.github.davemeier82.homeautomation.core.notification.EventPushNotificationConfigRepository;
import io.github.davemeier82.homeautomation.spring.core.persistence.mapper.EventPushNotificationConfigEntityMapper;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

import static java.util.stream.Collectors.toSet;


@Transactional(readOnly = true)
public class SpringDataEventPushNotificationConfigRepository implements EventPushNotificationConfigRepository {

  private final JpaEventPushNotificationConfigRepository repository;
  private final JpaDevicePropertyRepository jpaDevicePropertyRepository;
  private final EventPushNotificationConfigEntityMapper mapper;
  private final DeviceTypeMapper deviceTypeMapper;

  public SpringDataEventPushNotificationConfigRepository(JpaEventPushNotificationConfigRepository eventPushNotificationConfigRepository,
                                                         JpaDevicePropertyRepository jpaDevicePropertyRepository, EventPushNotificationConfigEntityMapper mapper,
                                                         DeviceTypeMapper deviceTypeMapper
  ) {
    repository = eventPushNotificationConfigRepository;
    this.jpaDevicePropertyRepository = jpaDevicePropertyRepository;
    this.mapper = mapper;
    this.deviceTypeMapper = deviceTypeMapper;
  }


  @Override
  public Set<EventPushNotificationConfig> findAllByEvent(DevicePropertyEvent<?> event) {

    DeviceId deviceId = event.getDevicePropertyId().deviceId();
    String deviceType = deviceTypeMapper.map(deviceId.type());
    return jpaDevicePropertyRepository.findByDevicePropertyIdAndDevice_DeviceIdAndDevice_DeviceType(event.getDevicePropertyId().id(), deviceId.id(), deviceType)
                                      .map(entity -> {
                                        boolean isBooleanEvent = Boolean.class.equals(event.getValueType().getClazz());
                                        boolean isUpdateEvent = event instanceof DevicePropertyUpdatedEvent;
                                        if (isUpdateEvent) {
                                          return repository.findAllBy(
                                              entity.getDevice().getId(),
                                              entity.getId(),
                                              event.getValueType().getTypeName(),
                                              false
                                          ).stream().map(mapper::map).collect(toSet());
                                        } else {
                                          return repository.findAllBy(
                                                               entity.getDevice().getId(),
                                                               entity.getId(),
                                                               event.getValueType().getTypeName()
                                                           ).stream().filter(s -> {
                                                             if (s.getBooleanValueFilter() == null || !isBooleanEvent || event.getNewValue() == null) {
                                                               return true;
                                                             }
                                                             return s.getBooleanValueFilter().equals(event.getNewValue());
                                                           }).map(mapper::map)
                                                           .collect(toSet());
                                        }
                                      }).orElse(Set.of());
  }
}
