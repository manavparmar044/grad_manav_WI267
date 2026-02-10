-- ================= USERS =================
CREATE TABLE users (
    user_id SERIAL PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    phone VARCHAR(15),
    email VARCHAR(100),
    address TEXT,
    role VARCHAR(20) CHECK (role IN ('ADMIN', 'OWNER')) NOT NULL
);

INSERT INTO users (name, phone, email, address, role) VALUES
('Admin User', '9999999999', 'admin@layout.com', 'Layout Office, City', 'ADMIN'),
('Rahul Sharma', '9876543210', 'rahul@gmail.com', 'Site 101, Layout', 'OWNER'),
('Anita Patil', '9123456780', 'anita@gmail.com', 'Site 102, Layout', 'OWNER'),
('Suresh Kumar', '9988776655', 'suresh@gmail.com', 'Site 103, Layout', 'OWNER'),
('Priya Verma', '9012345678', 'priya@gmail.com', 'Site 104, Layout', 'OWNER');

-- ================= SITES =================
CREATE TABLE sites (
    site_id SERIAL PRIMARY KEY,
    site_number INT UNIQUE NOT NULL,
    site_type VARCHAR(30) CHECK (
        site_type IN ('VILLA', 'APARTMENT', 'INDEPENDENT_HOUSE', 'OPEN_SITE')
    ) NOT NULL,
    length_ft INT NOT NULL,
    width_ft INT NOT NULL,
    area_sqft INT NOT NULL,
    occupancy_status VARCHAR(20) CHECK (
        occupancy_status IN ('OPEN', 'RENTED', 'SELF_OCCUPIED', 'VACANT', 'SOLD')
    ) NOT NULL,
    owner_id INT,
    FOREIGN KEY (owner_id) REFERENCES users(user_id)
);

INSERT INTO sites VALUES
(DEFAULT, 101, 'VILLA', 40, 60, 2400, 'SELF_OCCUPIED', 2),
(DEFAULT, 102, 'INDEPENDENT_HOUSE', 30, 50, 1500, 'RENTED', 3),
(DEFAULT, 103, 'OPEN_SITE', 30, 40, 1200, 'OPEN', 4),
(DEFAULT, 104, 'APARTMENT', 30, 40, 1200, 'VACANT', 5),
(DEFAULT, 105, 'OPEN_SITE', 40, 60, 2400, 'OPEN', NULL);

-- ================= MAINTENANCE =================
CREATE TABLE maintenance (
    maintenance_id SERIAL PRIMARY KEY,
    site_id INT NOT NULL,
    month INT CHECK (month BETWEEN 1 AND 12),
    year INT NOT NULL,
    rate_per_sqft INT NOT NULL,
    total_amount INT NOT NULL,
    payment_status VARCHAR(20) CHECK (
        payment_status IN ('PAID', 'UNPAID', 'PARTIALLY_PAID')
    ) DEFAULT 'UNPAID',
    FOREIGN KEY (site_id) REFERENCES sites(site_id),
    UNIQUE (site_id, month, year)
);

INSERT INTO maintenance (site_id, month, year, rate_per_sqft, total_amount, payment_status) VALUES
(1, 1, 2026, 9, 21600, 'PAID'),
(2, 1, 2026, 9, 13500, 'UNPAID'),
(3, 1, 2026, 6, 7200, 'PAID'),
(4, 1, 2026, 6, 7200, 'UNPAID');

-- ================= PAYMENTS =================
CREATE TABLE payments (
    payment_id SERIAL PRIMARY KEY,
    maintenance_id INT NOT NULL,
    paid_amount INT CHECK (paid_amount > 0),
    payment_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    payment_status VARCHAR(20) CHECK (
        payment_status IN ('PAID', 'PARTIALLY_PAID')
    ),
    approved_by_admin BOOLEAN DEFAULT FALSE,
    FOREIGN KEY (maintenance_id) REFERENCES maintenance(maintenance_id)
);

INSERT INTO payments (maintenance_id, paid_amount, payment_status, approved_by_admin) VALUES
(3, 21600, 'PAID', TRUE),
(4, 5000, 'PARTIALLY_PAID', TRUE);

-- ================= SITE UPDATE REQUESTS =================
CREATE TABLE site_update_requests (
    request_id SERIAL PRIMARY KEY,
    site_id INT,
    owner_id INT,
    requested_site_type VARCHAR(30),
    requested_occupancy_status VARCHAR(20),
    status VARCHAR(20) CHECK (
        status IN ('PENDING', 'APPROVED', 'REJECTED')
    ) DEFAULT 'PENDING',
    FOREIGN KEY (site_id) REFERENCES sites(site_id),
    FOREIGN KEY (owner_id) REFERENCES users(user_id)
);

INSERT INTO site_update_requests (site_id, owner_id, requested_site_type, requested_occupancy_status) VALUES
(3, 4, 'INDEPENDENT_HOUSE', 'SELF_OCCUPIED');
