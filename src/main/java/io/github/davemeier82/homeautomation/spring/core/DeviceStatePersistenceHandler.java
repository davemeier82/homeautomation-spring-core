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

import io.github.davemeier82.homeautomation.core.device.Device;
import io.github.davemeier82.homeautomation.core.device.property.DeviceProperty;
import io.github.davemeier82.homeautomation.core.event.DataWithTimestamp;
import io.github.davemeier82.homeautomation.core.event.DevicePropertyEvent;
import io.github.davemeier82.homeautomation.core.event.defaults.*;
import io.github.davemeier82.homeautomation.core.repositories.DevicePropertyRepository;
import io.github.davemeier82.homeautomation.core.repositories.DeviceStateRepository;
import org.springframework.context.event.EventListener;

import java.util.function.Consumer;

import static io.github.davemeier82.homeautomation.core.device.DeviceId.deviceIdFromDevice;

/**
 * This class listens to events and stores the values in the {@link DeviceStateRepository}.
 *
 * @author David Meier
 * @since 0.1.0
 */
public class DeviceStatePersistenceHandler {
  private final DeviceStateRepository deviceStateRepository;
  private final DevicePropertyRepository devicePropertyRepository;

  /**
   * Constructor.
   *
   * @param deviceStateRepository    the repository
   * @param devicePropertyRepository the device repository
   */
  public DeviceStatePersistenceHandler(DeviceStateRepository deviceStateRepository, DevicePropertyRepository devicePropertyRepository) {
    this.deviceStateRepository = deviceStateRepository;
    this.devicePropertyRepository = devicePropertyRepository;
  }

  @EventListener
  public void handleEvent(DefaultBatteryLevelUpdatedPropertyEvent event) {
    consumeEvent(event, deviceProperty -> writeIntValue("battery", deviceProperty.getLabel(), deviceProperty.getDevice(), event.getBatteryLevelInPercent()));
  }

  @EventListener
  public void handleEvent(DefaultDimmingLevelUpdatedPropertyEvent event) {
    consumeEvent(event, deviceProperty -> writeIntValue("dimmer", deviceProperty.getLabel(), deviceProperty.getDevice(), event.getDimmingLevelInPercent()));
  }

  @EventListener
  public void handleEvent(DefaultHumidityUpdatedPropertyEvent event) {
    consumeEvent(event, deviceProperty -> writeFloatValue("humidity", deviceProperty.getLabel(), deviceProperty.getDevice(), event.getRelativeHumidityInPercent()));
  }

  @EventListener
  public void handleEvent(DefaultTemperatureUpdatedPropertyEvent event) {
    consumeEvent(event, deviceProperty -> writeFloatValue("temperature", deviceProperty.getLabel(), deviceProperty.getDevice(), event.getTemperatureInDegree()));
  }

  @EventListener
  public void handleEvent(DefaultPowerUpdatedPropertyEvent event) {
    consumeEvent(event, deviceProperty -> writeDoubleValue("power", deviceProperty.getLabel(), deviceProperty.getDevice(), event.getWatt()));
  }

  @EventListener
  public void handleEvent(DefaultIlluminanceUpdatedPropertyEvent event) {
    consumeEvent(event, deviceProperty -> writeIntValue("illuminance", deviceProperty.getLabel(), deviceProperty.getDevice(), event.getLux()));
  }

  @EventListener
  public void handleEvent(DefaultRelayStateUpdatedPropertyEvent event) {
    consumeEvent(event, deviceProperty -> writeBooleanValue("relay", deviceProperty.getLabel(), deviceProperty.getDevice(), event.isOn()));
  }

  @EventListener
  public void handleEvent(DefaultMotionUpdatedPropertyEvent event) {
    consumeEvent(event, deviceProperty -> writeBooleanValue("motion", deviceProperty.getLabel(), deviceProperty.getDevice(), event.motionDetected()));
  }

  @EventListener
  public void handleEvent(DefaultWindowStateUpdatedPropertyEvent event) {
    consumeEvent(event, deviceProperty -> writeBooleanValue("window", deviceProperty.getLabel(), deviceProperty.getDevice(), event.isOpen()));
  }

  @EventListener
  public void handleEvent(DefaultICo2LevelUpdatedPropertyEvent event) {
    consumeEvent(event, deviceProperty -> writeIntValue("co2", deviceProperty.getLabel(), deviceProperty.getDevice(), event.getPpm()));
  }

  @EventListener
  public void handleEvent(DefaultSmokeStateUpdatedPropertyEvent event) {
    consumeEvent(event, deviceProperty -> writeBooleanValue("smoke", deviceProperty.getLabel(), deviceProperty.getDevice(), event.isActive()));
  }

