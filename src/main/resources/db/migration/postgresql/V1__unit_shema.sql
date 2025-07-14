-- Users Table
CREATE TABLE users (
    id SERIAL PRIMARY KEY,
    name VARCHAR(255),
    email VARCHAR(255) UNIQUE NOT NULL,
    password VARCHAR(255),
    provider VARCHAR(50),
    providerId VARCHAR(150),
    avatarUrl TEXT,
    createdAt TIMESTAMP DEFAULT NOW(),
    updatedAt TIMESTAMP DEFAULT NOW(),
    deletedAt TIMESTAMP DEFAULT NOW()
);

-- OAuth Token Table
CREATE TABLE oauth_tokens (
    id SERIAL PRIMARY KEY,
    user_id INT REFERENCES users(id) ON DELETE CASCADE,
    accessToken TEXT NOT NULL,
    refreshToken TEXT,
    expiresAt TIMESTAMP
);

-- Device Session Table
CREATE TABLE device_sessions (
    id SERIAL PRIMARY KEY,
    user_id INT REFERENCES users(id) ON DELETE CASCADE,
    deviceId VARCHAR(255) NOT NULL,
    jwtToken TEXT NOT NULL,
    createdAt TIMESTAMP DEFAULT NOW(),
    expiredAt TIMESTAMP
);

-- Revoked Token Table (Blacklist)
CREATE TABLE revoked_tokens (
    id SERIAL PRIMARY KEY,
    token TEXT NOT NULL,
    revokedAt TIMESTAMP DEFAULT NOW()
);