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

package io.github.davemeier82.homeautomation.spring.core;

import io.github.davemeier82.homeautomation.core.device.DeviceFactory;
import io.github.davemeier82.homeautomation.core.device.DeviceTypeMapper;
import io.github.davemeier82.homeautomation.core.device.property.DefaultDevicePropertyFactory;
import io.github.davemeier82.homeautomation.core.device.property.DefaultDevicePropertyTypeFactory;
import io.github.davemeier82.homeautomation.core.device.property.DevicePropertyFactory;
import io.github.davemeier82.homeautomation.core.device.property.DevicePropertyTypeFactory;
import io.github.davemeier82.homeautomation.core.device.property.DevicePropertyTypeMapper;
import io.github.davemeier82.homeautomation.core.device.property.DevicePropertyValueTypeFactory;
import io.github.davemeier82.homeautomation.core.notification.EventPushNotificationConfigRepository;
import io.github.davemeier82.homeautomation.core.repositories.DevicePropertyRepository;
import io.github.davemeier82.homeautomation.core.repositories.DevicePropertyValueRepository;
import io.github.davemeier82.homeautomation.core.repositories.DeviceRepository;
import io.github.davemeier82.homeautomation.spring.core.persistence.mapper.DeviceEntityMapper;
import io.github.davemeier82.homeautomation.spring.core.persistence.mapper.DevicePropertyEntityMapper;
import io.github.davemeier82.homeautomation.spring.core.persistence.mapper.DevicePropertyValueEntityMapper;
import io.github.davemeier82.homeautomation.spring.core.persistence.mapper.EventPushNotificationConfigEntityMapper;
import io.github.davemeier82.homeautomation.spring.core.persistence.repository.JpaCustomIdentifierRepository;
import io.github.davemeier82.homeautomation.spring.core.persistence.repository.JpaDeviceParameterRepository;
import io.github.davemeier82.homeautomation.spring.core.persistence.repository.JpaDevicePropertyRepository;
import io.github.davemeier82.homeautomation.spring.core.persistence.repository.JpaDevicePropertyValueRepository;
import io.github.davemeier82.homeautomation.spring.core.persistence.repository.JpaDeviceRepository;
import io.github.davemeier82.homeautomation.spring.core.persistence.repository.JpaEventPushNotificationConfigRepository;
import io.github.davemeier82.homeautomation.spring.core.persistence.repository.SpringDataDevicePropertyRepository;
import io.github.davemeier82.homeautomation.spring.core.persistence.repository.SpringDataDevicePropertyValueRepository;
import io.github.davemeier82.homeautomation.spring.core.persistence.repository.SpringDataDeviceRepository;
import io.github.davemeier82.homeautomation.spring.core.persistence.repository.SpringDataEventPushNotificationConfigRepository;
import net.javacrumbs.shedlock.core.LockProvider;
import net.javacrumbs.shedlock.provider.jdbctemplate.JdbcTemplateLockProvider;
import net.javacrumbs.shedlock.spring.annotation.EnableSchedulerLock;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;
import java.time.Duration;
import java.util.Set;

@Configuration
@AutoConfigureBefore(HomeAutomationCoreAutoConfiguration.class)
@EnableTransactionManagement
@EnableJpaRepositories(basePackages = "io.github.davemeier82.homeautomation.spring.core.persistence.repository")
@ComponentScan(basePackages = "io.github.davemeier82.homeautomation.spring.core.persistence.repository")
@EntityScan("io.github.davemeier82.homeautomation.spring.core.persistence.entity")
@EnableScheduling
@EnableSchedulerLock(defaultLockAtMostFor = "PT30S")
public class HomeAutomationCorePersistenceAutoConfiguration {

  @Bean
  @ConditionalOnMissingBean
  DeviceRepository deviceRepository(JpaDeviceRepository jpaDeviceRepository,
                                    JpaCustomIdentifierRepository jpaCustomIdentifierRepository,
                                    JpaDeviceParameterRepository jpaDeviceParameterRepository,
                                    DeviceEntityMapper deviceEntityMapper,
                                    DeviceTypeMapper deviceTypeMapper
  ) {
    return new SpringDataDeviceRepository(jpaDeviceRepository, jpaCustomIdentifierRepository, jpaDeviceParameterRepository, deviceEntityMapper, deviceTypeMapper);
  }

