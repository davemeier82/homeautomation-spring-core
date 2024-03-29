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

import io.github.davemeier82.homeautomation.core.device.DeviceId;
import io.github.davemeier82.homeautomation.core.device.mqtt.MqttDeviceFactory;
import io.github.davemeier82.homeautomation.core.device.mqtt.MqttSubscriber;
import io.github.davemeier82.homeautomation.core.event.EventPublisher;
import io.github.davemeier82.homeautomation.core.event.MqttClientConnectedEvent;
import io.github.davemeier82.homeautomation.core.event.factory.EventFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;

import java.nio.ByteBuffer;
import java.util.*;
import java.util.function.Function;

import static java.util.stream.Collectors.toMap;

/**
 * Listens to the MQTT messages and tries to create new devices. This is helpful to create the initial
 * {@link io.github.davemeier82.homeautomation.spring.core.config.device.DevicesConfig} without manually typing it.
 *
 * @author David Meier
 * @since 0.1.0
 */
public class OnFirstEventMqttDeviceLoader {
  private static final Logger log = LoggerFactory.getLogger(OnFirstEventMqttDeviceLoader.class);
  private final Map<String, MqttDeviceFactory> deviceFactories;
  private final Set<DeviceId> unknownDeviceIds = new HashSet<>();
  private final DeviceRegistry deviceRegistry;
  private final EventPublisher eventPublisher;
  private final EventFactory eventFactory;

  /**
   * Constructor.
   *
   * @param deviceFactories the device factories
   * @param deviceRegistry  the device registry
   * @param eventPublisher  the event publisher
   * @param eventFactory    the event factory
   */
  public OnFirstEventMqttDeviceLoader(List<MqttDeviceFactory> deviceFactories,
                                      DeviceRegistry deviceRegistry,
                                      EventPublisher eventPublisher,
                                      EventFactory eventFactory
  ) {
    this.deviceFactories = deviceFactories.stream().collect(toMap(MqttDeviceFactory::getRootTopic, Function.identity()));
    this.deviceRegistry = deviceRegistry;
    this.eventPublisher = eventPublisher;
    this.eventFactory = eventFactory;
  }

  /**
   * When the MQTT client is connected it listens to all topics supported by the {@link MqttDeviceFactory}'s
   * and tries to create new devices for all incoming MQTT messages.
   *
   * @param event the event
   */
  @EventListener
  public void loadDevices(MqttClientConnectedEvent event) {
    deviceFactories.forEach((rootTopic, factory) -> {
      log.debug("subscribing for topic '{}#' with factory '{}'", rootTopic, factory.getClass().getSimpleName());
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
          eventPublisher.publishEvent(eventFactory.createNewDeviceCreatedEvent(device));
          if (topic.startsWith(device.getTopic())) {
            device.processMessage(topic, byteBufferOptional);
          }
        });
        unknownDeviceIds.add(deviceId);
      }
    });
  }

  private boolean deviceDoesNotAlreadyExist(DeviceId deviceId) {
    return deviceRegistry.getByDeviceId(deviceId).isEmpty();
  }
}
