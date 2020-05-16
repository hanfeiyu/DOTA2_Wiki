select json_arrayagg(json_object(
'avg_order_processing_time', agg.avg_order_processing_time
))
from
(
	select avg(sr.order_processing_time) as avg_order_processing_time
	from sales_records sr where sr.region='Asia' and sr.sales_channel='Online'
) agg;





select json_arrayagg(json_object(
'avg_order_processing_time', agg.avg_order_processing_time
))
from
(
	select avg(sr.order_processing_time) as avg_order_processing_time
	from sales_records sr where sr.region='Asia' and sr.sales_channel='Online'
) agg;

