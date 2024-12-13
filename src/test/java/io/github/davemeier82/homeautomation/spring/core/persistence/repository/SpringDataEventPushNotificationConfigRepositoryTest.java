/*
 * Copyright 2021-2024 the original author or authors.
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

package io.github.davemeier82.homeautomation.spring.core.persistence.repository;

import io.github.davemeier82.homeautomation.core.device.DeviceId;
import io.github.davemeier82.homeautomation.core.device.property.DevicePropertyId;
import io.github.davemeier82.homeautomation.core.event.DataWithTimestamp;
import io.github.davemeier82.homeautomation.core.event.defaults.DefaultDimmingLevelChangedEvent;
import io.github.davemeier82.homeautomation.core.event.defaults.DefaultRelayStateChangedEvent;
import io.github.davemeier82.homeautomation.core.event.defaults.DefaultRelayStateUpdatedEvent;
import io.github.davemeier82.homeautomation.core.notification.EventPushNotificationConfig;
import io.github.davemeier82.homeautomation.spring.core.TestDeviceType;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

import static io.github.davemeier82.homeautomation.core.device.property.DefaultDevicePropertyValueType.RELAY_STATE;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
@ActiveProfiles(profiles = {"it"})
@TestPropertySource(locations = {"classpath:application-it.yml"})
class SpringDataEventPushNotificationConfigRepositoryTest {

  final DeviceId deviceId = new DeviceId("aaa", TestDeviceType.TEST);
  @Autowired
  SpringDataEventPushNotificationConfigRepository repository;

  @Test
  void returnsEmptyForEmptyDb() {
    DevicePropertyId devicePropertyId = new DevicePropertyId(deviceId, "1");

    Set<EventPushNotificationConfig> configs = repository.findAllByEvent(new DefaultRelayStateChangedEvent(devicePropertyId, "abc", new DataWithTimestamp<>(true), new DataWithTimestamp<>(false)));

    assertThat(configs).isEmpty();
  }

  @Test
  @Sql("/add-test-device-type.sql")
  void returnsEntryForMatchingChangeEvent() {
    DevicePropertyId devicePropertyId = new DevicePropertyId(deviceId, "1");

    Set<EventPushNotificationConfig> configs = repository.findAllByEvent(new DefaultRelayStateChangedEvent(devicePropertyId, "abc", new DataWithTimestamp<>(true), new DataWithTimestamp<>(false)));

    assertThat(configs).hasSize(1);
    assertThat(configs.iterator().next().devicePropertyValueType()).isEqualTo(RELAY_STATE);
  }


  @Test
  @Sql("/add-test-device-type.sql")
  void returnsEmptyForUpdateEventWhenOnChangeOnlyIsSetToTrue() {
    DevicePropertyId devicePropertyId = new DevicePropertyId(deviceId, "1");

    Set<EventPushNotificationConfig> configs = repository.findAllByEvent(new DefaultRelayStateUpdatedEvent(devicePropertyId, "abc", new DataWithTimestamp<>(true), new DataWithTimestamp<>(false)));

    assertThat(configs).isEmpty();
  }

  @Test
  @Sql("/add-test-device-type.sql")
  void returnsEmptyForNotMatchingDevicePropertyId() {
    DevicePropertyId devicePropertyId = new DevicePropertyId(deviceId, "2");

    Set<EventPushNotificationConfig> configs = repository.findAllByEvent(new DefaultDimmingLevelChangedEvent(devicePropertyId, "abc", new DataWithTimestamp<>(100), new DataWithTimestamp<>(50)));

    assertThat(configs).isEmpty();
  }

  @Test
  @Sql("/add-test-device-type.sql")
  void returnsEntryForMatchingDevicePropertyId() {
    DevicePropertyId devicePropertyId = new DevicePropertyId(deviceId, "1");

    Set<EventPushNotificationConfig> configs = repository.findAllByEvent(new DefaultDimmingLevelChangedEvent(devicePropertyId, "abc", new DataWithTimestamp<>(100), new DataWithTimestamp<>(50)));

    assertThat(configs).hasSize(1);
    assertThat(configs.stream()
                      .map(EventPushNotificationConfig::devicePropertyId)
                      .map(DevicePropertyId::id)
                      .findFirst().get())
        .isEqualTo(devicePropertyId.id());
  }

}