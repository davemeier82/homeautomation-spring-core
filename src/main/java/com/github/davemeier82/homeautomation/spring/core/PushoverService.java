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

import com.github.davemeier82.homeautomation.core.PushNotificationService;
import org.springframework.web.client.RestOperations;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

public class PushoverService implements PushNotificationService {

  private static final URI PUSHOVER_URI = URI.create("https://api.pushover.net/1/messages.json");
  private final RestOperations restOperations;
  private final String user;
  private final String token;

  public PushoverService(RestOperations restOperations, String user, String token) {
    this.restOperations = restOperations;
    this.user = user;
    this.token = token;
  }

  @Override
  public void sendTextMessage(String title, String message) {
    String uriString = UriComponentsBuilder.newInstance().uri(PUSHOVER_URI)
        .queryParam("token", token)
        .queryParam("user", user)
        .queryParam("message", message)
        .queryParam("title", title)
        .build().toUriString();
    restOperations.postForLocation(uriString, null);
  }

}
