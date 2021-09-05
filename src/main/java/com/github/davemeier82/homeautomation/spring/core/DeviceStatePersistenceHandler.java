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

import com.github.davemeier82.homeautomation.core.DeviceStateRepository;
import com.github.davemeier82.homeautomation.core.device.Device;
import com.github.davemeier82.homeautomation.core.event.DataWithTimestamp;
import com.github.davemeier82.homeautomation.spring.core.event.*;
import org.springframework.context.event.EventListener;

import static com.github.davemeier82.homeautomation.core.device.DeviceId.deviceIdFromDevice;

public class DeviceStatePersistenceHandler {
  private final DeviceStateRepository deviceStateRepository;

  public DeviceStatePersistenceHandler(DeviceStateRepository deviceStateRepository) {
    this.deviceStateRepository = deviceStateRepository;
  }

  @EventListener
  public void handleEvent(BatteryLevelChangedSpringEvent event) {
    writeIntValue("battery", event.getSensor().getDevice(), event.getBatteryLevelInPercent());
  }

  @EventListener
  public void handleEvent(DimmingLevelChangedSpringEvent event) {
    writeIntValue("dimmer", event.getDimmer().getDevice(), event.getDimmingLevelInPercent());
  }

  @EventListener
  public void handleEvent(HumidityChangedSpringEvent event) {
    writeFloatValue("humidity", event.getSensor().getDevice(), event.getRelativeHumidityInPercent());
  }

  @EventListener
  public void handleEvent(TemperatureChangedSpringEvent event) {
    writeFloatValue("temperature", event.getSensor().getDevice(), event.getTemperatureInDegree());
  }

  @EventListener
  public void handleEvent(IlluminanceChangedSpringEvent event) {
    writeIntValue("illuminance", event.getSensor().getDevice(), event.getLux());
  }

  @EventListener
  public void handleEvent(RelayStateChangedSpringEvent event) {
    writeBooleanValue("relay", event.getRelay().getDevice(), event.isOn());
  }

  @EventListener
  public void handleEvent(WindowStateChangedSpringEvent event) {
    writeBooleanValue("window", event.getWindow().getDevice(), event.isOpen());
  }

  private void writeIntValue(String category, Device device, DataWithTimestamp<Integer> data) {
    deviceStateRepository.insert(deviceIdFromDevice(device),
        category,
        data.getValue(),
        data.getDateTime().toInstant());
  }

  private void writeFloatValue(String category, Device device, DataWithTimestamp<Float> data) {
    deviceStateRepository.insert(deviceIdFromDevice(device),
        category,
        data.getValue(),
        data.getDateTime().toInstant());
  }

  private void writeBooleanValue(String category, Device device, DataWithTimestamp<Boolean> data) {
    deviceStateRepository.insert(deviceIdFromDevice(device),
        category,
        data.getValue(),
        data.getDateTime().toInstant());
  }
}
