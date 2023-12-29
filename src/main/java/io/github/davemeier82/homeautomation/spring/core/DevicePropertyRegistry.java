/*
 * Copyright 2021-2023 the original author or authors.
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
import io.github.davemeier82.homeautomation.core.device.property.DeviceProperty;
import io.github.davemeier82.homeautomation.core.device.property.DevicePropertyId;
import io.github.davemeier82.homeautomation.core.repositories.DevicePropertyRepository;
import io.github.davemeier82.homeautomation.core.repositories.DeviceRepository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import static java.util.stream.Collectors.toSet;

public class DevicePropertyRegistry implements DevicePropertyRepository {

  private final DeviceRepository deviceRepository;

  public DevicePropertyRegistry(DeviceRepository deviceRepository) {
    this.deviceRepository = deviceRepository;
  }

  @Override
  public Optional<DeviceProperty> getByDevicePropertyId(DevicePropertyId devicePropertyId) {
    List<DeviceProperty> deviceProperties = getByDeviceId(devicePropertyId.deviceId());
    if (deviceProperties.size() > devicePropertyId.id()) {
      return Optional.ofNullable(deviceProperties.get(devicePropertyId.id()));
    }
    return Optional.empty();
  }

  @Override
  public List<DeviceProperty> getByDeviceId(DeviceId deviceId) {
    //noinspection unchecked
    return deviceRepository.getByDeviceId(deviceId)
        .map(Device::getDeviceProperties)
        .map(dp -> (List<DeviceProperty>) dp)
        .orElse(List.of());
  }

  @Override
  public Set<DeviceProperty> getByType(String type) {
    return deviceRepository.getDevices().stream().flatMap(device -> device.getDeviceProperties().stream())
        .filter(deviceProperty -> deviceProperty.getType().equals(type))
        .collect(toSet());
  }
}
