
CREATE TABLE product_detail (
    id SERIAL PRIMARY KEY,
    product_id INT NOT NULL REFERENCES products(id) ON DELETE CASCADE,
    description TEXT,
    attachment_urls TEXT[],
    total_sold INT,
    review_rating DECIMAL(3,2)
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
    deleted_at TIMESTAMPTZ
);


CREATE TABLE product_variants (
    id SERIAL PRIMARY KEY,
    product_id INT NOT NULL REFERENCES products(id) ON DELETE CASCADE,
    variant_name VARCHAR(255),
    product_reviews INT,
    stock_quantity INT DEFAULT 0,
    total_sold INT DEFAULT 0
);


CREATE TABLE store_details (
    id SERIAL PRIMARY KEY,
    store_id INT NOT NULL REFERENCES stores(id) ON DELETE CASCADE,
    address TEXT,
    review DECIMAL(3,2),
    total_products INT,
    description TEXT,
    attachment_urls TEXT[],
    follower_count INT DEFAULT 0,
    created_at TIMESTAMPTZ DEFAULT NOW(),
    updated_at TIMESTAMPTZ DEFAULT NOW()
);


CREATE TABLE store_categories (
    id SERIAL PRIMARY KEY,
    store_id INT NOT NULL REFERENCES stores(id) ON DELETE CASCADE,
    category_name VARCHAR(255) NOT NULL
);


CREATE TABLE store_categories_item (
    category_id INT NOT NULL REFERENCES store_categories(id) ON DELETE CASCADE,
    product_id INT NOT NULL REFERENCES products(id) ON DELETE CASCADE,
    PRIMARY KEY (category_id, product_id)
);


CREATE TABLE store_role (
    user_id INT NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    store_id INT NOT NULL REFERENCES stores(id) ON DELETE CASCADE,
    role VARCHAR(50) CHECK (role IN ('owner', 'admin', 'null')) DEFAULT 'null',
    PRIMARY KEY (user_id, store_id)
);


CREATE TABLE user_cart (
    id SERIAL PRIMARY KEY,
    user_id INT NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    product_id INT NOT NULL REFERENCES products(id) ON DELETE CASCADE,
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
    deleted_at TIMESTAMPTZ
);


CREATE TABLE shipment (
    id SERIAL PRIMARY KEY,
    vendor_id INT NOT NULL REFERENCES shipment_vendor(id) ON DELETE CASCADE,
    order_id INT NOT NULL REFERENCES orders(id) ON DELETE CASCADE
);
