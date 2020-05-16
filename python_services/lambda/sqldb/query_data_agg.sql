select 
	avg(sr.order_processing_time) as avg_order_processing_time,
	avg(sr.gross_margin) as avg_gross_margin,
	avg(sr.unit_sold) as avg_unit_sold,
	max(sr.unit_sold) as max_unit_sold,
	min(sr.unit_sold) as min_unit_sold,
	sum(sr.unit_sold) as total_unit_sold,
	sum(sr.total_revenue) as total_total_revenue,
	sum(sr.total_profit) as total_total_profit,
	count(*) as total_orders 
from sales_records sr 