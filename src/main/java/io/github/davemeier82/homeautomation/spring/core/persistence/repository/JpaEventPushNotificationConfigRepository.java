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

import io.github.davemeier82.homeautomation.spring.core.persistence.entity.EventPushNotificationConfigEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Set;
import java.util.UUID;

@Repository
public interface JpaEventPushNotificationConfigRepository extends JpaRepository<EventPushNotificationConfigEntity, UUID> {

  @Query(value = """
      SELECT e FROM event_push_notification_config e
      left join e.device d
      left join e.deviceProperty dp
      WHERE e.onChangeOnly = :onChangeOnly
      AND (e.device IS NULL OR e.device.deviceId = :deviceId)
      AND (e.device IS NULL or e.device.deviceType = :deviceType)
      AND (e.deviceProperty IS NULL OR e.deviceProperty.devicePropertyId = :devicePropertyId)
      AND (e.propertyValueType  IS NULL OR e.propertyValueType = :propertyValueType)
      """)
  Set<EventPushNotificationConfigEntity> findAllBy(String deviceId, String deviceType, String devicePropertyId, String propertyValueType, boolean onChangeOnly);

  @Query(value = """
      SELECT e FROM event_push_notification_config e
      left join e.device d
      left join e.deviceProperty dp
      WHERE
      (e.device IS NULL OR e.device.deviceId = :deviceId)
      AND (e.device IS NULL or e.device.deviceType = :deviceType)
      AND (e.deviceProperty IS NULL OR e.deviceProperty.devicePropertyId = :devicePropertyId)
      AND (e.propertyValueType  IS NULL OR e.propertyValueType = :propertyValueType)
      """)
  Set<EventPushNotificationConfigEntity> findAllBy(String deviceId, String deviceType, String devicePropertyId, String propertyValueType);
}
