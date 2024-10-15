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

import io.github.davemeier82.homeautomation.core.device.property.DevicePropertyFactory;
import io.github.davemeier82.homeautomation.core.event.EventPublisher;
import io.github.davemeier82.homeautomation.core.event.factory.EventFactory;
import io.github.davemeier82.homeautomation.core.repositories.DevicePropertyRepository;
import io.github.davemeier82.homeautomation.core.repositories.DevicePropertyValueRepository;
import io.github.davemeier82.homeautomation.core.updater.AlarmStateValueUpdateService;
import io.github.davemeier82.homeautomation.core.updater.BatteryLevelUpdateService;
import io.github.davemeier82.homeautomation.core.updater.CloudBaseValueUpdateService;
import io.github.davemeier82.homeautomation.core.updater.Co2ValueUpdateService;
import io.github.davemeier82.homeautomation.core.updater.DevicePropertyCreator;
import io.github.davemeier82.homeautomation.core.updater.DimmingLevelValueUpdateService;
import io.github.davemeier82.homeautomation.core.updater.HumidityValueUpdateService;
import io.github.davemeier82.homeautomation.core.updater.IlluminanceValueUpdateService;
import io.github.davemeier82.homeautomation.core.updater.LightningCountValueUpdateService;
import io.github.davemeier82.homeautomation.core.updater.LightningDistanceValueUpdateService;
import io.github.davemeier82.homeautomation.core.updater.MotionStateValueUpdateService;
import io.github.davemeier82.homeautomation.core.updater.PowerValueUpdateService;
import io.github.davemeier82.homeautomation.core.updater.PressureValueUpdateService;
import io.github.davemeier82.homeautomation.core.updater.RainIntervalValueUpdateService;
import io.github.davemeier82.homeautomation.core.updater.RainRateValueUpdateService;
import io.github.davemeier82.homeautomation.core.updater.RainTodayValueUpdateService;
import io.github.davemeier82.homeautomation.core.updater.RelayStateValueUpdateService;
import io.github.davemeier82.homeautomation.core.updater.RollerPositionValueUpdateService;
import io.github.davemeier82.homeautomation.core.updater.RollerStateValueUpdateService;
import io.github.davemeier82.homeautomation.core.updater.SmokeStateValueUpdateService;
import io.github.davemeier82.homeautomation.core.updater.TemperatureValueUpdateService;
import io.github.davemeier82.homeautomation.core.updater.UvIndexValueUpdateService;
import io.github.davemeier82.homeautomation.core.updater.WindDirectionValueUpdateService;
import io.github.davemeier82.homeautomation.core.updater.WindGustDirectionValueUpdateService;
import io.github.davemeier82.homeautomation.core.updater.WindGustSpeedValueUpdateService;
import io.github.davemeier82.homeautomation.core.updater.WindRunValueUpdateService;
import io.github.davemeier82.homeautomation.core.updater.WindSpeedValueUpdateService;
import io.github.davemeier82.homeautomation.core.updater.WindowStateValueUpdateService;
import io.github.davemeier82.homeautomation.core.updater.WindowTiltAngleValueUpdateService;
import io.github.davemeier82.homeautomation.core.updater.defaults.DefaultAlarmStateValueUpdateService;
import io.github.davemeier82.homeautomation.core.updater.defaults.DefaultBatteryLevelValueUpdateService;
import io.github.davemeier82.homeautomation.core.updater.defaults.DefaultCloudBaseValueUpdateService;
import io.github.davemeier82.homeautomation.core.updater.defaults.DefaultCo2ValueUpdateService;
import io.github.davemeier82.homeautomation.core.updater.defaults.DefaultDimmingLevelValueUpdateService;
import io.github.davemeier82.homeautomation.core.updater.defaults.DefaultHumidityValueUpdateService;
import io.github.davemeier82.homeautomation.core.updater.defaults.DefaultIlluminanceValueUpdateService;
import io.github.davemeier82.homeautomation.core.updater.defaults.DefaultLightningCountValueUpdateService;
import io.github.davemeier82.homeautomation.core.updater.defaults.DefaultLightningDistanceValueUpdateService;
import io.github.davemeier82.homeautomation.core.updater.defaults.DefaultMotionStateValueUpdateService;
import io.github.davemeier82.homeautomation.core.updater.defaults.DefaultPowerValueUpdateService;
import io.github.davemeier82.homeautomation.core.updater.defaults.DefaultPressureValueUpdateService;
import io.github.davemeier82.homeautomation.core.updater.defaults.DefaultRainIntervalValueUpdateService;
import io.github.davemeier82.homeautomation.core.updater.defaults.DefaultRainRateValueUpdateService;
import io.github.davemeier82.homeautomation.core.updater.defaults.DefaultRainTodayValueUpdateService;
import io.github.davemeier82.homeautomation.core.updater.defaults.DefaultRelayStateValueUpdateService;
import io.github.davemeier82.homeautomation.core.updater.defaults.DefaultRollerPositionValueUpdateService;
import io.github.davemeier82.homeautomation.core.updater.defaults.DefaultRollerStateValueUpdateService;
import io.github.davemeier82.homeautomation.core.updater.defaults.DefaultSmokeStateValueUpdateService;
import io.github.davemeier82.homeautomation.core.updater.defaults.DefaultTemperatureValueUpdateService;
import io.github.davemeier82.homeautomation.core.updater.defaults.DefaultUvIndexValueUpdateService;
import io.github.davemeier82.homeautomation.core.updater.defaults.DefaultWindDirectionValueUpdateService;
import io.github.davemeier82.homeautomation.core.updater.defaults.DefaultWindGustDirectionValueUpdateService;
import io.github.davemeier82.homeautomation.core.updater.defaults.DefaultWindGustSpeedValueUpdateService;
import io.github.davemeier82.homeautomation.core.updater.defaults.DefaultWindRunValueUpdateService;
import io.github.davemeier82.homeautomation.core.updater.defaults.DefaultWindSpeedValueUpdateService;
import io.github.davemeier82.homeautomation.core.updater.defaults.DefaultWindowStateValueUpdateService;
import io.github.davemeier82.homeautomation.core.updater.defaults.DefaultWindowTiltAngleValueUpdateService;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
@AutoConfigureAfter({HomeAutomationCorePersistenceAutoConfiguration.class, HomeAutomationCoreAutoConfiguration.class})
public class HomeAutomationCoreValueUpdateServiceAutoConfiguration {

