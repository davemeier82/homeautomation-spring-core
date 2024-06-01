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

import io.github.davemeier82.homeautomation.core.device.DeviceTypeFactory;
import io.github.davemeier82.homeautomation.core.device.DeviceTypeMapper;
import io.github.davemeier82.homeautomation.core.device.property.DevicePropertyTypeFactory;
import io.github.davemeier82.homeautomation.core.device.property.DevicePropertyTypeMapper;
import org.springframework.boot.autoconfigure.AutoConfigureOrder;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

import static org.springframework.core.Ordered.LOWEST_PRECEDENCE;

@Configuration
@AutoConfigureOrder(LOWEST_PRECEDENCE)
public class HomeAutomationCoreDeviceAutoConfiguration {

  @Bean
  @ConditionalOnMissingBean
  DeviceTypeMapper deviceTypeMapper(List<DeviceTypeFactory> deviceTypeFactories) {
    return new DeviceTypeMapper(deviceTypeFactories);
  }

  @Bean
  @ConditionalOnMissingBean
  DevicePropertyTypeMapper devicePropertyTypeMapper(List<DevicePropertyTypeFactory> devicePropertyTypeFactories) {
    return new DevicePropertyTypeMapper(devicePropertyTypeFactories);
  }

}
