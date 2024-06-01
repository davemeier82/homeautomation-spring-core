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

import io.github.davemeier82.homeautomation.core.event.EventPublisher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationEventPublisher;

public class SpringEventPublisher implements EventPublisher {

  private static final Logger log = LoggerFactory.getLogger(SpringEventPublisher.class);
  private final ApplicationEventPublisher applicationEventPublisher;

  public SpringEventPublisher(ApplicationEventPublisher applicationEventPublisher) {
    this.applicationEventPublisher = applicationEventPublisher;
  }

  @Override
  public void publishEvent(Object event) {
    log.debug("publishing event: {}", event.getClass().getSimpleName());
    applicationEventPublisher.publishEvent(event);
  }
}
