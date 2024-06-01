/*
 * Copyright 2021-2024 the original author or authors.
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

package io.github.davemeier82.homeautomation.spring.core.pushnotification;

import io.github.davemeier82.homeautomation.core.event.DevicePropertyEvent;
import io.github.davemeier82.homeautomation.core.notification.EventPushNotificationMessageTranslator;
import org.springframework.context.MessageSource;

import java.util.Locale;

public class DefaultEventPushNotificationMessageTranslator implements EventPushNotificationMessageTranslator {
  private final MessageSource messageSource;
  private final Locale locale;

  public DefaultEventPushNotificationMessageTranslator(MessageSource messageSource, String defaultLocale) {
    this.messageSource = messageSource;
    locale = Locale.forLanguageTag(defaultLocale);
  }

  @Override
  public String translateMessage(DevicePropertyEvent<?> event) {
    return event.getDisplayName();
  }

  @Override
  public String translateTitle(DevicePropertyEvent<?> event) {
    if (event.getMessageKey() == null) {
      return "";
    }
    if (messageSource == null) {
      return event.getMessageKey();
    }
    return messageSource.getMessage(event.getMessageKey(), event.getMessageArgs(), locale);
  }
}
