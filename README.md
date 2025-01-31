# homeautomation-spring-core

This is a Spring Boot implementation of the [homeautomation-core](https://github.com/davemeier82/homeautomation-core/blob/main/README.md) home automation framework.

## Usage

Checkout the detailed usage in the Demo: [homeautomation-demo](https://github.com/davemeier82/homeautomation-demo/blob/main/README.md)

```xml

<dependencyManagement>
    <dependencies>
        <dependency>
            <groupId>io.github.davemeier82.homeautomation</groupId>
            <artifactId>homeautomation-bom</artifactId>
            <version>${homeautomation-bom.version}</version>
            <scope>import</scope>
            <type>pom</type>
        </dependency>
    </dependencies>
</dependencyManagement>

<dependencies>
<dependency>
    <groupId>io.github.davemeier82.homeautomation</groupId>
    <artifactId>homeautomation-spring-core</artifactId>
</dependency>
</dependencies>
```

## Supported Events

| Event                             | Description                           | Unit                                    | 
|-----------------------------------|---------------------------------------|-----------------------------------------|
| AlarmStateChanged/Updated         | Alarm (Smoke / Burglar)               | OFF, PRE_ALARM, FIRE, BURGLAR, SILENCED | 
| BatteryLevelChanged/Updated       | Battery charge level                  | Percent [0-100]                         | 
| CloudBaseChanged/Updated          | Cloud base                            | meter                                   | 
| Co2LevelChanged/Updated           | CO2 Level                             | ppm                                     | 
| DimmingLevelChanged/Updated       | Dimming of light                      | Percent [0-100]                         | 
| HumidityChanged/Updated           | Relative Humidity                     | Percent [0-100]                         | 
| IlluminanceChanged/Updated        | Illumination                          | lux                                     | 
| LightningDistanceChanged/Updated  | Lighting distance                     | meter                                   | 
| LightningCountChanged/Updated     | Lightning count of day                | integer                                 | 
| MotionChanged/Updated             | Motion detection                      | true, false                             | 
| PowerChanged/Updated              | Power                                 | watt                                    | 
| PressureChanged/Updated           | Pressure (e.g. air)                   | millibar                                | 
| RainIntervalAmountChanged/Updated | Rain amount in defined interval       | millimeter per interval                 | 
| RainRateChanged/Updated           | Rain rate                             | millimeter per hour                     | 
| RainTodayAmountChanged/Updated    | Rain amount today                     | millimeter today                        | 
| RainStateChanged/Updated          | Rain state                            | true, false (on/off)                    | 
| RelayStateChanged/Updated         | Relay state                           | true, false (on/off)                    | 
| RollerPositionChanged/Updated     | Roller postion                        | Percent [0 (closed) - 100 (open)]       | 
| RollerStateChanged/Updated        | Roller state                          | OPENING, CLOSING, IDLE                  | 
| SmokeStateChanged/Updated         | Smoke state                           | true, false                             | 
| TemperatureChanged/Updated        | Temperature                           | degree                                  | 
| UvIndexChanged/Updated            | UV Index                              | integer                                 | 
| WindDirectionChanged/Updated      | Wind direction                        | degree                                  | 
| WindGustDirectionChanged/Updated  | Wind gust direction                   | degree                                  | 
| WindSpeedChanged/Updated          | Wind speed                            | km/h                                    | 
| WindGustSpeedChanged/Updated      | Wind gust speed                       | km/h                                    | 
| WindowStateChanged/Updated        | Wind state                            | true, false (on/off)                    | 
| WindRunChanged/Updated            | Wind run today                        | kilometer                               | 
| WindowTiltAngleChanged/Updated    | Window tilt angle                     | degree                                  | 
| MqttClientConnected               | Connection to Mqtt Broker established |                                         | 
| NewDevicePropertyCreatedEvent     | New device property got created       |                                         | 