  @Bean
  @ConditionalOnMissingBean
  DevicePropertyCreator devicePropertyCreator(DevicePropertyRepository devicePropertyRepository,
                                              DevicePropertyFactory devicePropertyFactory,
                                              EventPublisher eventPublisher,
                                              EventFactory eventFactory
  ) {
    return new DevicePropertyCreator(devicePropertyRepository, devicePropertyFactory, eventPublisher, eventFactory);
  }


  @Bean
  @ConditionalOnMissingBean
  @ConditionalOnBean({DevicePropertyValueRepository.class, DevicePropertyCreator.class, EventPublisher.class, EventFactory.class})
  AlarmStateValueUpdateService alarmStateValueUpdateService(DevicePropertyValueRepository devicePropertyValueRepository,
                                                            DevicePropertyCreator devicePropertyCreator,
                                                            EventPublisher eventPublisher,
                                                            EventFactory eventFactory
  ) {
    return new DefaultAlarmStateValueUpdateService(devicePropertyValueRepository, devicePropertyCreator, eventPublisher, eventFactory);
  }

  @Bean
  @ConditionalOnMissingBean
  @ConditionalOnBean({DevicePropertyValueRepository.class, DevicePropertyCreator.class, EventPublisher.class, EventFactory.class})
  BatteryLevelUpdateService batteryLevelUpdateService(DevicePropertyValueRepository devicePropertyValueRepository,
                                                      DevicePropertyCreator devicePropertyCreator,
                                                      EventPublisher eventPublisher,
                                                      EventFactory eventFactory
  ) {
    return new DefaultBatteryLevelValueUpdateService(devicePropertyValueRepository, devicePropertyCreator, eventPublisher, eventFactory);
  }

