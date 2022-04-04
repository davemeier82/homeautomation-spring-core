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

package io.github.davemeier82.homeautomation.spring.core.config.device;


import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Map;

import static java.util.Objects.requireNonNullElseGet;

/**
 * Configuration to load and save a Device instance.
 *
 * @author David Meier
 * @since 0.1.0
 */
public record DeviceConfig(String type, String displayName, String id, Map<String, String> parameters, Map<String, String> customIdentifiers) {

  /**
   * Constructor.
   *
   * @param type              the device type
   * @param displayName       the display name
   * @param id                the device id
   * @param parameters        parameters that are mandatory to create the device
   * @param customIdentifiers optional custom identifiers
   */
  @JsonCreator
  public DeviceConfig(@JsonProperty(required = true) String type,
                      String displayName,
                      @JsonProperty(required = true) String id,
                      Map<String, String> parameters,
                      Map<String, String> customIdentifiers
  ) {
    this.type = type;
    this.displayName = displayName;
    this.id = id;
    this.parameters = requireNonNullElseGet(parameters, Map::of);
    this.customIdentifiers = requireNonNullElseGet(customIdentifiers, Map::of);
  }

}
