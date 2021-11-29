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

package com.github.davemeier82.homeautomation.spring.core.pushnotification;

import com.github.davemeier82.homeautomation.core.device.DeviceId;
import com.github.davemeier82.homeautomation.core.event.DevicePropertyEvent;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.MessageSource;
import org.springframework.context.event.EventListener;

import java.util.Locale;

public class DefaultPushNotificationSender {

  private final PushNotificationServiceRegistry registry;
  private final MessageSource messageSource;
  private final Locale locale;

  public DefaultPushNotificationSender(PushNotificationServiceRegistry pushNotificationServiceRegistry,
                                       MessageSource pushNotificationMessageSource,
                                       String defaultLocale
  ) {
    registry = pushNotificationServiceRegistry;
    messageSource = pushNotificationMessageSource;
    locale = Locale.forLanguageTag(defaultLocale);
  }

  @EventListener
  public void handleEvent(DevicePropertyEvent event) {
    registry.getBy(event.getClass(), DeviceId.deviceIdFromDevice(event.getDeviceProperty().getDevice()))
        .forEach(service -> service.sendTextMessage(event.getDeviceProperty().getDevice().getDisplayName(), translate(event)));
  }

  @EventListener
  public void handleEvent(ApplicationEvent event) {
    registry.getBy(event.getClass()).forEach(service -> service.sendTextMessage(event.getClass().getSimpleName(), event.toString()));
  }

  private String translate(DevicePropertyEvent event) {
    if (event.getMessageKey() == null) {
      return "";
    }
    if (messageSource == null) {
      return event.getMessageKey();
    }
    return messageSource.getMessage(event.getMessageKey(), event.getMessageArgs(), locale);
  }
}
