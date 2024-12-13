insert into event_push_notification_config (id, property_value_type, on_change_only)
values ('020c5121-3cd2-4b33-a16d-c80ff621d27c', 'RelayState', true);

insert into event_push_notification_config (id, property_value_type, on_change_only)
values ('b4929e44-ee4b-4224-a241-1e0e0881e2da', 'RollerState', true);


insert into device (id, device_id, device_type, display_name)
values ('763d87b3-cc38-4787-a32b-4fe62edf4250', 'aaa', 'test', 'bla');
insert into device_property (id, device_id, type, device_property_id, display_name)
values ('4571fbe3-0fc7-4528-a01a-8d1e4d71a9c6', '763d87b3-cc38-4787-a32b-4fe62edf4250', 'xxx', '1', 'bla');

insert into event_push_notification_config (id, property_value_type, on_change_only, device_property_id)
values ('27735576-450c-46f9-8439-d82ef8a67893', 'DimmingLevel', false, '4571fbe3-0fc7-4528-a01a-8d1e4d71a9c6');