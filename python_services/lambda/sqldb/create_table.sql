create table sales.sales_records
(
    order_id int not null primary key,
    region varchar(100),
    country varchar(80),
    item_type varchar(50),
    sales_channel varchar(10),
    order_priority varchar(10),
    order_date date,
    ship_date date,
    unit_sold int,
    unit_price decimal(7,2),
    unit_cost decimal(7,2),
    total_revenue decimal(12,2),
    total_cost decimal(12,2),
    total_profit decimal(12,2),
    order_processing_time smallint,
    gross_margin decimal(18,18)
) engine=innodb;
