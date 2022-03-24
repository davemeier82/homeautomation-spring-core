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

import io.github.davemeier82.homeautomation.core.event.DevicesLoadedEvent;
import io.github.davemeier82.homeautomation.core.mqtt.MqttClient;
import org.springframework.context.event.EventListener;

/**
 * Connects the MQTT Client to the Broker after all devices have been loaded.
 *
 * @author David Meier
 * @since 0.1.0
 */
public class MqttClientConnector {

  private final MqttClient mqttClient;

  /**
   * Constructor.
   *
   * @param mqttClient the MQTT client
   */
  public MqttClientConnector(MqttClient mqttClient) {
    this.mqttClient = mqttClient;
  }

  /**
   * Connects the MQTT Client to the Broker after all devices have been loaded
   */
  @EventListener(DevicesLoadedEvent.class)
  public void connectClient() {
    mqttClient.connect();
  }
}
