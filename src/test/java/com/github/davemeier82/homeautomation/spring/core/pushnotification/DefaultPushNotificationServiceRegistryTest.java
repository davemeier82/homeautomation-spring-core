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
import com.github.davemeier82.homeautomation.core.device.Device;
import com.github.davemeier82.homeautomation.core.device.DeviceId;
import com.github.davemeier82.homeautomation.core.device.property.WindowSensor;
import com.github.davemeier82.homeautomation.core.event.DataWithTimestamp;
import com.github.davemeier82.homeautomation.core.event.DevicePropertyEvent;
import com.github.davemeier82.homeautomation.core.event.factory.DefaultEventFactory;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class DefaultPushNotificationServiceRegistryTest {

  private static final String DEVICE_ID = "1";
  private static final String DEVICE_TYPE = "a";
  private static final String PUSH_NOTIFICATION_SERVICE_ID = "2";

  private final PushNotificationServiceRegistry registry = new DefaultPushNotificationServiceRegistry();
  private final DefaultEventFactory defaultEventFactory = new DefaultEventFactory();

  @Test
  void getByShouldReturnRegisteredService() {
    DeviceId deviceId = new DeviceId(DEVICE_ID, DEVICE_TYPE);
    PushNotificationService service = mock(PushNotificationService.class);
    registry.add(PUSH_NOTIFICATION_SERVICE_ID, service);
    registry.registerToEvent(DevicePropertyEvent.class, PUSH_NOTIFICATION_SERVICE_ID, List.of(deviceId));

    DevicePropertyEvent windowStateChangedEvent = createWindowStateChangedSpringEvent();
    Set<PushNotificationService> pushNotificationServices = registry.getBy(windowStateChangedEvent.getClass(), deviceId);

    assertThat(pushNotificationServices).isEqualTo(Set.of(service));
  }

  private DevicePropertyEvent createWindowStateChangedSpringEvent() {
    WindowSensor windowSensor = mock(WindowSensor.class);
    Device device = mock(Device.class);
    when(windowSensor.getDevice()).thenReturn(device);
    when(device.getId()).thenReturn(DEVICE_ID);
    when(device.getType()).thenReturn(DEVICE_TYPE);
    return defaultEventFactory.createWindowStateChangedEvent(windowSensor, new DataWithTimestamp<>(true), null);
  }
}