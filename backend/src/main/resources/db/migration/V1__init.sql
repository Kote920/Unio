CREATE TABLE users (
    id           UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    firebase_uid TEXT NOT NULL UNIQUE,
    email        TEXT NOT NULL UNIQUE,
    display_name TEXT,
    created_at   TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT now(),
    updated_at   TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT now()
);

CREATE INDEX idx_users_firebase_uid ON users(firebase_uid);
