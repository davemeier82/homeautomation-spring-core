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

import io.github.davemeier82.homeautomation.core.DeviceStateRepository;
import io.github.davemeier82.homeautomation.core.device.Device;
import io.github.davemeier82.homeautomation.core.event.DataWithTimestamp;
import io.github.davemeier82.homeautomation.core.event.defaults.*;
import org.springframework.context.event.EventListener;

import static io.github.davemeier82.homeautomation.core.device.DeviceId.deviceIdFromDevice;

public class DeviceStatePersistenceHandler {
  private final DeviceStateRepository deviceStateRepository;

  public DeviceStatePersistenceHandler(DeviceStateRepository deviceStateRepository) {
    this.deviceStateRepository = deviceStateRepository;
  }

  @EventListener
  public void handleEvent(DefaultBatteryLevelChangedPropertyEvent event) {
    writeIntValue("battery", event.getDeviceProperty().getDevice(), event.getBatteryLevelInPercent());
  }

  @EventListener
  public void handleEvent(DefaultDimmingLevelChangedPropertyEvent event) {
    writeIntValue("dimmer", event.getDeviceProperty().getDevice(), event.getDimmingLevelInPercent());
  }

  @EventListener
  public void handleEvent(DefaultHumidityChangedPropertyEvent event) {
    writeFloatValue("humidity", event.getDeviceProperty().getDevice(), event.getRelativeHumidityInPercent());
  }

  @EventListener
  public void handleEvent(DefaultTemperatureChangedPropertyEvent event) {
    writeFloatValue("temperature", event.getDeviceProperty().getDevice(), event.getTemperatureInDegree());
  }

  @EventListener
  public void handleEvent(DefaultPowerChangedPropertyEvent event) {
    writeDoubleValue("power", event.getDeviceProperty().getDevice(), event.getWatt());
  }

  @EventListener
  public void handleEvent(DefaultIlluminanceChangedPropertyEvent event) {
    writeIntValue("illuminance", event.getDeviceProperty().getDevice(), event.getLux());
  }

  @EventListener
  public void handleEvent(DefaultRelayStateChangedPropertyEvent event) {
    writeBooleanValue("relay", event.getDeviceProperty().getDevice(), event.isOn());
  }

  @EventListener
  public void handleEvent(DefaultWindowStateChangedPropertyEvent event) {
    writeBooleanValue("window", event.getDevice(), event.isOpen());
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

  private void writeDoubleValue(String category, Device device, DataWithTimestamp<Double> data) {
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
