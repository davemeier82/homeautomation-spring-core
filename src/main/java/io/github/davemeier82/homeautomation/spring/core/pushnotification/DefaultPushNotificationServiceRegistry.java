/*
 * Copyright 2021-2021 the original author or authors.
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

package io.github.davemeier82.homeautomation.spring.core.pushnotification;

import io.github.davemeier82.homeautomation.core.PushNotificationService;
import io.github.davemeier82.homeautomation.core.device.DeviceId;
import io.github.davemeier82.homeautomation.core.event.DevicePropertyEvent;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentHashMap.KeySetView;
import java.util.concurrent.ConcurrentSkipListSet;

import static java.util.concurrent.ConcurrentHashMap.newKeySet;
import static java.util.stream.Collectors.toSet;

public class DefaultPushNotificationServiceRegistry implements PushNotificationServiceRegistry {

  private final Map<String, PushNotificationService> idToService = new ConcurrentHashMap<>();
  private final Map<Class<? extends DevicePropertyEvent>, KeySetView<ServiceIdDeviceIdsPair, Boolean>> eventDeviceIdPairToServiceId = new ConcurrentHashMap<>();
  private final Map<Class<?>, Set<String>> applicationEventToServiceId = new ConcurrentHashMap<>();

  @Override
  public Set<PushNotificationService> getAll() {
    return new HashSet<>(idToService.values());
  }

  @Override
  public Optional<PushNotificationService> getById(String id) {
    return Optional.ofNullable(idToService.get(id));
  }

  @Override
  public Set<PushNotificationService> getBy(Class<? extends DevicePropertyEvent> event, DeviceId deviceId) {
    return eventDeviceIdPairToServiceId.entrySet().stream()
        .filter(entry -> entry.getKey().isAssignableFrom(event))
        .flatMap(entry -> entry.getValue().stream())
        .filter(pair -> pair.deviceIds().isEmpty() || pair.deviceIds().contains(deviceId))
        .map(ServiceIdDeviceIdsPair::serviceId)
        .map(idToService::get)
        .filter(Objects::nonNull)
        .collect(toSet());
  }

  @Override
  public Set<PushNotificationService> getBy(Class<?> event) {
    return applicationEventToServiceId.entrySet().stream()
        .filter(entry -> entry.getKey().isAssignableFrom(event))
        .flatMap(entry -> entry.getValue().stream())
        .map(idToService::get)
        .filter(Objects::nonNull)
        .collect(toSet());
  }

  @Override
  public void registerToEvent(Class<? extends DevicePropertyEvent> event, String serviceId, List<DeviceId> deviceIds) {
    if (idToService.containsKey(serviceId)) {
      KeySetView<ServiceIdDeviceIdsPair, Boolean> set = newKeySet();
      set.add(new ServiceIdDeviceIdsPair(serviceId, deviceIds));
      Set<ServiceIdDeviceIdsPair> previous = eventDeviceIdPairToServiceId.putIfAbsent(event, set);
      if (previous != null) {
        previous.add(new ServiceIdDeviceIdsPair(serviceId, deviceIds));
      }
    } else {
      throw new IllegalArgumentException("PushNotificationService with id '" + serviceId + "' not found");
    }
  }

  @Override
  public void registerToEvent(Class<?> event, String serviceId) {
    if (idToService.containsKey(serviceId)) {
      Set<String> previous = applicationEventToServiceId.putIfAbsent(event, new ConcurrentSkipListSet<>(Set.of(serviceId)));
      if (previous != null) {
        previous.add(serviceId);
      }
    } else {
      throw new IllegalArgumentException("PushNotificationService with id '" + serviceId + "' not found");
    }
  }

  @Override
  public void add(String id, PushNotificationService pushNotificationService) {
    idToService.put(id, pushNotificationService);
  }

  private static record ServiceIdDeviceIdsPair(String serviceId, List<DeviceId> deviceIds) {
  }

}