  @Bean
  @ConditionalOnMissingBean
  @ConditionalOnBean({DevicePropertyValueRepository.class, DevicePropertyCreator.class, EventPublisher.class, EventFactory.class})
  CloudBaseValueUpdateService cloudBaseValueUpdateService(DevicePropertyValueRepository devicePropertyValueRepository,
                                                          DevicePropertyCreator devicePropertyCreator,
                                                          EventPublisher eventPublisher,
                                                          EventFactory eventFactory
  ) {
    return new DefaultCloudBaseValueUpdateService(devicePropertyValueRepository, devicePropertyCreator, eventPublisher, eventFactory);
  }

  @Bean
  @ConditionalOnMissingBean
  @ConditionalOnBean({DevicePropertyValueRepository.class, DevicePropertyCreator.class, EventPublisher.class, EventFactory.class})
  Co2ValueUpdateService co2ValueUpdateService(DevicePropertyValueRepository devicePropertyValueRepository,
                                              DevicePropertyCreator devicePropertyCreator,
                                              EventPublisher eventPublisher,
                                              EventFactory eventFactory
  ) {
    return new DefaultCo2ValueUpdateService(devicePropertyValueRepository, devicePropertyCreator, eventPublisher, eventFactory);
  }

  @Bean
  @ConditionalOnMissingBean
  @ConditionalOnBean({DevicePropertyValueRepository.class, DevicePropertyCreator.class, EventPublisher.class, EventFactory.class})
  DimmingLevelValueUpdateService dimmingLevelValueUpdateService(DevicePropertyValueRepository devicePropertyValueRepository,
                                                                DevicePropertyCreator devicePropertyCreator,
                                                                EventPublisher eventPublisher,
                                                                EventFactory eventFactory
  ) {
    return new DefaultDimmingLevelValueUpdateService(devicePropertyValueRepository, devicePropertyCreator, eventPublisher, eventFactory);
  }

  @Bean
  @ConditionalOnMissingBean
  @ConditionalOnBean({DevicePropertyValueRepository.class, DevicePropertyCreator.class, EventPublisher.class, EventFactory.class})
  HumidityValueUpdateService humidityValueUpdateService(DevicePropertyValueRepository devicePropertyValueRepository,
                                                        DevicePropertyCreator devicePropertyCreator,
                                                        EventPublisher eventPublisher,
                                                        EventFactory eventFactory
  ) {
    return new DefaultHumidityValueUpdateService(devicePropertyValueRepository, devicePropertyCreator, eventPublisher, eventFactory);
  }

  @Bean
  @ConditionalOnMissingBean
  @ConditionalOnBean({DevicePropertyValueRepository.class, DevicePropertyCreator.class, EventPublisher.class, EventFactory.class})
  IlluminanceValueUpdateService illuminanceValueUpdateService(DevicePropertyValueRepository devicePropertyValueRepository,
                                                              DevicePropertyCreator devicePropertyCreator,
                                                              EventPublisher eventPublisher,
                                                              EventFactory eventFactory
  ) {
    return new DefaultIlluminanceValueUpdateService(devicePropertyValueRepository, devicePropertyCreator, eventPublisher, eventFactory);
  }

  @Bean
  @ConditionalOnMissingBean
  @ConditionalOnBean({DevicePropertyValueRepository.class, DevicePropertyCreator.class, EventPublisher.class, EventFactory.class})
  MotionStateValueUpdateService motionStateValueUpdateService(DevicePropertyValueRepository devicePropertyValueRepository,
                                                              DevicePropertyCreator devicePropertyCreator,
                                                              EventPublisher eventPublisher,
                                                              EventFactory eventFactory
  ) {
    return new DefaultMotionStateValueUpdateService(devicePropertyValueRepository, devicePropertyCreator, eventPublisher, eventFactory);
  }

  @Bean
  @ConditionalOnMissingBean
  @ConditionalOnBean({DevicePropertyValueRepository.class, DevicePropertyCreator.class, EventPublisher.class, EventFactory.class})
  PowerValueUpdateService powerValueUpdateService(DevicePropertyValueRepository devicePropertyValueRepository,
                                                  DevicePropertyCreator devicePropertyCreator,
                                                  EventPublisher eventPublisher,
                                                  EventFactory eventFactory
  ) {
    return new DefaultPowerValueUpdateService(devicePropertyValueRepository, devicePropertyCreator, eventPublisher, eventFactory);
  }

