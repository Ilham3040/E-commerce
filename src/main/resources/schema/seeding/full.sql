DROP TABLE IF EXISTS orders,
                  order_items,
                  product_details,
                  product_detail_attachment_urls, 
                  product_review_attachment_urls, 
                  product_reviews, 
                  product_variants, 
                  products, 
                  shipment, 
                  shipment_vendor, 
                  store_categories, 
                  store_categories_item, 
                  store_detail_attachment_urls, 
                  store_details, 
                  store_role, 
                  stores, 
                  user_cart, 
                  user_favorite, 
                  users 
CASCADE;

DROP FUNCTION IF EXISTS decrease_stock_and_increase_total_sold,
                        update_order_total_price,
                        update_product_price,
                        update_total_reviews
CASCADE;



CREATE TABLE users (
    id SERIAL PRIMARY KEY,
    username VARCHAR(255) NOT NULL,
    email VARCHAR(255) UNIQUE NOT NULL,
    phoneNumber VARCHAR(16),
    created_at TIMESTAMPTZ DEFAULT NOW(),
    updated_at TIMESTAMPTZ DEFAULT NOW(),
    deleted_at TIMESTAMPTZ,
    is_deleted BOOLEAN DEFAULT FALSE NOT NULL
);

CREATE TABLE stores (
    id SERIAL PRIMARY KEY,
    store_name VARCHAR(255) NOT NULL,
    user_id INT NOT NULL REFERENCES users(id) ON DELETE SET NULL,
    created_at TIMESTAMPTZ DEFAULT NOW(),
    updated_at TIMESTAMPTZ DEFAULT NOW(),
    deleted_at TIMESTAMPTZ,
    is_deleted BOOLEAN DEFAULT FALSE NOT NULL
);


CREATE TABLE products (
    id SERIAL PRIMARY KEY,
    product_name VARCHAR(255) NOT NULL,
    price DECIMAL(10, 2) DEFAULT 0,
    store_id INT NOT NULL REFERENCES stores(id) ON DELETE CASCADE,
    total_reviews INT DEFAULT 0,
    created_at TIMESTAMPTZ DEFAULT NOW(),
    updated_at TIMESTAMPTZ DEFAULT NOW(),
    deleted_at TIMESTAMPTZ,
    is_deleted BOOLEAN DEFAULT FALSE NOT NULL
);

CREATE TABLE orders (
    id SERIAL PRIMARY KEY,
    user_id INT NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    product_id INT NOT NULL REFERENCES products(id) ON DELETE CASCADE,
    order_date TIMESTAMPTZ DEFAULT NOW(),
    shipping_price DECIMAL(10,2) NOT NULL DEFAULT 0.00 CHECK (shipping_price >= 0),
    service_price  DECIMAL(10,2) NOT NULL DEFAULT 0.00 CHECK (service_price  >= 0),
    total_price    DECIMAL(10,2) NOT NULL DEFAULT 0.00 CHECK (total_price   >= 0),
    status VARCHAR(50) DEFAULT 'pending'
);


CREATE TABLE product_details (
    id SERIAL PRIMARY KEY,
    product_id INT NOT NULL REFERENCES products(id) ON DELETE CASCADE,
    description TEXT,
    attachment_urls TEXT[],
    total_sold INT,
    review_rating DECIMAL(3,2),
    created_at TIMESTAMPTZ DEFAULT NOW(),
    updated_at TIMESTAMPTZ DEFAULT NOW(),
    deleted_at TIMESTAMPTZ,
    is_deleted BOOLEAN DEFAULT FALSE NOT NULL
);


CREATE TABLE product_reviews (
    id SERIAL PRIMARY KEY,
    product_id INT NOT NULL REFERENCES products(id) ON DELETE CASCADE,
    user_id INT NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    star_rating INT CHECK (star_rating >= 1 AND star_rating <= 5),
    attachment_urls TEXT[],
    description TEXT,
    created_at TIMESTAMPTZ DEFAULT NOW(),
    updated_at TIMESTAMPTZ DEFAULT NOW(),
    deleted_at TIMESTAMPTZ,
    is_deleted BOOLEAN DEFAULT FALSE NOT NULL
);


