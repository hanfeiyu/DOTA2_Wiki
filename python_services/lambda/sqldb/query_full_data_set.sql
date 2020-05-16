select json_arrayagg(json_object(
'order_id',sr.order_id,
'region',sr.region,
'country',sr.country,
'item_type',sr.item_type,
'sales_channel',sr.sales_channel,
'order_priority',sr.order_priority,
'order_date',sr.order_date,
'ship_date',sr.ship_date,
'unit_sold',sr.unit_sold,
'unit_price',sr.unit_price,
'unit_cost',sr.unit_cost,
'total_revenue',sr.total_revenue,
'total_cost',sr.total_cost,
'total_profit',sr.total_profit,
'order_processing_time',sr.order_processing_time,
'gross_margin',sr.gross_margin
)) as full_data_set 
from sales_records sr 