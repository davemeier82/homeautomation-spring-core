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

/**
 * This class listens to events and stores the values in the {@link DeviceStateRepository}.
 *
 * @author David Meier
 * @since 0.1.0
 */
public class DeviceStatePersistenceHandler {
  private final DeviceStateRepository deviceStateRepository;

  /**
   * Constructor.
   *
   * @param deviceStateRepository the repository
   */
  public DeviceStatePersistenceHandler(DeviceStateRepository deviceStateRepository) {
    this.deviceStateRepository = deviceStateRepository;
  }

  @EventListener
  public void handleEvent(DefaultBatteryLevelUpdatedPropertyEvent event) {
    writeIntValue("battery", event.getDevice(), event.getBatteryLevelInPercent());
  }

  @EventListener
  public void handleEvent(DefaultDimmingLevelUpdatedPropertyEvent event) {
    writeIntValue("dimmer", event.getDevice(), event.getDimmingLevelInPercent());
  }

  @EventListener
  public void handleEvent(DefaultHumidityUpdatedPropertyEvent event) {
    writeFloatValue("humidity", event.getDevice(), event.getRelativeHumidityInPercent());
  }

  @EventListener
  public void handleEvent(DefaultTemperatureUpdatedPropertyEvent event) {
    writeFloatValue("temperature", event.getDevice(), event.getTemperatureInDegree());
  }

  @EventListener
  public void handleEvent(DefaultPowerUpdatedPropertyEvent event) {
    writeDoubleValue("power", event.getDevice(), event.getWatt());
  }

  @EventListener
  public void handleEvent(DefaultIlluminanceUpdatedPropertyEvent event) {
    writeIntValue("illuminance", event.getDevice(), event.getLux());
  }

  @EventListener
  public void handleEvent(DefaultRelayStateUpdatedPropertyEvent event) {
    writeBooleanValue("relay", event.getDevice(), event.isOn());
  }

  @EventListener
  public void handleEvent(DefaultMotionUpdatedPropertyEvent event) {
    writeBooleanValue("motion", event.getDevice(), event.motionDetected());
  }

  @EventListener
  public void handleEvent(DefaultWindowStateUpdatedPropertyEvent event) {
    writeBooleanValue("window", event.getDevice(), event.isOpen());
  }

  @EventListener
  public void handleEvent(DefaultICo2LevelUpdatedPropertyEvent event) {
    writeIntValue("co2", event.getDevice(), event.getPpm());
  }

  @EventListener
  public void handleEvent(DefaultSmokeStateUpdatedPropertyEvent event) {
    writeBooleanValue("smoke", event.getDevice(), event.isActive());
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