  @Bean
  @ConditionalOnMissingBean
  @ConditionalOnBean({DevicePropertyValueRepository.class, DevicePropertyCreator.class, EventPublisher.class, EventFactory.class})
  PressureValueUpdateService pressureValueUpdateService(DevicePropertyValueRepository devicePropertyValueRepository,
                                                        DevicePropertyCreator devicePropertyCreator,
                                                        EventPublisher eventPublisher,
                                                        EventFactory eventFactory
  ) {
    return new DefaultPressureValueUpdateService(devicePropertyValueRepository, devicePropertyCreator, eventPublisher, eventFactory);
  }

  @Bean
  @ConditionalOnMissingBean
  @ConditionalOnBean({DevicePropertyValueRepository.class, DevicePropertyCreator.class, EventPublisher.class, EventFactory.class})
  RainIntervalValueUpdateService rainIntervalValueUpdateService(DevicePropertyValueRepository devicePropertyValueRepository,
                                                                DevicePropertyCreator devicePropertyCreator,
                                                                EventPublisher eventPublisher,
                                                                EventFactory eventFactory
  ) {
    return new DefaultRainIntervalValueUpdateService(devicePropertyValueRepository, devicePropertyCreator, eventPublisher, eventFactory);
  }

  @Bean
  @ConditionalOnMissingBean
  @ConditionalOnBean({DevicePropertyValueRepository.class, DevicePropertyCreator.class, EventPublisher.class, EventFactory.class})
  RainRateValueUpdateService rainRateValueUpdateService(DevicePropertyValueRepository devicePropertyValueRepository,
                                                        DevicePropertyCreator devicePropertyCreator,
                                                        EventPublisher eventPublisher,
                                                        EventFactory eventFactory
  ) {
    return new DefaultRainRateValueUpdateService(devicePropertyValueRepository, devicePropertyCreator, eventPublisher, eventFactory);
  }

  @Bean
  @ConditionalOnMissingBean
  @ConditionalOnBean({DevicePropertyValueRepository.class, DevicePropertyCreator.class, EventPublisher.class, EventFactory.class})
  RainTodayValueUpdateService rainTodayValueUpdateService(DevicePropertyValueRepository devicePropertyValueRepository,
                                                          DevicePropertyCreator devicePropertyCreator,
                                                          EventPublisher eventPublisher,
                                                          EventFactory eventFactory
  ) {
    return new DefaultRainTodayValueUpdateService(devicePropertyValueRepository, devicePropertyCreator, eventPublisher, eventFactory);
  }

  @Bean
  @ConditionalOnMissingBean
  @ConditionalOnBean({DevicePropertyValueRepository.class, DevicePropertyCreator.class, EventPublisher.class, EventFactory.class})
  RelayStateValueUpdateService relayStateValueUpdateService(DevicePropertyValueRepository devicePropertyValueRepository,
                                                            DevicePropertyCreator devicePropertyCreator,
                                                            EventPublisher eventPublisher,
                                                            EventFactory eventFactory
  ) {
    return new DefaultRelayStateValueUpdateService(devicePropertyValueRepository, devicePropertyCreator, eventPublisher, eventFactory);
  }

  @Bean
  @ConditionalOnMissingBean
  @ConditionalOnBean({DevicePropertyValueRepository.class, DevicePropertyCreator.class, EventPublisher.class, EventFactory.class})
  RollerPositionValueUpdateService rollerPositionValueUpdateService(DevicePropertyValueRepository devicePropertyValueRepository,
                                                                    DevicePropertyCreator devicePropertyCreator,
                                                                    EventPublisher eventPublisher,
                                                                    EventFactory eventFactory
  ) {
    return new DefaultRollerPositionValueUpdateService(devicePropertyValueRepository, devicePropertyCreator, eventPublisher, eventFactory);
  }

