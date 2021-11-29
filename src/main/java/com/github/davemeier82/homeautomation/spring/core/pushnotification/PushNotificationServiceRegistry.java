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

package com.github.davemeier82.homeautomation.spring.core.pushnotification;

import com.github.davemeier82.homeautomation.core.PushNotificationService;
import com.github.davemeier82.homeautomation.core.device.DeviceId;
import com.github.davemeier82.homeautomation.core.event.DevicePropertyEvent;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface PushNotificationServiceRegistry {

  void add(String id, PushNotificationService pushNotificationService);

  void registerToEvent(Class<? extends DevicePropertyEvent> event, String serviceId, List<DeviceId> deviceIds);

  void registerToEvent(Class<?> event, String serviceId);

  Optional<PushNotificationService> getById(String id);

  Set<PushNotificationService> getBy(Class<? extends DevicePropertyEvent> event, DeviceId deviceId);

  Set<PushNotificationService> getBy(Class<?> event);

  Set<PushNotificationService> getAll();
}
