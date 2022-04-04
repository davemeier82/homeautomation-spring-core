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

package io.github.davemeier82.homeautomation.spring.core;

import io.github.davemeier82.homeautomation.core.device.Device;
import io.github.davemeier82.homeautomation.core.device.DeviceId;
import io.github.davemeier82.homeautomation.core.event.EventPublisher;
import io.github.davemeier82.homeautomation.core.event.NewDeviceCreatedEvent;
import io.github.davemeier82.homeautomation.spring.core.event.DeviceRegisteredEvent;
import org.springframework.context.event.EventListener;

import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import static io.github.davemeier82.homeautomation.core.device.DeviceId.deviceIdFromDevice;

/**
 * Registry that contains all devices that got published with a {@link NewDeviceCreatedEvent}.
 *
 * @author David Meier
 * @since 0.1.0
 */
public class DeviceRegistry {

  private final Map<DeviceId, Device> devices = new ConcurrentHashMap<>();
  private final EventPublisher eventPublisher;

  public DeviceRegistry(EventPublisher eventPublisher) {
    this.eventPublisher = eventPublisher;
  }

  /**
   * @return all devices
   */
  public Set<Device> getDevices() {
    return Set.copyOf(devices.values());
  }

  /**
   * Returns the device for an id.
   *
   * @param deviceId the id
   * @return the device
   */
  public Optional<Device> getByDeviceId(DeviceId deviceId) {
    return Optional.ofNullable(devices.get(deviceId));
  }

  /**
   * Adds a device to the registry.
   *
   * @param event the event
   */
  @EventListener
  void onDeviceCreated(NewDeviceCreatedEvent event) {
    DeviceId deviceId = deviceIdFromDevice(event.getDevice());
    if (devices.putIfAbsent(deviceId, event.getDevice()) == null) {
      eventPublisher.publishEvent(new DeviceRegisteredEvent(event.getDevice()));
    }
  }
}
