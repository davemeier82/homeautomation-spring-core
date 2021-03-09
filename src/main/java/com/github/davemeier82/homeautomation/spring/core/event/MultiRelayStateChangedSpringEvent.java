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

import com.github.davemeier82.homeautomation.core.device.MultiRelay;
import com.github.davemeier82.homeautomation.core.event.DataWithTimestamp;
import com.github.davemeier82.homeautomation.core.event.MultiRelayStateChangedEvent;
import org.springframework.context.ApplicationEvent;

public class MultiRelayStateChangedSpringEvent extends ApplicationEvent implements MultiRelayStateChangedEvent {

  private final int indexOfChangedRelay;
  private final DataWithTimestamp<Boolean> isOn;

  public MultiRelayStateChangedSpringEvent(MultiRelay relay, int indexOfChangedRelay, DataWithTimestamp<Boolean> isOn) {
    super(relay);
    this.indexOfChangedRelay = indexOfChangedRelay;
    this.isOn = isOn;
  }

  @Override
  public MultiRelay getRelay() {
    return (MultiRelay) getSource();
  }

  @Override
  public int getIndexOfChangedRelay() {
    return indexOfChangedRelay;
  }

  @Override
  public DataWithTimestamp<Boolean> isOn() {
    return isOn;
  }
}
