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

package io.github.davemeier82.homeautomation.spring.core.config.notification;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.davemeier82.homeautomation.core.event.DevicePropertyEvent;
import io.github.davemeier82.homeautomation.core.event.factory.EventFactory;
import io.github.davemeier82.homeautomation.spring.core.pushnotification.PushNotificationServiceRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Optional;

/**
 * Loads {@link PushNotificationConfig} form a JSON file.
 *
 * @author David Meier
 * @since 0.1.0
 */
public class NotificationConfigLoader {
  private static final Logger log = LoggerFactory.getLogger(NotificationConfigLoader.class);

  private final Path configFilePath;
  private final PushNotificationServiceRegistry pushNotificationServiceRegistry;
  private final EventFactory eventFactory;
  private final ObjectMapper objectMapper;

  /**
   * Constructor.
   *
   * @param configFilePath                  the path (incl. filename) to the configuration file
   * @param pushNotificationServiceRegistry registry for push notification services
   * @param eventFactory                    the event factory
   * @param objectMapper                    the object mapper to map the JSON file
   */
  public NotificationConfigLoader(Path configFilePath,
                                  PushNotificationServiceRegistry pushNotificationServiceRegistry,
                                  EventFactory eventFactory,
                                  ObjectMapper objectMapper
  ) {
    this.configFilePath = configFilePath;
    this.eventFactory = eventFactory;
    this.objectMapper = objectMapper;
    this.pushNotificationServiceRegistry = pushNotificationServiceRegistry;
  }

  /**
   * Load the configuration when the application is ready.
   *
   * @param event the event
   */
  @EventListener
  public void onApplicationEvent(ApplicationReadyEvent event) {
    load();
  }

  /**
   * Loads the file and configures the push notification events.
   */
  public void load() {
    loadPushNotificationConfigs().forEach(this::load);
  }

  private void load(PushNotificationConfig pushNotificationConfig) {
    pushNotificationConfig.events().forEach(eventConfig -> registerToServiceRegistry(pushNotificationConfig, eventConfig));
  }

  private void registerToServiceRegistry(PushNotificationConfig pushNotificationConfig, PushNotificationEventConfig eventConfig) {
    Optional<Class<?>> eventClass = eventFactory.fromEventName(eventConfig.type());
    if (eventClass.isPresent()) {
      if (DevicePropertyEvent.class.isAssignableFrom(eventClass.get())) {
        pushNotificationServiceRegistry.registerToEvent((Class<? extends DevicePropertyEvent>) eventClass.get(),
            pushNotificationConfig.serviceId(),
            eventConfig.deviceIds());
      } else {
        pushNotificationServiceRegistry.registerToEvent(eventClass.get(), pushNotificationConfig.serviceId());
      }
    } else {
      try {
        Class<?> aClass = Class.forName(eventConfig.type());
        pushNotificationServiceRegistry.registerToEvent(aClass, pushNotificationConfig.serviceId());
      } catch (ClassNotFoundException e) {
        log.error("event type unknown: {}", eventConfig.type(), e);
      }
    }
  }

  private List<PushNotificationConfig> loadPushNotificationConfigs() {
    if (Files.exists(configFilePath)) {
      try {
        return objectMapper.readValue(configFilePath.toFile(), NotificationConfig.class).pushNotificationConfigs();
      } catch (IOException e) {
        throw new UncheckedIOException(e);
      }
    } else {
      log.info("notification config file '{}' does not exits", configFilePath);
    }
    return List.of();
  }

}
