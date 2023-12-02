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
    writeIntValue("battery", event.getDeviceProperty().getLabel(), event.getDevice(), event.getBatteryLevelInPercent());
  }

  @EventListener
  public void handleEvent(DefaultDimmingLevelUpdatedPropertyEvent event) {
    writeIntValue("dimmer", event.getDeviceProperty().getLabel(), event.getDevice(), event.getDimmingLevelInPercent());
  }

  @EventListener
  public void handleEvent(DefaultHumidityUpdatedPropertyEvent event) {
    writeFloatValue("humidity", event.getDeviceProperty().getLabel(), event.getDevice(), event.getRelativeHumidityInPercent());
  }

  @EventListener
  public void handleEvent(DefaultTemperatureUpdatedPropertyEvent event) {
    writeFloatValue("temperature", event.getDeviceProperty().getLabel(), event.getDevice(), event.getTemperatureInDegree());
  }

  @EventListener
  public void handleEvent(DefaultPowerUpdatedPropertyEvent event) {
    writeDoubleValue("power", event.getDeviceProperty().getLabel(), event.getDevice(), event.getWatt());
  }

  @EventListener
  public void handleEvent(DefaultIlluminanceUpdatedPropertyEvent event) {
    writeIntValue("illuminance", event.getDeviceProperty().getLabel(), event.getDevice(), event.getLux());
  }

  @EventListener
  public void handleEvent(DefaultRelayStateUpdatedPropertyEvent event) {
    writeBooleanValue("relay", event.getDeviceProperty().getLabel(), event.getDevice(), event.isOn());
  }

  @EventListener
  public void handleEvent(DefaultMotionUpdatedPropertyEvent event) {
    writeBooleanValue("motion", event.getDeviceProperty().getLabel(), event.getDevice(), event.motionDetected());
  }

  @EventListener
  public void handleEvent(DefaultWindowStateUpdatedPropertyEvent event) {
    writeBooleanValue("window", event.getDeviceProperty().getLabel(), event.getDevice(), event.isOpen());
  }

  @EventListener
  public void handleEvent(DefaultICo2LevelUpdatedPropertyEvent event) {
    writeIntValue("co2", event.getDeviceProperty().getLabel(), event.getDevice(), event.getPpm());
  }

  @EventListener
  public void handleEvent(DefaultSmokeStateUpdatedPropertyEvent event) {
    writeBooleanValue("smoke", event.getDeviceProperty().getLabel(), event.getDevice(), event.isActive());
  }

  @EventListener
  public void handleEvent(DefaultPressureUpdatedPropertyEvent event) {
    writeFloatValue("pressure", event.getDeviceProperty().getLabel(), event.getDevice(), event.getPressureInMbar());
  }

  @EventListener
  public void handleEvent(DefaultUvIndexUpdatedPropertyEvent event) {
    writeFloatValue("uvindex", event.getDeviceProperty().getLabel(), event.getDevice(), event.getUvIndex());
  }

  @EventListener
  public void handleEvent(DefaultCloudBaseUpdatedPropertyEvent event) {
    writeFloatValue("cloudbase", event.getDeviceProperty().getLabel(), event.getDevice(), event.getCloudBaseInMeter());
  }

  @EventListener
  public void handleEvent(DefaultWindSpeedUpdatedPropertyEvent event) {
    writeFloatValue("windspeed", event.getDeviceProperty().getLabel(), event.getDevice(), event.getKilometerPerHour());
  }

  @EventListener
  public void handleEvent(DefaultWindGustSpeedUpdatedPropertyEvent event) {
    writeFloatValue("windgustspeed", event.getDeviceProperty().getLabel(), event.getDevice(), event.getKilometerPerHour());
  }

  @EventListener
  public void handleEvent(DefaultWindDirectionUpdatedPropertyEvent event) {
    writeFloatValue("winddirection", event.getDeviceProperty().getLabel(), event.getDevice(), event.getDegree());
  }

  @EventListener
  public void handleEvent(DefaultWindGustDirectionUpdatedPropertyEvent event) {
    writeFloatValue("windgustdirection", event.getDeviceProperty().getLabel(), event.getDevice(), event.getDegree());
  }

  @EventListener
  public void handleEvent(DefaultWindRunUpdatedPropertyEvent event) {
    writeDoubleValue("windrun", event.getDeviceProperty().getLabel(), event.getDevice(), event.getKilometer());
  }

  @EventListener
  public void handleEvent(DefaultRainRateUpdatedPropertyEvent event) {
    writeFloatValue("rainrate", event.getDeviceProperty().getLabel(), event.getDevice(), event.getMillimeterPerHour());
  }

  @EventListener
  public void handleEvent(DefaultRainIntervalAmountUpdatedPropertyEvent event) {
    writeFloatValue("rainintervalamount", event.getDeviceProperty().getLabel(), event.getDevice(), event.getMillimeter());
  }

  @EventListener
  public void handleEvent(DefaultRainTodayAmountUpdatedPropertyEvent event) {
    writeFloatValue("raintodayamount", event.getDeviceProperty().getLabel(), event.getDevice(), event.getMillimeter());
  }

  private void writeIntValue(String category, String label, Device device, DataWithTimestamp<Integer> data) {
    deviceStateRepository.insert(deviceIdFromDevice(device),
        category,
        label,
        data.getValue(),
        data.getDateTime().toInstant());
  }

  private void writeFloatValue(String category, String label, Device device, DataWithTimestamp<Float> data) {
    deviceStateRepository.insert(deviceIdFromDevice(device),
        category,
        label,
        data.getValue(),
        data.getDateTime().toInstant());
  }

  private void writeDoubleValue(String category, String label, Device device, DataWithTimestamp<Double> data) {
    deviceStateRepository.insert(deviceIdFromDevice(device),
        category,
        label,
        data.getValue(),
        data.getDateTime().toInstant());
  }

  private void writeBooleanValue(String category, String label, Device device, DataWithTimestamp<Boolean> data) {
    deviceStateRepository.insert(deviceIdFromDevice(device),
        category,
        label,
        data.getValue(),
        data.getDateTime().toInstant());
  }
}
