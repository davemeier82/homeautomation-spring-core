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

import io.github.davemeier82.homeautomation.core.PushNotificationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import static org.springframework.http.HttpHeaders.CONTENT_TYPE;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

/**
 * Implementation of {@link PushNotificationService} for Pushbullet (https://www.pushbullet.com/).
 *
 * @author David Meier
 * @since 0.1.0
 */
public class PushbulletService implements PushNotificationService {

  private static final Logger log = LoggerFactory.getLogger(PushbulletService.class);
  private static final String PUSHBULLET_URI = "https://api.pushbullet.com/v2/pushes";
  private final WebClient webClient;
  private final String token;

  /**
   * Constructor.
   *
   * @param webClient the web client for REST calls
   * @param token     the authentication token
   */
  public PushbulletService(WebClient webClient, String token) {
    this.webClient = webClient;
    this.token = token;
  }

  @Override
  public void sendTextMessage(String title, String message) {
    webClient.post()
        .uri(PUSHBULLET_URI)
        .header("Access-Token", token)
        .header(CONTENT_TYPE, APPLICATION_JSON_VALUE)
        .body(Mono.just(new PushbulletNote(title, message)), PushbulletNote.class).retrieve().bodyToMono(String.class).subscribe(log::trace);
  }

}
