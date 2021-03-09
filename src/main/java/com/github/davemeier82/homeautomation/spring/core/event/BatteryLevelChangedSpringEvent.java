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

import com.github.davemeier82.homeautomation.core.device.BatteryStateSensor;
import com.github.davemeier82.homeautomation.core.event.BatteryLevelChangedEvent;
import com.github.davemeier82.homeautomation.core.event.DataWithTimestamp;
import org.springframework.context.ApplicationEvent;

public class BatteryLevelChangedSpringEvent extends ApplicationEvent implements BatteryLevelChangedEvent {
  private final DataWithTimestamp<Integer> batteryLevelInPercent;

  public BatteryLevelChangedSpringEvent(BatteryStateSensor source, DataWithTimestamp<Integer> batteryLevelInPercent) {
    super(source);
    this.batteryLevelInPercent = batteryLevelInPercent;
  }

  @Override
  public BatteryStateSensor getSensor() {
    return (BatteryStateSensor) getSource();
  }

  @Override
  public DataWithTimestamp<Integer> getBatteryLevelInPercent() {
    return batteryLevelInPercent;
  }
}
