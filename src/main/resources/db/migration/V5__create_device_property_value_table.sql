create table if not exists device_property_value
(
    device_property_id uuid      not null
        constraint device_property_value_device_property_id_fk references device_property on delete cascade,
    "value"            text,
    timestamp          timestamp not null,
    type               text      not null,
    id                 uuid      not null
        constraint device_property_value_pk primary key
);