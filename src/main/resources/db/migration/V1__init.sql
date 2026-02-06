CREATE TABLE IF NOT EXISTS users (
    id BIGSERIAL PRIMARY KEY,
    email VARCHAR(255) NOT NULL UNIQUE,
    first_name VARCHAR(255),
    last_name VARCHAR(255),
    password VARCHAR(255) NOT NULL,
    role VARCHAR(50) NOT NULL
);

CREATE TABLE IF NOT EXISTS rooms (
    id BIGSERIAL PRIMARY KEY,
    title VARCHAR(255),
    description TEXT,
    price_per_night DOUBLE PRECISION,
    room_type VARCHAR(255),
    bed_type VARCHAR(255),
    bed_size INTEGER,
    rating REAL,
    max_guest INTEGER
);

CREATE TABLE IF NOT EXISTS amenities (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(255),
    icon VARCHAR(255)
);

CREATE TABLE IF NOT EXISTS room_images (
    id BIGSERIAL PRIMARY KEY,
    image_url VARCHAR(255) NOT NULL,
    image_type VARCHAR(255),
    room_id BIGINT REFERENCES rooms(id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS bookings (
    id BIGSERIAL PRIMARY KEY,
    customer_name VARCHAR(255),
    start_date DATE,
    end_date DATE,
    nights INTEGER,
    taxes DOUBLE PRECISION,
    discount DOUBLE PRECISION,
    guests INTEGER,
    address TEXT,
    status VARCHAR(255),
    room_id BIGINT REFERENCES rooms(id) ON DELETE SET NULL
);

CREATE TABLE IF NOT EXISTS room_amenities (
    room_id BIGINT NOT NULL REFERENCES rooms(id) ON DELETE CASCADE,
    amenity_id BIGINT NOT NULL REFERENCES amenities(id) ON DELETE CASCADE,
    PRIMARY KEY (room_id, amenity_id)
);
