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

package com.github.davemeier82.homeautomation.spring.core.event;

import com.github.davemeier82.homeautomation.core.device.WindowSensor;
import com.github.davemeier82.homeautomation.core.event.WindowOpenedEvent;
import org.springframework.context.ApplicationEvent;

import java.time.ZonedDateTime;

public class WindowOpenedSpringEvent extends ApplicationEvent implements WindowOpenedEvent {

  private final ZonedDateTime eventTime;

  public WindowOpenedSpringEvent(WindowSensor source, ZonedDateTime eventTime) {
    super(source);
    this.eventTime = eventTime;
  }

  @Override
  public WindowSensor getWindow() {
    return (WindowSensor) getSource();
  }

  @Override
  public ZonedDateTime getEventTime() {
    return eventTime;
  }
}
