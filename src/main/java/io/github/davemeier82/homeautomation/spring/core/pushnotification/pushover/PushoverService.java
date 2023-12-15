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

import io.github.davemeier82.homeautomation.core.PushNotificationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.client.RestClient;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

/**
 * Implementation of {@link PushNotificationService} for Pushover (<a href="https://pushover.net/">pushover.net</a>).
 *
 * @author David Meier
 * @since 0.1.0
 */
public class PushoverService implements PushNotificationService {
  private static final Logger log = LoggerFactory.getLogger(PushoverService.class);
  private static final URI PUSHOVER_URI = URI.create("https://api.pushover.net/1/messages.json");
  private final RestClient restClient;
  private final String user;
  private final String token;

  /**
   * Constructor.
   *
   * @param restClient the rest client for REST calls
   * @param user      username
   * @param token     authentication token
   */
  public PushoverService(RestClient restClient, String user, String token) {
    this.restClient = restClient;
    this.user = user;
    this.token = token;
  }

  @Override
  public void sendTextMessage(String title, String message) {
    URI uri = UriComponentsBuilder.newInstance().uri(PUSHOVER_URI)
        .queryParam("token", token)
        .queryParam("user", user)
        .queryParam("message", message)
        .queryParam("title", title)
        .build().toUri();
    String body = restClient.post()
        .uri(uri)
        .retrieve().body(String.class);
    log.trace(body);
  }

}
