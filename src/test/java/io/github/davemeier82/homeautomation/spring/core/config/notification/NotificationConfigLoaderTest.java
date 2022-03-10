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
import io.github.davemeier82.homeautomation.core.device.DeviceId;
import io.github.davemeier82.homeautomation.core.event.DevicePropertyEvent;
import io.github.davemeier82.homeautomation.core.event.WindowStateChangedEvent;
import io.github.davemeier82.homeautomation.core.event.factory.DefaultEventFactory;
import io.github.davemeier82.homeautomation.spring.core.pushnotification.PushNotificationServiceRegistry;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.springframework.boot.context.event.ApplicationReadyEvent;

import java.nio.file.Path;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;

class NotificationConfigLoaderTest {

  private final PushNotificationServiceRegistry registry = mock(PushNotificationServiceRegistry.class);
  private final NotificationConfigLoader loader = new NotificationConfigLoader(Path.of("src", "test", "resources", "notification-config.json"),
      registry, new DefaultEventFactory(), new ObjectMapper());

  @Test
  void load() {
    loader.load();

    ArgumentCaptor<Class<? extends DevicePropertyEvent>> devicePropertyEventCaptor = ArgumentCaptor.forClass(Class.class);
    ArgumentCaptor<String> serviceIdCaptor = ArgumentCaptor.forClass(String.class);
    ArgumentCaptor<List<DeviceId>> deviceIdsCaptor = ArgumentCaptor.forClass(List.class);
    ArgumentCaptor<Class<?>> eventCaptor = ArgumentCaptor.forClass(Class.class);

    Mockito.verify(registry).registerToEvent(devicePropertyEventCaptor.capture(), serviceIdCaptor.capture(), deviceIdsCaptor.capture());
    Mockito.verify(registry, times(2)).registerToEvent(eventCaptor.capture(), serviceIdCaptor.capture());

    assertThat(devicePropertyEventCaptor.getValue()).isEqualTo(WindowStateChangedEvent.class);
    assertThat(serviceIdCaptor.getAllValues()).isEqualTo(List.of("service1", "service1", "service2"));
    assertThat(deviceIdsCaptor.getValue()).isEqualTo(List.of(new DeviceId("abc", "typeX"), new DeviceId("123", "typeZ")));
    assertThat(eventCaptor.getValue()).isEqualTo(ApplicationReadyEvent.class);

  }
}