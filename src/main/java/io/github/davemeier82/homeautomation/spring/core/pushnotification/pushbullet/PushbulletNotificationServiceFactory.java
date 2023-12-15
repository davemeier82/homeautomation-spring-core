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

package io.github.davemeier82.homeautomation.spring.core.pushnotification.pushbullet;

import io.github.davemeier82.homeautomation.spring.core.pushnotification.PushNotificationServiceRegistry;
import org.springframework.web.client.RestClient;

/**
 * Factory to create a Pushbullet (<a href="https://www.pushbullet.com/">pushbullet.com</a>) notification service.
 *
 * @author David Meier
 * @since 0.1.0
 */
public class PushbulletNotificationServiceFactory {

  private final PushNotificationServiceRegistry pushNotificationServiceRegistry;
  private final RestClient restClient;

  /**
   * Constructor.
   *
   * @param pushNotificationServiceRegistry the registry to register this service
   * @param restClient                      restClient for REST calls
   * @param pushbulletConfiguration         the configuration
   */
  public PushbulletNotificationServiceFactory(PushNotificationServiceRegistry pushNotificationServiceRegistry,
                                              RestClient restClient,
                                              PushbulletConfiguration pushbulletConfiguration
  ) {
    this.pushNotificationServiceRegistry = pushNotificationServiceRegistry;
    this.restClient = restClient;
    pushbulletConfiguration.credentials().forEach(credential -> createAndAddToRegistry(credential.id(), credential.token()));
  }

  /**
   * Creates a Pushbullet push notification service for a token
   *
   * @param token the authentication token
   * @return the service
   */
  public PushbulletService create(String token) {
    return new PushbulletService(restClient, token);
  }

  /**
   * Creates a Pushbullet push notification service and registers it to the registry
   *
   * @param id    unique id of the service
   * @param token the authentication token
   */
  public void createAndAddToRegistry(String id, String token) {
    pushNotificationServiceRegistry.add(id, create(token));
  }

}
