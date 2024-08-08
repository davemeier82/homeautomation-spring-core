drop view if exists latest_device_property_value;
create view latest_device_property_value
            (device_id, device_type, device_display_name, device_property_id, device_property_type,
             device_property_display_name, "value", device_property_value_type, timestamp)
as
SELECT d.device_id,
       d.device_type,
       d.display_name                                AS device_display_name,
       COALESCE(dp.device_property_id, 'null'::text) AS device_property_id,
       dp.type                                       AS device_property_type,
       dp.display_name                               AS device_property_display_name,
       dpvx."value"                                  AS "value",
       COALESCE(dpv.type, 'null'::text)              AS device_property_value_type,
       dpv.timestamp                                 AS timestamp
FROM (SELECT max(device_property_value.timestamp) AS timestamp,
             device_property_value.device_property_id,
             device_property_value.type
      FROM device_property_value
      GROUP BY device_property_value.device_property_id, device_property_value.type) dpv
         JOIN device_property_value dpvx ON dpv.device_property_id = dpvx.device_property_id AND dpv.timestamp = dpvx.timestamp AND dpv.type = dpvx.type
         RIGHT JOIN device_property dp ON dp.id = dpv.device_property_id
         RIGHT JOIN device d ON dp.device_id = d.id;
