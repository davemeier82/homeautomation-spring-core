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

import io.github.davemeier82.homeautomation.core.event.DevicePropertyEvent;
import io.github.davemeier82.homeautomation.core.event.DevicePropertyUpdatedEvent;
import io.github.davemeier82.homeautomation.core.repositories.DevicePropertyValueRepository;
import org.springframework.context.event.EventListener;

import java.util.Set;

public class DeviceStatePersistenceHandler {
  private final Set<DevicePropertyValueRepository> devicePropertyValueRepositories;

  public DeviceStatePersistenceHandler(Set<DevicePropertyValueRepository> devicePropertyValueRepositories) {
    this.devicePropertyValueRepositories = devicePropertyValueRepositories;
  }

  @EventListener
  public void handleEvent(DevicePropertyEvent<?> event) {
    boolean isUpdateEvent = event instanceof DevicePropertyUpdatedEvent;
    if (isUpdateEvent) {
      devicePropertyValueRepositories.forEach(r -> r.insert(event.getDevicePropertyId(), event.getValueType(), event.getDisplayName(), event.getNewValue(), event.getNewTimestamp()));
    }
  }

}
