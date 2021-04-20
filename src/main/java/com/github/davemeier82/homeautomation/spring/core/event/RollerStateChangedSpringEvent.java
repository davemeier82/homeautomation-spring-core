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

import com.github.davemeier82.homeautomation.core.device.property.Roller;
import com.github.davemeier82.homeautomation.core.device.property.RollerState;
import com.github.davemeier82.homeautomation.core.event.DataWithTimestamp;
import com.github.davemeier82.homeautomation.core.event.RollerStateChangedEvent;
import org.springframework.context.ApplicationEvent;

public class RollerStateChangedSpringEvent extends ApplicationEvent implements RollerStateChangedEvent {

  private final DataWithTimestamp<RollerState> state;

  public RollerStateChangedSpringEvent(Roller roller, DataWithTimestamp<RollerState> state) {
    super(roller);
    this.state = state;
  }

  @Override
  public Roller getRoller() {
    return (Roller) source;
  }

  @Override
  public DataWithTimestamp<RollerState> getState() {
    return state;
  }
}
