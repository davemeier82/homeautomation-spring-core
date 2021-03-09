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
import com.github.davemeier82.homeautomation.core.device.DeviceFactory;
import com.github.davemeier82.homeautomation.core.event.EventFactory;
import com.github.davemeier82.homeautomation.core.event.EventPublisher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.core.io.Resource;
import org.springframework.util.FileCopyUtils;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;

import static java.nio.charset.StandardCharsets.UTF_8;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toMap;

public class DeviceLoader {
  private static final Logger log = LoggerFactory.getLogger(DeviceLoader.class);

  private final Path configFilePath;
  private final Map<String, DeviceFactory> deviceTypeToFactory;
  private final EventFactory eventFactory;
  private final ObjectMapper objectMapper;
  private final EventPublisher eventPublisher;

  public DeviceLoader(Path configFilePath,
                      Set<DeviceFactory> deviceFactories,
                      ObjectMapper objectMapper,
                      EventFactory eventFactory,
                      EventPublisher eventPublisher
  ) {
    this.configFilePath = configFilePath;
    this.objectMapper = objectMapper;
    deviceTypeToFactory = deviceFactories.stream()
        .flatMap(deviceFactory -> deviceFactory.getSupportedDeviceTypes().stream().collect(toMap(Function.identity(),
            (type) -> deviceFactory)).entrySet().stream())
        .collect(toMap(Map.Entry::getKey, Map.Entry::getValue));
    this.eventFactory = eventFactory;
    this.eventPublisher = eventPublisher;
  }

  @EventListener
  public void onApplicationEvent(ApplicationReadyEvent event) {
    load();
  }

  public List<Device> load() {
    List<Device> devices = loadDeviceConfigs().stream().map(deviceConfig -> {
      DeviceFactory deviceFactory = deviceTypeToFactory.get(deviceConfig.getType());
      if (deviceFactory == null) {
        throw new RuntimeException("failed to load device of type " + deviceConfig.getType());
      }
      return deviceFactory.createDevice(deviceConfig.getType(), deviceConfig.getId(), deviceConfig.getDisplayName());
    }).collect(toList());
    eventPublisher.publishEvent(eventFactory.createDevicesLoadedEvent(this));
    return devices;
  }

  private List<DeviceConfig> loadDeviceConfigs() {
    if (Files.exists(configFilePath)) {
      try {
        return objectMapper.readValue(configFilePath.toFile(), DevicesConfig.class).getDevices();
      } catch (IOException e) {
        throw new UncheckedIOException(e);
      }
    } else {
      log.info("config file '{}' does not exits", configFilePath);
    }
    return List.of();
  }

  public static String asString(Resource resource) {
    try (Reader reader = new InputStreamReader(resource.getInputStream(), UTF_8)) {
      return FileCopyUtils.copyToString(reader);
    } catch (IOException e) {
      throw new UncheckedIOException(e);
    }
  }
}
