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

import com.github.davemeier82.homeautomation.core.device.DeviceId;
import com.github.davemeier82.homeautomation.core.device.mqtt.MqttDeviceFactory;
import com.github.davemeier82.homeautomation.core.device.mqtt.MqttSubscriber;
import com.github.davemeier82.homeautomation.core.mqtt.MqttClient;
import com.github.davemeier82.homeautomation.spring.core.event.MqttClientConnectedSpringEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;

import java.nio.ByteBuffer;
import java.util.*;
import java.util.function.Function;

import static java.util.stream.Collectors.toMap;

public class OnFirstEventMqttDeviceLoader {
  private static final Logger log = LoggerFactory.getLogger(OnFirstEventMqttDeviceLoader.class);
  private final Map<String, MqttDeviceFactory> deviceFactories;
  private final Set<DeviceId> unknownDeviceIds = new HashSet<>();
  private final MqttClient mqttClient;
  private final DeviceRegistry deviceRegistry;

  public OnFirstEventMqttDeviceLoader(List<MqttDeviceFactory> deviceFactories, MqttClient mqttClient, DeviceRegistry deviceRegistry) {
    this.deviceFactories = deviceFactories.stream().collect(toMap(MqttDeviceFactory::getRootTopic, Function.identity()));
    this.mqttClient = mqttClient;
    this.deviceRegistry = deviceRegistry;
  }

  @EventListener
  public void loadDevices(MqttClientConnectedSpringEvent event) {
    deviceFactories.forEach((rootTopic, factory) -> {
      log.debug("subscribing for topic '{}' with factory '{}'", rootTopic, factory.getClass().getSimpleName());
      event.getClient().subscribe(rootTopic + "#", (topic, byteBuffer) -> createDevice(topic, factory, byteBuffer));
    });
  }

  @SuppressWarnings("OptionalUsedAsFieldOrParameterType")
  private synchronized void createDevice(String topic, MqttDeviceFactory factory, Optional<ByteBuffer> byteBufferOptional) {
    factory.getDeviceId(topic).ifPresent(deviceId -> {
      if (!unknownDeviceIds.contains(deviceId) && deviceDoesNotAlreadyExist(deviceId)) {
        //TODO: possible duplicate device
        Optional<MqttSubscriber> deviceOptional = factory.createMqttSubscriber(deviceId);
        deviceOptional.ifPresent(device -> {
          if (topic.startsWith(device.getTopic())) {
            device.processMessage(topic, byteBufferOptional);
          }
          mqttClient.subscribe(device.getTopic(), device::processMessage);
        });
        unknownDeviceIds.add(deviceId);
      }
    });
  }

  private boolean deviceDoesNotAlreadyExist(DeviceId deviceId) {
    return deviceRegistry.getByDeviceId(deviceId).isEmpty();
  }
}
