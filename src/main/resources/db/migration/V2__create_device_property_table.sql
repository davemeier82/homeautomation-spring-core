create table if not exists device_property
(
    id                 uuid not null
        constraint device_property_pk primary key,
    device_id          uuid not null
        constraint device_property_device_id_fk references device on delete cascade,
    type               text not null,
    device_property_id text not null,
    display_name       text,
    constraint device_property_pk_2 unique (device_id, device_property_id)
);
