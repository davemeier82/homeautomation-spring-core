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
import com.github.davemeier82.homeautomation.core.DeviceStateRepository;
import com.github.davemeier82.homeautomation.core.device.DeviceFactory;
import com.github.davemeier82.homeautomation.core.event.EventFactory;
import com.github.davemeier82.homeautomation.core.event.EventPublisher;
import com.github.davemeier82.homeautomation.core.mqtt.MqttClient;
import com.github.davemeier82.homeautomation.spring.core.config.DeviceConfigWriter;
import com.github.davemeier82.homeautomation.spring.core.config.DeviceLoader;
import com.github.davemeier82.homeautomation.spring.core.pushnotification.PushNotificationServiceRegistry;
import com.github.davemeier82.homeautomation.spring.core.pushnotification.pushover.PushoverConfiguration;
import com.github.davemeier82.homeautomation.spring.core.pushnotification.pushover.PushoverNotificationServiceFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.AutoConfigureOrder;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.ApplicationEventMulticaster;
import org.springframework.context.event.SimpleApplicationEventMulticaster;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.web.client.RestOperations;

import java.nio.file.Path;
import java.util.Set;

import static org.springframework.core.Ordered.LOWEST_PRECEDENCE;

@Configuration
@AutoConfigureOrder(LOWEST_PRECEDENCE)
@EnableConfigurationProperties(PushoverConfiguration.class)
public class HomeAutomationCoreAutoConfiguration {

  @Bean
  @ConditionalOnProperty(prefix = "pushover", name = "enabled", havingValue = "true")
  PushoverNotificationServiceFactory pushoverNotificationServiceFactory(PushNotificationServiceRegistry pushNotificationServiceRegistry,
                                                                        RestOperations restOperations,
                                                                        PushoverConfiguration pushoverConfiguration
  ) {
    return new PushoverNotificationServiceFactory(pushNotificationServiceRegistry, restOperations, pushoverConfiguration);
  }

  @Bean
  @ConditionalOnMissingBean
  @ConditionalOnProperty(prefix = "push-notification", name = "enabled", havingValue = "true")
  PushNotificationServiceRegistry pushNotificationServiceRegistry() {
    return new PushNotificationServiceRegistry();
  }

  @Bean
  @ConditionalOnBean(MqttClient.class)
  @ConditionalOnMissingBean
  MqttClientConnector mqttClientConnector(MqttClient mqttClient) {
    return new MqttClientConnector(mqttClient);
  }

  @Bean
  @ConditionalOnMissingBean
  EventFactory eventFactory() {
    return new SpringEventFactory();
  }

  @Bean
  @ConditionalOnMissingBean
  EventPublisher eventPublisher(ApplicationEventPublisher applicationEventPublisher) {
    return new SpringEventPublisher(applicationEventPublisher);
  }

  @Bean
  @ConditionalOnMissingBean
  DeviceRegistry deviceRegistry() {
    return new DeviceRegistry();
  }

  @Bean
  @ConditionalOnMissingBean
  RestOperations restOperations(RestTemplateBuilder builder) {
    return builder.build();
  }

  @Bean
  @ConditionalOnMissingBean
  ApplicationEventMulticaster applicationEventMulticaster(
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
  @ConditionalOnMissingBean
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
  @ConditionalOnMissingBean
  @ConditionalOnProperty(name = "config.writer.enabled", havingValue = "true")
  DeviceConfigWriter deviceConfigWriter(DeviceRegistry deviceRegistry, ObjectMapper objectMapper, @Value("${config.location}") String configPath) {
    return new DeviceConfigWriter(deviceRegistry, objectMapper, Path.of(configPath));
  }

  @Bean
  @ConditionalOnBean(DeviceStateRepository.class)
  DeviceStatePersistenceHandler deviceStatePersistenceHandler(DeviceStateRepository deviceStateRepository) {
    return new DeviceStatePersistenceHandler(deviceStateRepository);
  }
}
