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

package com.github.davemeier82.homeautomation.spring.core;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.module.paramnames.ParameterNamesModule;
import com.github.davemeier82.homeautomation.core.PushNotificationService;
import com.github.davemeier82.homeautomation.core.device.DeviceFactory;
import com.github.davemeier82.homeautomation.core.event.EventFactory;
import com.github.davemeier82.homeautomation.core.event.EventPublisher;
import com.github.davemeier82.homeautomation.core.mqtt.MqttClient;
import com.github.davemeier82.homeautomation.spring.core.config.DeviceConfigWriter;
import com.github.davemeier82.homeautomation.spring.core.config.DeviceLoader;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.AutoConfigureOrder;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.ApplicationEventMulticaster;
import org.springframework.context.event.SimpleApplicationEventMulticaster;
import org.springframework.core.Ordered;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.web.client.RestOperations;

import java.nio.file.Path;
import java.util.Set;

import static com.fasterxml.jackson.annotation.JsonCreator.Mode.PROPERTIES;

@Configuration
@AutoConfigureOrder(Ordered.LOWEST_PRECEDENCE)
public class HomeAutomationCoreAutoConfiguration {

  @Bean
  @ConditionalOnClass(PushoverService.class)
  PushNotificationService pushNotificationService(RestOperations restOperations,
                                                  @Value("${pushover.user}") String user,
                                                  @Value("${pushover.token}") String token
  ) {
    return new PushoverService(restOperations, user, token);
  }

  @Bean
  @ConditionalOnBean(MqttClient.class)
  MqttClientConnector mqttClientConnector(MqttClient mqttClient) {
    return new MqttClientConnector(mqttClient);
  }

  @Bean
  public Jackson2ObjectMapperBuilderCustomizer jsonCustomizer() {
    return builder -> builder.modulesToInstall(new ParameterNamesModule(PROPERTIES));
  }

  @Bean
  EventFactory eventFactory() {
    return new SpringEventFactory();
  }

  @Bean
  EventPublisher eventPublisher(ApplicationEventPublisher applicationEventPublisher) {
    return new SpringEventPublisher(applicationEventPublisher);
  }

  @Bean
  DeviceRegistry deviceRegistry() {
    return new DeviceRegistry();
  }

  @Bean
  public RestOperations restOperations(RestTemplateBuilder builder) {
    return builder.build();
  }

  @Bean
  public ApplicationEventMulticaster applicationEventMulticaster(
      @Value("${event.executor.core-pool-size:1}") int corePoolSize,
      @Value("${event.executor.max-pool-size:8}") int maxPoolSize
  ) {
    SimpleApplicationEventMulticaster eventMulticaster =
        new SimpleApplicationEventMulticaster();
    ThreadPoolTaskExecutor threadPoolTaskExecutor = new ThreadPoolTaskExecutor();
    threadPoolTaskExecutor.setCorePoolSize(corePoolSize);
    threadPoolTaskExecutor.setMaxPoolSize(maxPoolSize);
    threadPoolTaskExecutor.initialize();
    eventMulticaster.setTaskExecutor(threadPoolTaskExecutor);
    return eventMulticaster;
  }

  @Bean
  @ConditionalOnProperty(name = "config.location")
  DeviceLoader deviceLoader(@Value("${config.location}") String configPath,
                            ObjectMapper objectMapper,
                            Set<DeviceFactory> deviceFactories,
                            EventFactory eventFactory,
                            EventPublisher eventPublisher
  ) {
    return new DeviceLoader(Path.of(configPath), deviceFactories, objectMapper, eventFactory, eventPublisher);
  }

  @Bean
  @ConditionalOnProperty(name = "config.writer.enabled", havingValue = "true")
  DeviceConfigWriter deviceConfigWriter(DeviceRegistry deviceRegistry, ObjectMapper objectMapper, @Value("${config.location}") String configPath) {
    return new DeviceConfigWriter(deviceRegistry, objectMapper, Path.of(configPath));
  }
}
