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

package io.github.davemeier82.homeautomation.spring.core.pushnotification;

import io.github.davemeier82.homeautomation.core.PushNotificationService;
import io.github.davemeier82.homeautomation.core.device.DeviceId;
import io.github.davemeier82.homeautomation.core.event.DevicePropertyEvent;

import java.util.List;
import java.util.Optional;
import java.util.Set;

/**
 * Registry to register events and devices that should trigger a push notification.
 *
 * @author David Meier
 * @since 0.1.0
 */
public interface PushNotificationServiceRegistry {

  /**
   * Adds a push notification service
   *
   * @param id                      unique id
   * @param pushNotificationService the service
   */
  void add(String id, PushNotificationService pushNotificationService);

  /**
   * Registers an event that should send messages to a push notification service for the given devices.
   *
   * @param event     the event
   * @param serviceId the id of the push notification service
   * @param deviceIds the device ids
   */
  void registerToEvent(Class<? extends DevicePropertyEvent> event, String serviceId, List<DeviceId> deviceIds);

  /**
   * Register an event that should send messages to a push notification service.
   *
   * @param event     the event
   * @param serviceId the id of the push notification service
   */
  void registerToEvent(Class<?> event, String serviceId);

  /**
   * Returns a push notification service for an id.
   *
   * @param id the id
   * @return the service
   */
  Optional<PushNotificationService> getById(String id);

  /**
   * Returns all push notification services for an event and device id.
   *
   * @param event    the event
   * @param deviceId the device id
   * @return all services that match the parameters
   */
  Set<PushNotificationService> getBy(Class<? extends DevicePropertyEvent> event, DeviceId deviceId);

  /**
   * Returns all push notification services for an event.
   *
   * @param event the event
   * @return the services
   */
  Set<PushNotificationService> getBy(Class<?> event);

  /**
   * @return all push notification services.
   */
  Set<PushNotificationService> getAll();
}
