/*
 * Copyright 2021-2024 the original author or authors.
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

package io.github.davemeier82.homeautomation.spring.core.persistence.mapper;

import io.github.davemeier82.homeautomation.core.device.property.DevicePropertyValueType;
import io.github.davemeier82.homeautomation.core.event.DataWithTimestamp;
import io.github.davemeier82.homeautomation.spring.core.persistence.entity.DevicePropertyValueEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.time.OffsetDateTime;
import java.util.Optional;
import java.util.UUID;

import static java.util.Objects.requireNonNull;

public class DevicePropertyValueEntityMapper {
  private static final Logger log = LoggerFactory.getLogger(DevicePropertyValueEntityMapper.class);

  static <T> T cast(String value, Class<T> clazz) {

    if (value == null) {
      return null;
    }

    if (value.getClass().equals(clazz)) {
      return (T) value;
    } else if (clazz.equals(Long.class)) {
      return (T) Long.valueOf(value);
    } else if (clazz.equals(Integer.class)) {
      return (T) Integer.valueOf(value);
    } else if (clazz.equals(Float.class)) {
      return (T) Float.valueOf(value);
    } else if (clazz.equals(Double.class)) {
      return (T) Double.valueOf(value);
    } else if (clazz.equals(Short.class)) {
      return (T) Short.valueOf(value);
    } else if (clazz.equals(Byte.class)) {
      return (T) Byte.valueOf(value);
    } else if (clazz.equals(Boolean.class)) {
      return (T) Boolean.valueOf(value);
    } else if (clazz.isEnum()) {
      try {
        Method valueOf = clazz.getMethod("valueOf", String.class);
        Object e = valueOf.invoke(null, value);
        return (T) e;
      } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
        log.error("cast from {} to {} is not supported", value.getClass(), clazz);
      }
    }

    log.error("cast from {} to {} is not supported", value.getClass(), clazz);
    return null;
  }

  public DevicePropertyValueEntity map(UUID devicePropertyId, DevicePropertyValueType devicePropertyValueType, Object value, OffsetDateTime time) {
    return new DevicePropertyValueEntity(devicePropertyId, devicePropertyValueType.getTypeName(), value == null ? null : value.toString(),
        time);
  }

  public <T> Optional<DataWithTimestamp<T>> map(DevicePropertyValueEntity entity, Class<T> clazz) {
    T mapped = cast(entity.getValue(), clazz);
    return Optional.of(new DataWithTimestamp<>(requireNonNull(entity.getTimestamp()), mapped));
  }
}
