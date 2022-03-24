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

package io.github.davemeier82.homeautomation.spring.core.config.device;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.davemeier82.homeautomation.core.device.Device;
import io.github.davemeier82.homeautomation.core.event.defaults.DefaultDevicesLoadedEvent;
import io.github.davemeier82.homeautomation.core.event.defaults.DefaultNewDeviceCreatedEvent;
import io.github.davemeier82.homeautomation.spring.core.DeviceRegistry;
import org.springframework.context.event.EventListener;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.concurrent.locks.ReentrantLock;

import static java.nio.file.Files.createFile;
import static java.nio.file.Files.notExists;
import static java.nio.file.StandardOpenOption.TRUNCATE_EXISTING;
import static java.nio.file.StandardOpenOption.WRITE;

/**
 * Saves {@link DevicesConfig} into a JSON file when new devices are created.
 *
 * @author David Meier
 * @since 0.1.0
 */
public class DeviceConfigWriter {

  private final DeviceRegistry deviceRegistry;
  private final ReentrantLock saveLock = new ReentrantLock();
  private final ObjectMapper objectMapper;
  private final Path configFilePath;
  private boolean enabled = false;

  /**
   * Constructor.
   *
   * @param deviceRegistry the device registry
   * @param objectMapper   the object mapper used to map the config to a json
   * @param configFilePath the file path (incl. filename) where the config should get saved
   */
  public DeviceConfigWriter(DeviceRegistry deviceRegistry, ObjectMapper objectMapper, Path configFilePath) {
    this.deviceRegistry = deviceRegistry;
    this.objectMapper = objectMapper;
    this.configFilePath = configFilePath;
  }

  /**
   * Saved the device config when a new device is created and the initial loading of the devices has completed {@link DefaultDevicesLoadedEvent}
   *
   * @param event event that signals that a new device has been created
   */
  @EventListener
  void onDeviceCreated(DefaultNewDeviceCreatedEvent event) {
    if (enabled) {
      save();
    }
  }

  /**
   * Enables the device config saving (but does not trigger a save)
   *
   * @param event event that signals that the initial loading of the devices has been completed
   */
  @EventListener
  void onDevicesLoaded(DefaultDevicesLoadedEvent event) {
    enabled = true;
  }

  private void save() {
    if (saveLock.hasQueuedThreads()) {
      return;
    } else {
      saveLock.lock();
    }
    try {
      if (notExists(configFilePath)) {
        createFile(configFilePath);
      }
      List<DeviceConfig> deviceConfigs = deviceRegistry.getDevices().stream()
          .map(this::toConfig)
          .toList();
      Files.writeString(configFilePath,
          objectMapper.writerWithDefaultPrettyPrinter()
              .writeValueAsString(new DevicesConfig(DevicesConfig.CURRENT_VERSION, deviceConfigs)),
          TRUNCATE_EXISTING,
          WRITE);
    } catch (IOException e) {
      throw new UncheckedIOException(e);
    } finally {
      saveLock.unlock();
    }
  }

  private DeviceConfig toConfig(Device device) {
    return new DeviceConfig(device.getType(), device.getDisplayName(), device.getId(), device.getParameters(), device.getCustomIdentifiers());
  }

}