  @Bean
  @ConditionalOnBean(DeviceRepository.class)
  @ConditionalOnMissingBean
  DevicePropertyRepository devicePropertyRepository(JpaDevicePropertyRepository jpaDevicePropertyRepository,
                                                    JpaDeviceRepository jpaDeviceRepository,
                                                    DevicePropertyEntityMapper devicePropertyEntityMapper,
                                                    DeviceTypeMapper deviceTypeMapper
  ) {
    return new SpringDataDevicePropertyRepository(jpaDevicePropertyRepository, jpaDeviceRepository, devicePropertyEntityMapper, deviceTypeMapper);
  }

  @Bean
  @ConditionalOnMissingBean
  DeviceStatePersistenceHandler deviceStatePersistenceHandler(Set<DevicePropertyValueRepository> devicePropertyValueRepositories) {
    return new DeviceStatePersistenceHandler(devicePropertyValueRepositories);
  }

  @Bean
  @ConditionalOnMissingBean
  DeviceEntityMapper deviceEntityMapper(Set<DeviceFactory> deviceFactories, DeviceTypeMapper deviceTypeMapper) {
    return new DeviceEntityMapper(deviceFactories, deviceTypeMapper);
  }

  @Bean
  @ConditionalOnMissingBean
  DevicePropertyFactory devicePropertyFactory() {
    return new DefaultDevicePropertyFactory();
  }

  @Bean
  @ConditionalOnMissingBean
  DevicePropertyTypeFactory devicePropertyTypeFactory() {
    return new DefaultDevicePropertyTypeFactory();
  }

  @Bean
  @ConditionalOnMissingBean
  DevicePropertyEntityMapper devicePropertyEntityMapper(DevicePropertyFactory devicePropertyFactory,
                                                        DeviceTypeMapper deviceTypeMapper,
                                                        DevicePropertyTypeMapper devicePropertyTypeMapper
  ) {
    return new DevicePropertyEntityMapper(devicePropertyFactory, devicePropertyTypeMapper, deviceTypeMapper);
  }

  @Bean
  @ConditionalOnMissingBean
  EventPushNotificationConfigEntityMapper eventPushNotificationConfigEntityMapper(DeviceTypeMapper deviceTypeMapper, DevicePropertyValueTypeFactory devicePropertyValueTypeFactory) {
    return new EventPushNotificationConfigEntityMapper(deviceTypeMapper, devicePropertyValueTypeFactory);
  }

  @Bean
  @ConditionalOnMissingBean
  EventPushNotificationConfigRepository eventPushNotificationConfigRepository(JpaEventPushNotificationConfigRepository eventPushNotificationConfigRepository,
                                                                              EventPushNotificationConfigEntityMapper eventPushNotificationConfigEntityMapper
  ) {
    return new SpringDataEventPushNotificationConfigRepository(eventPushNotificationConfigRepository, eventPushNotificationConfigEntityMapper);
  }

  @Bean
  @ConditionalOnMissingBean
  DevicePropertyValueEntityMapper devicePropertyValueEntityMapper() {
    return new DevicePropertyValueEntityMapper();
  }

  @Bean
  @ConditionalOnMissingBean
  SpringDataDevicePropertyValueRepository springDataDevicePropertyValueRepository(JpaDevicePropertyValueRepository devicePropertyValueRepository,
                                                                                  JpaDevicePropertyRepository devicePropertyRepository,
                                                                                  DeviceTypeMapper deviceTypeMapper,
                                                                                  DevicePropertyValueEntityMapper devicePropertyValueEntityMapper
  ) {
    return new SpringDataDevicePropertyValueRepository(devicePropertyValueRepository, devicePropertyRepository, deviceTypeMapper, devicePropertyValueEntityMapper);
  }

  @Bean
  @ConditionalOnMissingBean
  public LockProvider lockProvider(DataSource dataSource) {
    return new JdbcTemplateLockProvider(dataSource);
  }

  @Bean
  @ConditionalOnMissingBean
  @ConditionalOnBean(SpringDataDevicePropertyValueRepository.class)
  SpringDataDevicePropertyValueRepositoryHousekeeper springDataDevicePropertyValueRepositoryHousekeeper(SpringDataDevicePropertyValueRepository springDataDevicePropertyValueRepository,
                                                                                                        @Value("${homeautomation.spring-core.device-property-value-repository.clean-up.duration:P90D}")
                                                                                                        Duration deleteOlderThanDuration
  ) {
    return new SpringDataDevicePropertyValueRepositoryHousekeeper(springDataDevicePropertyValueRepository, deleteOlderThanDuration);
  }
}