  @Bean
  @ConditionalOnMissingBean
  @ConditionalOnBean({DevicePropertyValueRepository.class, DevicePropertyCreator.class, EventPublisher.class, EventFactory.class})
  RollerStateValueUpdateService rollerStateValueUpdateService(DevicePropertyValueRepository devicePropertyValueRepository,
                                                              DevicePropertyCreator devicePropertyCreator,
                                                              EventPublisher eventPublisher,
                                                              EventFactory eventFactory
  ) {
    return new DefaultRollerStateValueUpdateService(devicePropertyValueRepository, devicePropertyCreator, eventPublisher, eventFactory);
  }

  @Bean
  @ConditionalOnMissingBean
  @ConditionalOnBean({DevicePropertyValueRepository.class, DevicePropertyCreator.class, EventPublisher.class, EventFactory.class})
  SmokeStateValueUpdateService smokeStateValueUpdateService(DevicePropertyValueRepository devicePropertyValueRepository,
                                                            DevicePropertyCreator devicePropertyCreator,
                                                            EventPublisher eventPublisher,
                                                            EventFactory eventFactory
  ) {
    return new DefaultSmokeStateValueUpdateService(devicePropertyValueRepository, devicePropertyCreator, eventPublisher, eventFactory);
  }

  @Bean
  @ConditionalOnMissingBean
  @ConditionalOnBean({DevicePropertyValueRepository.class, DevicePropertyCreator.class, EventPublisher.class, EventFactory.class})
  TemperatureValueUpdateService temperatureValueUpdateService(DevicePropertyValueRepository devicePropertyValueRepository,
                                                              DevicePropertyCreator devicePropertyCreator,
                                                              EventPublisher eventPublisher,
                                                              EventFactory eventFactory
  ) {
    return new DefaultTemperatureValueUpdateService(devicePropertyValueRepository, devicePropertyCreator, eventPublisher, eventFactory);
  }

  @Bean
  @ConditionalOnMissingBean
  @ConditionalOnBean({DevicePropertyValueRepository.class, DevicePropertyCreator.class, EventPublisher.class, EventFactory.class})
  UvIndexValueUpdateService uvIndexValueUpdateService(DevicePropertyValueRepository devicePropertyValueRepository,
                                                      DevicePropertyCreator devicePropertyCreator,
                                                      EventPublisher eventPublisher,
                                                      EventFactory eventFactory
  ) {
    return new DefaultUvIndexValueUpdateService(devicePropertyValueRepository, devicePropertyCreator, eventPublisher, eventFactory);
  }

  @Bean
  @ConditionalOnMissingBean
  @ConditionalOnBean({DevicePropertyValueRepository.class, DevicePropertyCreator.class, EventPublisher.class, EventFactory.class})
  WindDirectionValueUpdateService windDirectionValueUpdateService(DevicePropertyValueRepository devicePropertyValueRepository,
                                                                  DevicePropertyCreator devicePropertyCreator,
                                                                  EventPublisher eventPublisher,
                                                                  EventFactory eventFactory
  ) {
    return new DefaultWindDirectionValueUpdateService(devicePropertyValueRepository, devicePropertyCreator, eventPublisher, eventFactory);
  }

  @Bean
  @ConditionalOnMissingBean
  @ConditionalOnBean({DevicePropertyValueRepository.class, DevicePropertyCreator.class, EventPublisher.class, EventFactory.class})
  WindGustDirectionValueUpdateService windGustDirectionValueUpdateService(DevicePropertyValueRepository devicePropertyValueRepository,
                                                                          DevicePropertyCreator devicePropertyCreator,
                                                                          EventPublisher eventPublisher,
                                                                          EventFactory eventFactory
  ) {
    return new DefaultWindGustDirectionValueUpdateService(devicePropertyValueRepository, devicePropertyCreator, eventPublisher, eventFactory);
  }

  @Bean
  @ConditionalOnMissingBean
  @ConditionalOnBean({DevicePropertyValueRepository.class, DevicePropertyCreator.class, EventPublisher.class, EventFactory.class})
  WindGustSpeedValueUpdateService windGustSpeedValueUpdateService(DevicePropertyValueRepository devicePropertyValueRepository,
                                                                  DevicePropertyCreator devicePropertyCreator,
                                                                  EventPublisher eventPublisher,
                                                                  EventFactory eventFactory
  ) {
    return new DefaultWindGustSpeedValueUpdateService(devicePropertyValueRepository, devicePropertyCreator, eventPublisher, eventFactory);
  }

