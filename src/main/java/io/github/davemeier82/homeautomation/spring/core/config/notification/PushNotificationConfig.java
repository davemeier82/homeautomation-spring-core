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

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

/**
 * Configuration to load and save push notification settings.
 *
 * @author David Meier
 * @since 0.1.0
 */
public record PushNotificationConfig(String serviceId, List<PushNotificationEventConfig> events) {

  @JsonCreator
  public PushNotificationConfig(@JsonProperty("serviceId") String serviceId, @JsonProperty("events") List<PushNotificationEventConfig> events) {
    this.serviceId = serviceId;
    this.events = events;
  }
}
