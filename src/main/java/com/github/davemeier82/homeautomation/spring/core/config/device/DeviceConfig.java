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

package com.github.davemeier82.homeautomation.spring.core.config.device;


import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Map;

import static java.util.Objects.requireNonNullElseGet;

public record DeviceConfig(String type, String displayName, String id, Map<String, String> parameters, Map<String, String> customIdentifiers) {

  @JsonCreator
  public DeviceConfig(@JsonProperty(value = "type", required = true) String type,
                      @JsonProperty("displayName") String displayName,
                      @JsonProperty(value = "id", required = true) String id,
                      @JsonProperty("parameters") Map<String, String> parameters,
                      @JsonProperty("customIdentifiers") Map<String, String> customIdentifiers
  ) {
    this.type = type;
    this.displayName = displayName;
    this.id = id;
    this.parameters = requireNonNullElseGet(parameters, Map::of);
    this.customIdentifiers = customIdentifiers;
  }

}
