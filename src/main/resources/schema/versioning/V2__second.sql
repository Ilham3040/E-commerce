
CREATE TABLE product_detail (
    product_detail_id SERIAL PRIMARY KEY,
    product_id INT NOT NULL REFERENCES products(product_id) ON DELETE CASCADE,
    attachment_urls TEXT[],
    total_sold INT,
    review_rating DECIMAL(3,2)
);

CREATE TABLE product_reviews (
    review_id SERIAL PRIMARY KEY,
    product_id INT NOT NULL REFERENCES products(product_id) ON DELETE CASCADE,
    user_id INT NOT NULL REFERENCES users(user_id) ON DELETE CASCADE,
    star_rating INT CHECK (star_rating >= 1 AND star_rating <= 5),
    attachment_urls TEXT[],
    description TEXT,
    likes INT DEFAULT 0,
    created_at TIMESTAMPTZ DEFAULT NOW(),
    updated_at TIMESTAMPTZ DEFAULT NOW(),
    deleted_at TIMESTAMPTZ
);

CREATE TABLE product_variants (
    variant_id SERIAL PRIMARY KEY,
    product_id INT NOT NULL REFERENCES products(product_id) ON DELETE CASCADE,
    variant_name VARCHAR(255),
    product_reviews INT,
    stock_quantity INT DEFAULT 0,
    total_sold INT DEFAULT 0
);

CREATE TABLE store_details (
    store_detail_id SERIAL PRIMARY KEY,
    store_id INT NOT NULL REFERENCES stores(store_id) ON DELETE CASCADE,
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
    category_id SERIAL PRIMARY KEY,
    store_id INT NOT NULL REFERENCES stores(store_id) ON DELETE CASCADE,
    category_name VARCHAR(255) NOT NULL
);

CREATE TABLE store_categories_item (
    category_id INT NOT NULL REFERENCES store_categories(category_id) ON DELETE CASCADE,
    product_id INT NOT NULL REFERENCES products(product_id) ON DELETE CASCADE,
    PRIMARY KEY (category_id, product_id)
);

CREATE TABLE store_role (
    user_id INT NOT NULL REFERENCES users(user_id) ON DELETE CASCADE,
    store_id INT NOT NULL REFERENCES stores(store_id) ON DELETE CASCADE,
    role VARCHAR(50) CHECK (role IN ('owner', 'admin', 'null')) DEFAULT 'null',
    PRIMARY KEY (user_id, store_id)
);

CREATE TABLE user_cart (
    cart_id SERIAL PRIMARY KEY,
    user_id INT NOT NULL REFERENCES users(user_id) ON DELETE CASCADE,
    product_id INT NOT NULL REFERENCES products(product_id) ON DELETE CASCADE,
    quantity INT DEFAULT 1
);

CREATE TABLE user_favorite (
    user_id INT NOT NULL REFERENCES users(user_id) ON DELETE CASCADE,
    product_id INT NOT NULL REFERENCES products(product_id) ON DELETE CASCADE,
    PRIMARY KEY (user_id, product_id)
);

CREATE TABLE shipment_vendor (
    vendor_id SERIAL PRIMARY KEY,
    vendor_name VARCHAR(255) NOT NULL,
    vendor_contact VARCHAR(255),
    vendor_email VARCHAR(255) UNIQUE,
    official_website_url TEXT,
    created_at TIMESTAMPTZ DEFAULT NOW(),
    updated_at TIMESTAMPTZ DEFAULT NOW(),
    deleted_at TIMESTAMPTZ
);

CREATE TABLE shipment (
    shipment_id SERIAL PRIMARY KEY,
    vendor_id INT NOT NULL REFERENCES shipment_vendor(vendor_id) ON DELETE CASCADE,
    order_id INT NOT NULL REFERENCES orders(order_id) ON DELETE CASCADE
);