  @Bean
  @ConditionalOnMissingBean
  @ConditionalOnBean({DevicePropertyValueRepository.class, DevicePropertyCreator.class, EventPublisher.class, EventFactory.class})
  WindowStateValueUpdateService windowStateValueUpdateService(DevicePropertyValueRepository devicePropertyValueRepository,
                                                              DevicePropertyCreator devicePropertyCreator,
                                                              EventPublisher eventPublisher,
                                                              EventFactory eventFactory
  ) {
    return new DefaultWindowStateValueUpdateService(devicePropertyValueRepository, devicePropertyCreator, eventPublisher, eventFactory);
  }

  @Bean
  @ConditionalOnMissingBean
  @ConditionalOnBean({DevicePropertyValueRepository.class, DevicePropertyCreator.class, EventPublisher.class, EventFactory.class})
  WindowTiltAngleValueUpdateService windowTiltAngleValueUpdateService(DevicePropertyValueRepository devicePropertyValueRepository,
                                                                      DevicePropertyCreator devicePropertyCreator,
                                                                      EventPublisher eventPublisher,
                                                                      EventFactory eventFactory
  ) {
    return new DefaultWindowTiltAngleValueUpdateService(devicePropertyValueRepository, devicePropertyCreator, eventPublisher, eventFactory);
  }

  @Bean
  @ConditionalOnMissingBean
  @ConditionalOnBean({DevicePropertyValueRepository.class, DevicePropertyCreator.class, EventPublisher.class, EventFactory.class})
  WindRunValueUpdateService windRunValueUpdateService(DevicePropertyValueRepository devicePropertyValueRepository,
                                                      DevicePropertyCreator devicePropertyCreator,
                                                      EventPublisher eventPublisher,
                                                      EventFactory eventFactory
  ) {
    return new DefaultWindRunValueUpdateService(devicePropertyValueRepository, devicePropertyCreator, eventPublisher, eventFactory);
  }

  @Bean
  @ConditionalOnMissingBean
  @ConditionalOnBean({DevicePropertyValueRepository.class, DevicePropertyCreator.class, EventPublisher.class, EventFactory.class})
  WindSpeedValueUpdateService windSpeedValueUpdateService(DevicePropertyValueRepository devicePropertyValueRepository,
                                                          DevicePropertyCreator devicePropertyCreator,
                                                          EventPublisher eventPublisher,
                                                          EventFactory eventFactory
  ) {
    return new DefaultWindSpeedValueUpdateService(devicePropertyValueRepository, devicePropertyCreator, eventPublisher, eventFactory);
  }

  @Bean
  @ConditionalOnMissingBean
  @ConditionalOnBean({DevicePropertyValueRepository.class, DevicePropertyCreator.class, EventPublisher.class, EventFactory.class})
  LightningCountValueUpdateService lightningCountValueUpdateService(DevicePropertyValueRepository devicePropertyValueRepository,
                                                                    DevicePropertyCreator devicePropertyCreator,
                                                                    EventPublisher eventPublisher,
                                                                    EventFactory eventFactory
  ) {
    return new DefaultLightningCountValueUpdateService(devicePropertyValueRepository, devicePropertyCreator, eventPublisher, eventFactory);
  }

  @Bean
  @ConditionalOnMissingBean
  @ConditionalOnBean({DevicePropertyValueRepository.class, DevicePropertyCreator.class, EventPublisher.class, EventFactory.class})
  LightningDistanceValueUpdateService lightningDistanceValueUpdateService(DevicePropertyValueRepository devicePropertyValueRepository,
                                                                          DevicePropertyCreator devicePropertyCreator,
                                                                          EventPublisher eventPublisher,
                                                                          EventFactory eventFactory
  ) {
    return new DefaultLightningDistanceValueUpdateService(devicePropertyValueRepository, devicePropertyCreator, eventPublisher, eventFactory);
  }

}
