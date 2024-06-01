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

import io.github.davemeier82.homeautomation.core.device.property.DefaultDevicePropertyValueTypeFactory;
import io.github.davemeier82.homeautomation.core.device.property.DevicePropertyValueTypeFactory;
import io.github.davemeier82.homeautomation.core.event.EventPublisher;
import io.github.davemeier82.homeautomation.core.event.factory.DefaultEventFactory;
import io.github.davemeier82.homeautomation.core.event.factory.EventFactory;
import org.springframework.boot.autoconfigure.AutoConfigureOrder;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.ApplicationEventMulticaster;
import org.springframework.context.event.SimpleApplicationEventMulticaster;
import org.springframework.core.task.VirtualThreadTaskExecutor;
import org.springframework.web.client.RestClient;

import static org.springframework.core.Ordered.HIGHEST_PRECEDENCE;

@Configuration
@AutoConfigureOrder(HIGHEST_PRECEDENCE)
public class HomeAutomationCoreAutoConfiguration {

  @Bean
  @ConditionalOnMissingBean
  EventFactory eventFactory() {
    return new DefaultEventFactory();
  }

  @Bean
  @ConditionalOnMissingBean
  EventPublisher eventPublisher(ApplicationEventPublisher applicationEventPublisher) {
    return new SpringEventPublisher(applicationEventPublisher);
  }

  @Bean
  @ConditionalOnMissingBean
  RestClient restClient() {
    return RestClient.create();
  }

  @Bean
  @ConditionalOnMissingBean
  ApplicationEventMulticaster applicationEventMulticaster() {
    SimpleApplicationEventMulticaster eventMulticaster = new SimpleApplicationEventMulticaster();
    eventMulticaster.setTaskExecutor(new VirtualThreadTaskExecutor());
    return eventMulticaster;
  }

  @Bean
  @ConditionalOnMissingBean
  DevicePropertyValueTypeFactory devicePropertyValueTypeFactory() {
    return new DefaultDevicePropertyValueTypeFactory();
  }

}
