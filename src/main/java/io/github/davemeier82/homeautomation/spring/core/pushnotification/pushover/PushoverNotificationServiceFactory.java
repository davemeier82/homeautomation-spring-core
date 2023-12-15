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

package io.github.davemeier82.homeautomation.spring.core.pushnotification.pushover;

import io.github.davemeier82.homeautomation.spring.core.pushnotification.PushNotificationServiceRegistry;
import org.springframework.web.client.RestClient;

/**
 * Factory to create a Pushover (<a href="https://pushover.net/">pushover.net</a>) notification service.
 *
 * @author David Meier
 * @since 0.1.0
 */
public class PushoverNotificationServiceFactory {

  private final PushNotificationServiceRegistry pushNotificationServiceRegistry;
  private final RestClient restClient;

  /**
   * Constructor
   *
   * @param pushNotificationServiceRegistry the registry to register this service
   * @param restClient                      rest client for REST calls
   * @param pushoverConfiguration           the configuration
   */
  public PushoverNotificationServiceFactory(PushNotificationServiceRegistry pushNotificationServiceRegistry,
                                            RestClient restClient,
                                            PushoverConfiguration pushoverConfiguration
  ) {
    this.pushNotificationServiceRegistry = pushNotificationServiceRegistry;
    this.restClient = restClient;
    pushoverConfiguration.credentials().forEach(credential -> createAndAddToRegistry(credential.id(), credential.user(), credential.token()));
  }

  /**
   * Creates a Pushover push notification service
   *
   * @param user  username
   * @param token authentication token
   * @return the service
   */
  public PushoverService create(String user, String token) {
    return new PushoverService(restClient, user, token);
  }

  /**
   * Creates a Pushover push notification service and registers it to the registry
   *
   * @param id    unique id of the service
   * @param user  username
   * @param token authentication token
   */
  public void createAndAddToRegistry(String id, String user, String token) {
    pushNotificationServiceRegistry.add(id, create(user, token));
  }

}
