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
import io.github.davemeier82.homeautomation.core.device.DeviceFactory;
import io.github.davemeier82.homeautomation.core.event.EventPublisher;
import io.github.davemeier82.homeautomation.core.event.NewDeviceCreatedEvent;
import io.github.davemeier82.homeautomation.core.event.factory.EventFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;

import static java.util.stream.Collectors.toMap;

/**
 * Loads {@link DevicesConfig} form a JSON file and creates devices.
 *
 * @author David Meier
 * @since 0.1.0
 */
public class DeviceLoader {
  private static final Logger log = LoggerFactory.getLogger(DeviceLoader.class);

  private final Path configFilePath;
  private final Map<String, DeviceFactory> deviceTypeToFactory;
  private final EventFactory eventFactory;
  private final ObjectMapper objectMapper;
  private final EventPublisher eventPublisher;

  /**
   * Constructor.
   *
   * @param configFilePath  the file path (incl. filename) of the config file
   * @param deviceFactories factory to create the devices
   * @param objectMapper    the mapper to map the JSON file
   * @param eventFactory    the event factory
   * @param eventPublisher  the event publisher where the created devices are getting published
   */
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

  /**
   * Loads the device config, creates the devices and publishes {@link NewDeviceCreatedEvent} for each created device
   * as well as {@link io.github.davemeier82.homeautomation.core.event.DevicesLoadedEvent} when the loading has been completed.
   *
   * @param event this event triggers the loading
   */
  @EventListener
  public void onApplicationEvent(ApplicationReadyEvent event) {
    List<Device> load = load();
    load.forEach(device -> eventPublisher.publishEvent(eventFactory.createNewDeviceCreatedEvent(device)));
    eventPublisher.publishEvent(eventFactory.createDevicesLoadedEvent(load));
  }

  /**
   * Loads the device config for a file and creates devices.
   *
   * @return the loaded devices
   */
  public List<Device> load() {
    return loadDeviceConfigs().stream().map(this::load).toList();
  }

  /**
   * Creates a device for a device config
   *
   * @return the newly created device
   */
  public Device load(DeviceConfig deviceConfig) {
    DeviceFactory deviceFactory = deviceTypeToFactory.get(deviceConfig.type());
    if (deviceFactory == null) {
      throw new RuntimeException("failed to load device of type " + deviceConfig.type());
    }
    return deviceFactory.createDevice(deviceConfig.type(),
        deviceConfig.id(),
        deviceConfig.displayName(),
        deviceConfig.parameters(),
        deviceConfig.customIdentifiers());
  }

  private List<DeviceConfig> loadDeviceConfigs() {
    if (Files.exists(configFilePath)) {
      try {
        return objectMapper.readValue(configFilePath.toFile(), DevicesConfig.class).devices();
      } catch (IOException e) {
        throw new UncheckedIOException(e);
      }
    } else {
      log.info("config file '{}' does not exits", configFilePath);
    }
    return List.of();
  }

}