  @EventListener
  public void handleEvent(DefaultPressureUpdatedPropertyEvent event) {
    consumeEvent(event, deviceProperty -> writeFloatValue("pressure", deviceProperty.getLabel(), deviceProperty.getDevice(), event.getPressureInMbar()));
  }

  @EventListener
  public void handleEvent(DefaultUvIndexUpdatedPropertyEvent event) {
    consumeEvent(event, deviceProperty -> writeFloatValue("uvindex", deviceProperty.getLabel(), deviceProperty.getDevice(), event.getUvIndex()));
  }

  @EventListener
  public void handleEvent(DefaultCloudBaseUpdatedPropertyEvent event) {
    consumeEvent(event, deviceProperty -> writeFloatValue("cloudbase", deviceProperty.getLabel(), deviceProperty.getDevice(), event.getCloudBaseInMeter()));
  }

  @EventListener
  public void handleEvent(DefaultWindSpeedUpdatedPropertyEvent event) {
    consumeEvent(event, deviceProperty -> writeFloatValue("windspeed", deviceProperty.getLabel(), deviceProperty.getDevice(), event.getKilometerPerHour()));
  }

  @EventListener
  public void handleEvent(DefaultWindGustSpeedUpdatedPropertyEvent event) {
    consumeEvent(event, deviceProperty -> writeFloatValue("windgustspeed", deviceProperty.getLabel(), deviceProperty.getDevice(), event.getKilometerPerHour()));
  }

  @EventListener
  public void handleEvent(DefaultWindDirectionUpdatedPropertyEvent event) {
    consumeEvent(event, deviceProperty -> writeFloatValue("winddirection", deviceProperty.getLabel(), deviceProperty.getDevice(), event.getDegree()));
  }

  @EventListener
  public void handleEvent(DefaultWindGustDirectionUpdatedPropertyEvent event) {
    consumeEvent(event, deviceProperty -> writeFloatValue("windgustdirection", deviceProperty.getLabel(), deviceProperty.getDevice(), event.getDegree()));
  }

  @EventListener
  public void handleEvent(DefaultWindRunUpdatedPropertyEvent event) {
    consumeEvent(event, deviceProperty -> writeDoubleValue("windrun", deviceProperty.getLabel(), deviceProperty.getDevice(), event.getKilometer()));
  }

  @EventListener
  public void handleEvent(DefaultRainRateUpdatedPropertyEvent event) {
    consumeEvent(event, deviceProperty -> writeFloatValue("rainrate", deviceProperty.getLabel(), deviceProperty.getDevice(), event.getMillimeterPerHour()));
  }

  @EventListener
  public void handleEvent(DefaultRainIntervalAmountUpdatedPropertyEvent event) {
    consumeEvent(event, deviceProperty -> writeFloatValue("rainintervalamount", deviceProperty.getLabel(), deviceProperty.getDevice(), event.getMillimeter()));
  }

  @EventListener
  public void handleEvent(DefaultRainTodayAmountUpdatedPropertyEvent event) {
    consumeEvent(event, deviceProperty -> writeFloatValue("raintodayamount", deviceProperty.getLabel(), deviceProperty.getDevice(), event.getMillimeter()));
  }

  private void writeIntValue(String category, String label, Device device, DataWithTimestamp<Integer> data) {
    if (data == null) {
      return;
    }
    deviceStateRepository.insert(deviceIdFromDevice(device),
        category,
        label,
        data.getValue(),
        data.getDateTime().toInstant());
  }

  private void writeFloatValue(String category, String label, Device device, DataWithTimestamp<Float> data) {
    if (data == null) {
      return;
    }
    deviceStateRepository.insert(deviceIdFromDevice(device),
        category,
        label,
        data.getValue(),
        data.getDateTime().toInstant());
  }

  private void writeDoubleValue(String category, String label, Device device, DataWithTimestamp<Double> data) {
    if (data == null) {
      return;
    }
    deviceStateRepository.insert(deviceIdFromDevice(device),
        category,
        label,
        data.getValue(),
        data.getDateTime().toInstant());
  }

  private void writeBooleanValue(String category, String label, Device device, DataWithTimestamp<Boolean> data) {
    if (data == null) {
      return;
    }
    deviceStateRepository.insert(deviceIdFromDevice(device),
        category,
        label,
        data.getValue(),
        data.getDateTime().toInstant());
  }

  private void consumeEvent(DevicePropertyEvent event, Consumer<DeviceProperty> consumer) {
    devicePropertyRepository.getByDevicePropertyId(event.getDevicePropertyId()).ifPresent(consumer);
  }

}