CREATE TABLE product_variants (
    id SERIAL PRIMARY KEY,
    product_id INT NOT NULL REFERENCES products(id) ON DELETE CASCADE,
    variant_name VARCHAR(255),
    price DECIMAL(10, 2) NOT NULL,
    stock_quantity INT DEFAULT 0,
    total_sold INT DEFAULT 0,
    created_at TIMESTAMPTZ DEFAULT NOW(),
    updated_at TIMESTAMPTZ DEFAULT NOW(),
    deleted_at TIMESTAMPTZ,
    is_deleted BOOLEAN DEFAULT FALSE NOT NULL
);


CREATE TABLE store_details (
    id SERIAL PRIMARY KEY,
    store_id INT NOT NULL REFERENCES stores(id) ON DELETE CASCADE,
    address TEXT,
    total_review INT DEFAULT 0,
    total_products INT,
    description TEXT,
    attachment_urls TEXT[],
    follower_count INT DEFAULT 0,
    created_at TIMESTAMPTZ DEFAULT NOW(),
    updated_at TIMESTAMPTZ DEFAULT NOW(),
    deleted_at TIMESTAMPTZ,
    is_deleted BOOLEAN DEFAULT FALSE NOT NULL
);


CREATE TABLE store_categories (
    id SERIAL PRIMARY KEY,
    store_id INT NOT NULL REFERENCES stores(id) ON DELETE CASCADE,
    category_name VARCHAR(255) NOT NULL,
    created_at TIMESTAMPTZ DEFAULT NOW(),
    updated_at TIMESTAMPTZ DEFAULT NOW(),
    deleted_at TIMESTAMPTZ,
    is_deleted BOOLEAN DEFAULT FALSE NOT NULL
);


CREATE TABLE store_categories_item (
    category_id INT NOT NULL REFERENCES store_categories(id) ON DELETE CASCADE,
    product_id INT NOT NULL REFERENCES products(id) ON DELETE CASCADE,
    PRIMARY KEY (category_id, product_id),
    created_at TIMESTAMPTZ DEFAULT NOW(),
    updated_at TIMESTAMPTZ DEFAULT NOW(),
    deleted_at TIMESTAMPTZ,
    is_deleted BOOLEAN DEFAULT FALSE NOT NULL
);


CREATE TABLE store_role (
    user_id INT NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    store_id INT NOT NULL REFERENCES stores(id) ON DELETE CASCADE,
    role VARCHAR(50) CHECK (role IN ('owner', 'admin', 'null')) DEFAULT 'null',
    PRIMARY KEY (user_id, store_id),
    created_at TIMESTAMPTZ DEFAULT NOW(),
    updated_at TIMESTAMPTZ DEFAULT NOW(),
    deleted_at TIMESTAMPTZ
);


CREATE TABLE user_cart (
    user_id INT NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    product_id INT NOT NULL REFERENCES products(id) ON DELETE CASCADE,
    PRIMARY KEY (user_id, product_id),
    quantity INT DEFAULT 1
);


CREATE TABLE user_favorite (
    user_id INT NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    product_id INT NOT NULL REFERENCES products(id) ON DELETE CASCADE,
    PRIMARY KEY (user_id, product_id)
);


CREATE TABLE shipment_vendor (
    id SERIAL PRIMARY KEY,
    vendor_name VARCHAR(255) NOT NULL,
    vendor_contact VARCHAR(255),
    vendor_email VARCHAR(255) UNIQUE,
    official_website_url TEXT,
    created_at TIMESTAMPTZ DEFAULT NOW(),
    updated_at TIMESTAMPTZ DEFAULT NOW(),
    deleted_at TIMESTAMPTZ,
    is_deleted BOOLEAN DEFAULT FALSE NOT NULL
);


