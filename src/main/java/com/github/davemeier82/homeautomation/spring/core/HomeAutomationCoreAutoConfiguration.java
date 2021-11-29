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
import com.github.davemeier82.homeautomation.core.event.EventPublisher;
import com.github.davemeier82.homeautomation.core.event.factory.DefaultEventFactory;
import com.github.davemeier82.homeautomation.core.event.factory.EventFactory;
import com.github.davemeier82.homeautomation.core.mqtt.MqttClient;
import com.github.davemeier82.homeautomation.spring.core.config.device.DeviceConfigWriter;
import com.github.davemeier82.homeautomation.spring.core.config.device.DeviceLoader;
import com.github.davemeier82.homeautomation.spring.core.config.notification.NotificationConfigLoader;
import com.github.davemeier82.homeautomation.spring.core.pushnotification.DefaultPushNotificationSender;
import com.github.davemeier82.homeautomation.spring.core.pushnotification.DefaultPushNotificationServiceRegistry;
import com.github.davemeier82.homeautomation.spring.core.pushnotification.PushNotificationServiceRegistry;
import com.github.davemeier82.homeautomation.spring.core.pushnotification.pushbullet.PushbulletConfiguration;
import com.github.davemeier82.homeautomation.spring.core.pushnotification.pushbullet.PushbulletNotificationServiceFactory;
import com.github.davemeier82.homeautomation.spring.core.pushnotification.pushover.PushoverConfiguration;
import com.github.davemeier82.homeautomation.spring.core.pushnotification.pushover.PushoverNotificationServiceFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.AutoConfigureOrder;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.ApplicationEventMulticaster;
import org.springframework.context.event.SimpleApplicationEventMulticaster;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.web.reactive.function.client.WebClient;

import java.nio.file.Path;
import java.util.Set;

import static java.nio.charset.StandardCharsets.UTF_8;
import static org.springframework.core.Ordered.LOWEST_PRECEDENCE;

@Configuration
@AutoConfigureOrder(LOWEST_PRECEDENCE)
@EnableConfigurationProperties({PushoverConfiguration.class, PushbulletConfiguration.class})
public class HomeAutomationCoreAutoConfiguration {

  @Bean
  @ConditionalOnProperty(prefix = "pushover", name = "enabled", havingValue = "true")
  PushoverNotificationServiceFactory pushoverNotificationServiceFactory(PushNotificationServiceRegistry pushNotificationServiceRegistry,
                                                                        WebClient webClient,
                                                                        PushoverConfiguration pushoverConfiguration
  ) {
    return new PushoverNotificationServiceFactory(pushNotificationServiceRegistry, webClient, pushoverConfiguration);
  }

  @Bean
  @ConditionalOnProperty(prefix = "pushbullet", name = "enabled", havingValue = "true")
  PushbulletNotificationServiceFactory pushbulletNotificationServiceFactory(PushNotificationServiceRegistry pushNotificationServiceRegistry,
                                                                            WebClient webClient,
                                                                            PushbulletConfiguration pushbulletConfiguration
  ) {
    return new PushbulletNotificationServiceFactory(pushNotificationServiceRegistry, webClient, pushbulletConfiguration);
  }

  @Bean
  @ConditionalOnMissingBean
  @ConditionalOnProperty(prefix = "notification.push", name = "enabled", havingValue = "true")
  PushNotificationServiceRegistry pushNotificationServiceRegistry() {
    return new DefaultPushNotificationServiceRegistry();
  }

  @Bean
  @ConditionalOnProperty(prefix = "notification.push.default-sender", name = "enabled", havingValue = "true")
  DefaultPushNotificationSender defaultPushNotificationSender(PushNotificationServiceRegistry pushNotificationServiceRegistry,
                                                              MessageSource pushNotificationMessageSource,
                                                              @Value("${notification.push.translation.properties.baseName:defaultLocale:en}") String defaultLocale
  ) {
    return new DefaultPushNotificationSender(pushNotificationServiceRegistry, pushNotificationMessageSource, defaultLocale);
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
    return new DefaultEventFactory();
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
  WebClient webClient() {
    return WebClient.create();
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
  @ConditionalOnProperty(prefix = "device.config", name = "location")
  DeviceLoader deviceLoader(@Value("${device.config.location}") String configPath,
                            ObjectMapper objectMapper,
                            Set<DeviceFactory> deviceFactories,
                            EventFactory eventFactory,
                            EventPublisher eventPublisher
  ) {
    return new DeviceLoader(Path.of(configPath), deviceFactories, objectMapper, eventFactory, eventPublisher);
  }

  @Bean
  @ConditionalOnMissingBean
  @ConditionalOnProperty(prefix = "config.writer", name = "enabled", havingValue = "true")
  DeviceConfigWriter deviceConfigWriter(DeviceRegistry deviceRegistry,
                                        ObjectMapper objectMapper,
                                        @Value("${device.config.location}") String configPath
  ) {
    return new DeviceConfigWriter(deviceRegistry, objectMapper, Path.of(configPath));
  }

  @Bean
  @ConditionalOnBean(DeviceStateRepository.class)
  DeviceStatePersistenceHandler deviceStatePersistenceHandler(DeviceStateRepository deviceStateRepository) {
    return new DeviceStatePersistenceHandler(deviceStateRepository);
  }

  @Bean
  @ConditionalOnMissingBean
  MessageSource pushNotificationMessageSource(@Value("${notification.push.translation.properties.baseName:device-property-message}") String baseName) {
    ResourceBundleMessageSource rs = new ResourceBundleMessageSource();
    rs.setBasename(baseName);
    rs.setDefaultEncoding(UTF_8.name());
    rs.setUseCodeAsDefaultMessage(true);
    return rs;
  }

  @Bean
  @ConditionalOnProperty(prefix = "notification.config", name = "location")
  NotificationConfigLoader notificationConfigLoader(@Value("${notification.config.location}") String configPath,
                                                    PushNotificationServiceRegistry pushNotificationServiceRegistry,
                                                    EventFactory eventFactory,
                                                    ObjectMapper objectMapper
  ) {
    return new NotificationConfigLoader(Path.of(configPath), pushNotificationServiceRegistry, eventFactory, objectMapper);
  }
}
