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

import com.github.davemeier82.homeautomation.core.device.Device;
import com.github.davemeier82.homeautomation.core.device.property.*;
import com.github.davemeier82.homeautomation.core.event.*;
import com.github.davemeier82.homeautomation.core.mqtt.MqttClient;
import com.github.davemeier82.homeautomation.spring.core.event.*;

import java.time.ZonedDateTime;

public class SpringEventFactory implements EventFactory {
  @Override
  public MotionDetectedEvent createMotionDetectedEvent(MotionSensor sensor, ZonedDateTime eventTime) {
    return new MotionDetectedSpringEvent(sensor, eventTime);
  }

  @Override
  public MqttClientConnectedEvent createMqttClientConnectedEvent(MqttClient client) {
    return new MqttClientConnectedSpringEvent(client);
  }

  @Override
  public RelayStateChangedEvent createRelayStateChangedEvent(Relay relay, DataWithTimestamp<Boolean> isOn) {
    return new RelayStateChangedSpringEvent(relay, isOn);
  }

  @Override
  public WindowOpenedEvent createWindowOpenedEvent(WindowSensor windowSensor, ZonedDateTime eventTime) {
    return new WindowOpenedSpringEvent(windowSensor, eventTime);
  }

  @Override
  public WindowClosedEvent createWindowClosedEvent(WindowSensor windowSensor, ZonedDateTime eventTime) {
    return new WindowClosedSpringEvent(windowSensor, eventTime);
  }

  @Override
  public TemperatureChangedEvent createTemperatureChangedEvent(TemperatureSensor temperatureSensor, DataWithTimestamp<Float> temperatureInDegree) {
    return new TemperatureChangedSpringEvent(temperatureSensor, temperatureInDegree);
  }

  @Override
  public HumidityChangedEvent createHumidityChangedEvent(HumiditySensor humiditySensor, DataWithTimestamp<Float> relativeHumidityInPercent) {
    return new HumidityChangedSpringEvent(humiditySensor, relativeHumidityInPercent);
  }

  @Override
  public BatteryLevelChangedEvent createBatteryLevelChangedEvent(BatteryStateSensor batteryStateSensor,
                                                                 DataWithTimestamp<Integer> batteryLevelInPercent
  ) {
    return new BatteryLevelChangedSpringEvent(batteryStateSensor, batteryLevelInPercent);
  }

  @Override
  public DimmingLevelChangedEvent createDimmingLevelChangedEvent(Dimmer dimmer, DataWithTimestamp<Integer> levelInPercent) {
    return new DimmingLevelChangedSpringEvent(dimmer, levelInPercent);
  }

  @Override
  public DevicesLoadedEvent createDevicesLoadedEvent(Object source) {
    return new DevicesLoadedSpringEvent(source);
  }

  @Override
  public NewDeviceCreatedEvent createNewDeviceCreatedEvent(Device device) {
    return new NewDeviceCreatedSpringEvent(device);
  }

  @Override
  public RollerStateChangedEvent createRollerStateChangedEvent(Roller roller, DataWithTimestamp<RollerState> state) {
    return new RollerStateChangedSpringEvent(roller, state);
  }

  @Override
  public RollerPositionChangedEvent createRollerPositionChangedEvent(Roller roller, DataWithTimestamp<Integer> positionInPercent) {
    return new RollerPositionChangedSpringEvent(roller, positionInPercent);
  }

  @Override
  public IlluminanceChangedEvent createIlluminanceChangedEvent(IlluminanceSensor sensor, DataWithTimestamp<Integer> lux
  ) {
    return new IlluminanceChangedSpringEvent(sensor, lux);
  }
}
