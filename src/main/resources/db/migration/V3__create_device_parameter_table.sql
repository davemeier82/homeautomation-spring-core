create table if not exists device_parameter
(
    device_id uuid not null
        constraint device_parameter_device_id_fk references device on delete cascade,
    name      text not null,
    "value"   text not null,
    constraint device_parameter_pk primary key (device_id, name)
);
