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

import io.github.davemeier82.homeautomation.core.notification.DefaultEventPushNotificationSender;
import io.github.davemeier82.homeautomation.core.notification.EventPushNotificationConfigRepository;
import io.github.davemeier82.homeautomation.core.notification.EventPushNotificationMessageTranslator;
import io.github.davemeier82.homeautomation.core.notification.EventPushNotificationSender;
import io.github.davemeier82.homeautomation.core.notification.PushNotificationService;
import io.github.davemeier82.homeautomation.spring.core.pushnotification.DefaultEventPushNotificationMessageTranslator;
import io.github.davemeier82.homeautomation.spring.core.pushnotification.SpringEventPushNotificationSenderAdapter;
import io.github.davemeier82.homeautomation.spring.core.pushnotification.pushbullet.PushbulletConfiguration;
import io.github.davemeier82.homeautomation.spring.core.pushnotification.pushbullet.PushbulletService;
import io.github.davemeier82.homeautomation.spring.core.pushnotification.pushover.PushoverConfiguration;
import io.github.davemeier82.homeautomation.spring.core.pushnotification.pushover.PushoverService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.web.client.RestClient;

import java.util.Set;

import static java.nio.charset.StandardCharsets.UTF_8;

@Configuration
@AutoConfigureAfter(HomeAutomationCorePersistenceAutoConfiguration.class)
@EnableConfigurationProperties({PushoverConfiguration.class, PushbulletConfiguration.class})
@ConditionalOnProperty(prefix = "homeautomation.spring-core.notification.push", name = "enabled", havingValue = "true")
public class HomeAutomationCorePushNotificationAutoConfiguration {

  @Bean
  @ConditionalOnProperty(prefix = "homeautomation.spring-core.notification.push.pushover", name = "enabled", havingValue = "true")
  PushoverService pushoverNotificationServiceFactory(RestClient restClient,
                                                     PushoverConfiguration pushoverConfiguration
  ) {
    return new PushoverService(restClient, pushoverConfiguration);
  }

  @Bean
  @ConditionalOnProperty(prefix = "homeautomation.spring-core.notification.push.pushbullet", name = "enabled", havingValue = "true")
  PushbulletService pushbulletNotificationServiceFactory(RestClient restClient,
                                                         PushbulletConfiguration pushbulletConfiguration
  ) {
    return new PushbulletService(restClient, pushbulletConfiguration);
  }

  @Bean
  @ConditionalOnProperty(prefix = "homeautomation.spring-core.notification.push.default-sender", name = "enabled", havingValue = "true")
  @ConditionalOnMissingBean
  EventPushNotificationSender eventPushNotificationSender(EventPushNotificationConfigRepository eventPushNotificationConfigRepository,
                                                          Set<PushNotificationService> pushNotificationServices,
                                                          EventPushNotificationMessageTranslator eventPushNotificationMessageTranslator
  ) {
    return new DefaultEventPushNotificationSender(eventPushNotificationConfigRepository, pushNotificationServices, eventPushNotificationMessageTranslator);
  }

  @Bean
  @ConditionalOnBean(EventPushNotificationSender.class)
  SpringEventPushNotificationSenderAdapter springEventPushNotificationSenderAdapter(EventPushNotificationSender eventPushNotificationSender) {
    return new SpringEventPushNotificationSenderAdapter(eventPushNotificationSender);
  }

  @Bean
  @ConditionalOnMissingBean
  EventPushNotificationMessageTranslator eventPushNotificationMessageTranslator(MessageSource pushNotificationMessageSource,
                                                                                @Value("${homeautomation.spring-core.notification.push.translation.properties.defaultLocale:en}") String defaultLocale
  ) {
    return new DefaultEventPushNotificationMessageTranslator(pushNotificationMessageSource, defaultLocale);
  }

  @Bean
  @ConditionalOnMissingBean
  MessageSource pushNotificationMessageSource(@Value("${homeautomation.spring-core.notification.push.translation.properties.baseName:device-property-message}") String baseName) {
    ResourceBundleMessageSource rs = new ResourceBundleMessageSource();
    rs.setBasename(baseName);
    rs.setDefaultEncoding(UTF_8.name());
    rs.setUseCodeAsDefaultMessage(true);
    return rs;
  }


}