CREATE TABLE shipment (
    vendor_id INT NOT NULL REFERENCES shipment_vendor(id) ON DELETE CASCADE,
    order_id INT NOT NULL REFERENCES orders(id) ON DELETE CASCADE,
    shipment_status INT CHECK (shipment_status >= 1 AND shipment_status <= 16),
    created_at TIMESTAMPTZ DEFAULT NOW(),
    updated_at TIMESTAMPTZ DEFAULT NOW(),
    PRIMARY KEY (vendor_id, order_id)
);

CREATE TABLE order_items (
    order_id INT NOT NULL REFERENCES orders(id) ON DELETE CASCADE,
    product_id INT NOT NULL REFERENCES products(id) ON DELETE CASCADE,
    product_variants_id INT NOT NULL REFERENCES product_variants(id) ON DELETE CASCADE,
    unit_price DECIMAL(10, 2) NOT NULL CHECK(unit_price > 0),
    quantity INT NOT NULL CHECK(quantity > 0),
    line_total DECIMAL(10,2) GENERATED ALWAYS AS (quantity * unit_price) STORED,
    PRIMARY KEY (order_id)
);



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

-- Trigger to update total price in orders after an order item is inserted or updated
CREATE TRIGGER trigger_update_order_total_price
AFTER INSERT OR UPDATE ON order_items
FOR EACH ROW
EXECUTE FUNCTION update_order_total_price();

-- Trigger to update total price in orders after shipping or service price is updated
CREATE TRIGGER trigger_update_order_total_price_on_order
AFTER UPDATE OF shipping_price, service_price ON orders
FOR EACH ROW
EXECUTE FUNCTION update_order_total_price();




CREATE OR REPLACE FUNCTION update_total_reviews()
RETURNS TRIGGER AS $$
BEGIN
    -- Increment total_reviews by 1 when a new review is added
    UPDATE products
    SET total_reviews = (SELECT COUNT(*) FROM product_reviews WHERE id = NEW.product_id)
    WHERE id = NEW.product_id;

    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

-- Trigger to update the total reviews after a new product review is inserted
CREATE TRIGGER trigger_update_total_reviews
AFTER INSERT ON product_reviews
FOR EACH ROW
EXECUTE FUNCTION update_total_reviews();




CREATE OR REPLACE FUNCTION update_product_price()
RETURNS TRIGGER AS $$
BEGIN
    -- Update price in product_detail to match the lowest price of its variants
    UPDATE products
    SET price = (
        SELECT MIN(price) FROM product_variants WHERE id = NEW.product_id
    )
    WHERE id = NEW.product_id;

    RETURN NEW;
END;
$$ LANGUAGE plpgsql;



-- Trigger to update the product price in product_details after an insert or update on product_variants
CREATE TRIGGER trigger_update_product_price
AFTER INSERT OR UPDATE ON product_variants
FOR EACH ROW
EXECUTE FUNCTION update_product_price();


CREATE OR REPLACE FUNCTION decrease_stock_and_increase_total_sold()
RETURNS TRIGGER AS $$
BEGIN
    -- Decrease stock quantity based on the quantity ordered
    UPDATE product_variants
    SET stock_quantity = stock_quantity - NEW.quantity
    WHERE id = NEW.product_variants_id;

    -- Increase total_sold in product_detail by the quantity ordered
    UPDATE product_details
    SET total_sold = total_sold + NEW.quantity
    WHERE product_id = NEW.product_id;

    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

-- Trigger to run after an insert on orders, to decrease stock and increase total sold
CREATE TRIGGER trigger_decrease_stock_and_increase_total_sold
AFTER INSERT ON order_items
FOR EACH ROW
EXECUTE FUNCTION decrease_stock_and_increase_total_sold();



