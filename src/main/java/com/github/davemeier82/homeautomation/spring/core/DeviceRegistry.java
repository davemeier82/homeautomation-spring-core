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

package com.github.davemeier82.homeautomation.spring.core;

import com.github.davemeier82.homeautomation.core.device.Device;
import com.github.davemeier82.homeautomation.core.device.DeviceId;
import com.github.davemeier82.homeautomation.core.event.defaults.DefaultNewDeviceCreatedEvent;
import org.springframework.context.event.EventListener;

import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import static com.github.davemeier82.homeautomation.core.device.DeviceId.deviceIdFromDevice;

public class DeviceRegistry {

  private final Map<DeviceId, Device> devices = new ConcurrentHashMap<>();

  public Set<Device> getDevices() {
    return Set.copyOf(devices.values());
  }

  public Optional<Device> getByDeviceId(DeviceId deviceId) {
    return Optional.ofNullable(devices.get(deviceId));
  }

  @EventListener
  void onDeviceCreated(DefaultNewDeviceCreatedEvent event) {
    DeviceId deviceId = deviceIdFromDevice(event.getDevice());
    devices.putIfAbsent(deviceId, event.getDevice());
  }
}
