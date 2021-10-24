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

package com.github.davemeier82.homeautomation.spring.core.pushnotification.pushbullet;

import com.github.davemeier82.homeautomation.spring.core.pushnotification.PushNotificationServiceRegistry;
import org.springframework.web.reactive.function.client.WebClient;

public class PushbulletNotificationServiceFactory {

  private final PushNotificationServiceRegistry pushNotificationServiceRegistry;
  private final WebClient webClient;

  public PushbulletNotificationServiceFactory(PushNotificationServiceRegistry pushNotificationServiceRegistry,
                                              WebClient webClient,
                                              PushbulletConfiguration pushbulletConfiguration
  ) {
    this.pushNotificationServiceRegistry = pushNotificationServiceRegistry;
    this.webClient = webClient;
    pushbulletConfiguration.credentials().forEach(credential -> createAndAddToRegistry(credential.id(), credential.token()));
  }

  public PushbulletService create(String token) {
    return new PushbulletService(webClient, token);
  }

  public void createAndAddToRegistry(String id, String token) {
    pushNotificationServiceRegistry.add(id, create(token));
  }

}
