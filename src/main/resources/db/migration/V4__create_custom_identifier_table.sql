create table if not exists custom_identifier
(
    device_id uuid not null
        constraint custom_identifier_device_id_fk references device on delete cascade,
    name      text not null,
    "value" text not null,
    constraint custom_identifier_parameter_pk primary key (device_id, name)
);
