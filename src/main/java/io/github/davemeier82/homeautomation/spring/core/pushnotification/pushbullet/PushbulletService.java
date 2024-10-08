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

import io.github.davemeier82.homeautomation.core.notification.PushNotificationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.client.RestClient;

import java.util.Map;
import java.util.Set;

import static java.util.stream.Collectors.toMap;
import static org.springframework.http.HttpHeaders.CONTENT_TYPE;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

public class PushbulletService implements PushNotificationService {

  private static final Logger log = LoggerFactory.getLogger(PushbulletService.class);
  private static final String PUSHBULLET_URI = "https://api.pushbullet.com/v2/pushes";
  private final RestClient restClient;
  private final Map<String, String> serviceIdToToken;

  public PushbulletService(RestClient restClient, PushbulletConfiguration configuration) {
    this.restClient = restClient;
    serviceIdToToken = configuration.credentials().stream().collect(toMap(PushbulletCredential::id, PushbulletCredential::token));
  }

  @Override
  public Set<String> getServiceIds() {
    return serviceIdToToken.keySet();
  }

  @Override
  public void sendTextMessageToAllServices(String title, String message) {
    serviceIdToToken.values().forEach(token -> sendTextMessage(token, title, message));
  }

  @Override
  public void sendTextMessageToServiceWithId(String serviceId, String title, String message) {
    sendTextMessage(serviceIdToToken.get(serviceId), title, message);
  }

  private void sendTextMessage(String token, String title, String message) {
    String body = restClient.post()
                            .uri(PUSHBULLET_URI)
                            .header("Access-Token", token)
                            .header(CONTENT_TYPE, APPLICATION_JSON_VALUE)
                            .body(new PushbulletNote(title, message))
                            .retrieve()
                            .body(String.class);
    log.trace(body);
  }

}
