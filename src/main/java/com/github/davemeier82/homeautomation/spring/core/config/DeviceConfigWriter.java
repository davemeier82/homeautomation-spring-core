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

package com.github.davemeier82.homeautomation.spring.core.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.davemeier82.homeautomation.core.device.Device;
import com.github.davemeier82.homeautomation.spring.core.DeviceRegistry;
import com.github.davemeier82.homeautomation.spring.core.event.DevicesLoadedSpringEvent;
import com.github.davemeier82.homeautomation.spring.core.event.NewDeviceCreatedSpringEvent;
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
import static java.util.stream.Collectors.toList;

public class DeviceConfigWriter {

  private final DeviceRegistry deviceRegistry;
  private final ReentrantLock saveLock = new ReentrantLock();
  private final ObjectMapper objectMapper;
  private final Path configFilePath;
  private boolean enabled = false;

  public DeviceConfigWriter(DeviceRegistry deviceRegistry, ObjectMapper objectMapper, Path configFilePath) {
    this.deviceRegistry = deviceRegistry;
    this.objectMapper = objectMapper;
    this.configFilePath = configFilePath;
  }

  @EventListener
  void onDeviceCreated(NewDeviceCreatedSpringEvent event) {
    if (enabled) {
      save();
    }
  }

  @EventListener
  void onDevicesLoaded(DevicesLoadedSpringEvent event) {
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
          .collect(toList());
      Files.writeString(configFilePath,
          objectMapper.writeValueAsString(new DevicesConfig(DevicesConfig.CURRENT_VERSION, deviceConfigs)),
          TRUNCATE_EXISTING,
          WRITE);
    } catch (IOException e) {
      throw new UncheckedIOException(e);
    } finally {
      saveLock.unlock();
    }
  }

  private DeviceConfig toConfig(Device device) {
    return new DeviceConfig(device.getType(), device.getClass().getSimpleName(), device.getId());
  }

}