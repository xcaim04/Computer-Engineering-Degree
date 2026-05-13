DO $$
DECLARE
    cur_sales CURSOR FOR
        SELECT quantity, price FROM sales;
    v_quantity NUMERIC;
    v_price NUMERIC;
    total_sales NUMERIC := 0;
BEGIN
    OPEN cur_sales;
    LOOP
        FETCH cur_sales INTO v_quantity, v_price;
        EXIT WHEN NOT FOUND;
        total_sales := total_sales + (v_quantity * v_price);
    END LOOP;
    CLOSE cur_sales;
    RAISE LOG 'Total sales: %', total_sales;
END $$;