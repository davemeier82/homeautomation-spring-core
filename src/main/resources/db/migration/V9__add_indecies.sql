create index device_property_value_device_property_id_type_index
    on device_property_value (device_property_id, type);

create index device_property_value_timestamp_index
    on device_property_value (timestamp);

create index device_property_value_type_index
    on device_property_value (type desc);
