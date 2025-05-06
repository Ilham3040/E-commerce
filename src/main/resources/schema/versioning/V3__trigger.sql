
CREATE OR REPLACE FUNCTION update_order_total_price()
RETURNS TRIGGER AS $$
BEGIN

    UPDATE orders
    SET total_price = (
        SELECT COALESCE(SUM(line_total), 0)
        FROM order_items
        WHERE order_id = NEW.order_id
    ) + NEW.shipping_price + NEW.service_price
    WHERE id = NEW.order_id;

    RETURN NEW;
END;
$$ LANGUAGE plpgsql;


CREATE TRIGGER trigger_update_order_total_price
AFTER INSERT OR UPDATE ON order_items
FOR EACH ROW
EXECUTE FUNCTION update_order_total_price();

CREATE TRIGGER trigger_update_order_total_price_on_order
AFTER UPDATE OF shipping_price, service_price ON orders
FOR EACH ROW
EXECUTE FUNCTION update_order_total_price();



-- Trigger function to update total_reviews in product_detail
CREATE OR REPLACE FUNCTION update_total_reviews()
RETURNS TRIGGER AS $$
BEGIN
    -- Increment total_reviews by 1 when a new review is added
    UPDATE products
    SET product_reviews = (SELECT COUNT(*) FROM product_reviews WHERE product_id = NEW.product_id)
    WHERE product_id = NEW.product_id;

    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

-- Trigger to run after a new review is inserted into product_reviews
CREATE TRIGGER trigger_update_total_reviews
AFTER INSERT ON product_reviews
FOR EACH ROW
EXECUTE FUNCTION update_total_reviews();


-- Trigger function to update the price in product_detail based on the lowest variant price
CREATE OR REPLACE FUNCTION update_product_price()
RETURNS TRIGGER AS $$
BEGIN
    -- Update price in product_detail to match the lowest price of its variants
    UPDATE product_detail
    SET price = (
        SELECT MIN(price) FROM product_variants WHERE product_id = NEW.product_id
    )
    WHERE product_id = NEW.product_id;

    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

-- Trigger to run after an insert or update on product_variants
CREATE TRIGGER trigger_update_product_price
AFTER INSERT OR UPDATE ON product_variants
FOR EACH ROW
EXECUTE FUNCTION update_product_price();

-- Trigger function to decrease stock quantity and increase total_sold in product_detail
CREATE OR REPLACE FUNCTION decrease_stock_and_increase_total_sold()
RETURNS TRIGGER AS $$
BEGIN
    -- Decrease stock quantity based on the quantity ordered
    UPDATE product_variants
    SET stock_quantity = stock_quantity - NEW.quantity
    WHERE product_variants_id = NEW.product_variants_id;

    -- Increase total_sold in product_detail by the quantity ordered
    UPDATE product_detail
    SET total_sold = total_sold + NEW.quantity
    WHERE product_id = NEW.product_id;

    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

-- Trigger to run after an insert on orders
CREATE TRIGGER trigger_decrease_stock_and_increase_total_sold
AFTER INSERT ON orders
FOR EACH ROW
EXECUTE FUNCTION decrease_stock_and_increase_total_sold();
