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

import com.github.davemeier82.homeautomation.core.PushNotificationService;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class PushNotificationServiceRegistry {

  private final Map<String, PushNotificationService> idToService = new HashMap<>();

  public Set<PushNotificationService> getAll() {
    return new HashSet<>(idToService.values());
  }

  public PushNotificationService getById(String id) {
    return idToService.get(id);
  }

  public void add(String id, PushNotificationService pushNotificationService) {
    idToService.put(id, pushNotificationService);
  }

}
