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

package io.github.davemeier82.homeautomation.spring.core;

import io.github.davemeier82.homeautomation.spring.core.persistence.repository.SpringDataDevicePropertyValueRepository;
import net.javacrumbs.shedlock.spring.annotation.SchedulerLock;
import org.springframework.scheduling.annotation.Scheduled;

import java.time.Duration;
import java.time.OffsetDateTime;

public class SpringDataDevicePropertyValueRepositoryHousekeeper {

  private final SpringDataDevicePropertyValueRepository repository;
  private final Duration duration;

  public SpringDataDevicePropertyValueRepositoryHousekeeper(SpringDataDevicePropertyValueRepository repository, Duration deleteOlderThanDuration) {
    this.repository = repository;
    duration = deleteOlderThanDuration;
  }

  @Scheduled(cron = "0 0 3 * * ?")
  @SchedulerLock(name = "SDDPVR_Housekeeper",
      lockAtLeastFor = "PT5M", lockAtMostFor = "PT60M")
  public void deleteOldValues() {
    repository.deleteAllBefore(OffsetDateTime.now().minus(duration));
  }
}
