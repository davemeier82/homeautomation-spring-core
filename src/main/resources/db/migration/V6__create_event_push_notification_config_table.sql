create table if not exists event_push_notification_config
(
    device_id           uuid
        constraint event_push_notification_config_device_id_fk references device,
    device_property_id  uuid
        constraint event_push_notification_config_device_property_id_fk references device_property,
    property_value_type text,
    on_change_only      boolean not null,
    service_ids         text    not null,
    id                  uuid    not null
        constraint event_push_notification_config_pk primary key
);