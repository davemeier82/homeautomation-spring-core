/*
 * Copyright 2021-2022 the original author or authors.
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

package io.github.davemeier82.homeautomation.spring.core.config.device;

import io.github.davemeier82.homeautomation.core.device.Device;
import io.github.davemeier82.homeautomation.core.device.DeviceId;
import io.github.davemeier82.homeautomation.spring.core.DeviceRegistry;

import java.util.List;
import java.util.Optional;

/**
 * Factory that create device configs from the {@link DeviceRegistry}
 *
 * @author David Meier
 * @since 0.1.3
 */
public class DeviceConfigFactory {

  private final DeviceRegistry deviceRegistry;

  /**
   * Constructor.
   *
   * @param deviceRegistry the device registry
   */
  public DeviceConfigFactory(DeviceRegistry deviceRegistry) {
    this.deviceRegistry = deviceRegistry;
  }

  /**
   * @return the new instance of a config from the current state of the device registry
   */
  public DevicesConfig createDevicesConfig() {
    List<DeviceConfig> deviceConfigs = deviceRegistry.getDevices().stream()
        .map(this::toConfig)
        .toList();
    return new DevicesConfig(DevicesConfig.CURRENT_VERSION, deviceConfigs);
  }

  /**
   * The config of a specific device.
   *
   * @param deviceId the device id
   * @return the config
   */
  public Optional<DeviceConfig> createDeviceConfig(DeviceId deviceId) {
    return deviceRegistry.getByDeviceId(deviceId).map(this::toConfig);
  }

  private DeviceConfig toConfig(Device device) {
    return new DeviceConfig(device.getType(), device.getDisplayName(), device.getId(), device.getParameters(), device.getCustomIdentifiers());
  }

}
