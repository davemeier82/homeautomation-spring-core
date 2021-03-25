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

package com.github.davemeier82.homeautomation.spring.core.config;


import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class DeviceConfig {

  private final String type;
  private final String displayName;
  private final String id;

  @JsonCreator
  public DeviceConfig(@JsonProperty("type") String type, @JsonProperty("displayName") String displayName, @JsonProperty("id") String id) {
    this.type = type;
    this.displayName = displayName;
    this.id = id;
  }

  public String getType() {
    return type;
  }

  public String getDisplayName() {
    return displayName;
  }

  public String getId() {
    return id;
  }
}
