# homeautomation-spring-core

This is a Spring Boot implementation of the [homeautomation-core](https://github.com/davemeier82/homeautomation-core/blob/main/README.md) home automation framework.

<!-- TOC -->

* [homeautomation-spring-core](#homeautomation-spring-core)
    * [Usage](#usage)
    * [Supported Events](#supported-events)
    * [Push notifications](#push-notifications)
        * [Configuration parameter](#configuration-parameter)
        * [Pushover https://pushover.net/](#pushover-httpspushovernet)
            * [Configuration parameter](#configuration-parameter-1)
        * [Pushbullet https://www.pushbullet.com/](#pushbullet-httpswwwpushbulletcom)
            * [Configuration parameter](#configuration-parameter-2)
    * [Persistence](#persistence)
        * [H2 Database](#h2-database)
        * [Postgres](#postgres)

<!-- TOC -->

## Usage

Checkout the detailed usage in the Demo: [homeautomation-demo](https://github.com/davemeier82/homeautomation-demo/blob/main/README.md)

```xml

<project>
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
</project>
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

## Push notifications

There is support for two Push-Notification services built-in. You can easily add a custom one by implementing the interface `PushNotificationService`.

#### Configuration parameter

| Property                                                                          | Default Value           | Description                                          |
|-----------------------------------------------------------------------------------|-------------------------|------------------------------------------------------|
| homeautomation.spring-core.notification.push.enabled                              | false                   | Enables the push notification support                |
| homeautomation.spring-core.notification.push.default-sender                       | false                   | Enables the default push notification sender         |
| homeautomation.spring-core.notification.push.translation.properties.defaultLocale | en                      | Default locale for the push notification messages    |
| homeautomation.spring-core.notification.push.translation.properties.baseName      | device-property-message | ResourceBundle basename for the message translations |

### Pushover https://pushover.net/

#### Configuration parameter

| Property                                                                | Default Value | Description                                                                  |
|-------------------------------------------------------------------------|---------------|------------------------------------------------------------------------------|
| homeautomation.spring-core.notification.push.pushover.enabled           | false         | Enables the pushbullet support                                               |
| homeautomation.spring-core.notification.push.pushover.credentials       |               | Array of credentials                                                         |
| homeautomation.spring-core.notification.push.pushover.credentials.id    |               | Unique custom ID to identify this push notification service in the framework |
| homeautomation.spring-core.notification.push.pushover.credentials.user  |               | Pushover user                                                                |
| homeautomation.spring-core.notification.push.pushover.credentials.token |               | Pushover token                                                               |          

### Pushbullet https://www.pushbullet.com/

#### Configuration parameter

| Property                                                                  | Default Value | Description                                                                  |
|---------------------------------------------------------------------------|---------------|------------------------------------------------------------------------------|
| homeautomation.spring-core.notification.push.pushbullet.enabled           | false         | Enables the pushbullet support                                               |
| homeautomation.spring-core.notification.push.pushbullet.credentials       |               | Array of credentials                                                         |
| homeautomation.spring-core.notification.push.pushbullet.credentials.id    |               | Unique custom ID to identify this push notification service in the framework |
| homeautomation.spring-core.notification.push.pushbullet.credentials.token |               | Pushover token                                                               |

## Persistence

The configuration of the database is done in the standard way of spring boot.

### H2 Database

This shows an example of an in-memory h2 database (not recommended).

```yaml
spring:
    datasource:
        url: jdbc:h2:file:~/hadb;DATABASE_TO_LOWER=TRUE;DEFAULT_NULL_ORDERING=HIGH
        username: sa
        password: password
        driver-class-name: org.h2.Driver
    jpa:
        open-in-view: off
        database-platform: org.hibernate.dialect.H2Dialect
        properties:
            hibernate:
                globally_quoted_identifiers: true
```

### Postgres

This shows an example of a postgres database.

```yaml
spring:
    datasource:
        url: jdbc:postgresql://192.168.1.100:5432/postgres?currentSchema=ha
        username: postgres
        password: postgres
    jpa:
        open-in-view: off
```