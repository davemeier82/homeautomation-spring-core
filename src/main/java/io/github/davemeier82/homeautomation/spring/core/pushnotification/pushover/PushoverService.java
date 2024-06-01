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

import io.github.davemeier82.homeautomation.core.notification.PushNotificationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.client.RestClient;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;

import static java.util.stream.Collectors.toMap;

public class PushoverService implements PushNotificationService {
  private static final Logger log = LoggerFactory.getLogger(PushoverService.class);
  private static final URI PUSHOVER_URI = URI.create("https://api.pushover.net/1/messages.json");
  private final RestClient restClient;
  private final Map<String, PushoverCredential> idToProperties;


  public PushoverService(RestClient restClient, PushoverConfiguration configuration) {
    this.restClient = restClient;
    idToProperties = configuration.credentials().stream().collect(toMap(PushoverCredential::id, Function.identity()));
  }

  @Override
  public Set<String> getServiceIds() {
    return idToProperties.keySet();
  }

  @Override
  public void sendTextMessageToAllServices(String title, String message) {
    idToProperties.values().forEach(p -> sendTextMessage(p, title, message));
  }

  @Override
  public void sendTextMessageToServiceWithId(String serviceId, String title, String message) {
    sendTextMessage(idToProperties.get(serviceId), title, message);


  }

  private void sendTextMessage(PushoverCredential credential, String title, String message) {
    URI uri = UriComponentsBuilder.newInstance().uri(PUSHOVER_URI)
                                  .queryParam("token", credential.token())
                                  .queryParam("user", credential.user())
                                  .queryParam("message", message)
                                  .queryParam("title", title)
                                  .build().toUri();
    String body = restClient.post()
                            .uri(uri)
                            .retrieve().body(String.class);
    log.trace(body);
  }

}
