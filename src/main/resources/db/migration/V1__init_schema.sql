-- V1__init_schema.sql

-- Audit log
CREATE TABLE audit_log (
                           id BIGSERIAL PRIMARY KEY,
                           audit_date TIMESTAMP(6),
                           action VARCHAR(255),
                           audit_by VARCHAR(255),
                           data TEXT,
                           message_id VARCHAR(255),
                           service_name VARCHAR(255),
                           topic_name VARCHAR(255)
);

-- Contact
CREATE TABLE contact (
                         contact_id BIGSERIAL PRIMARY KEY,
                         email VARCHAR(255) NOT NULL UNIQUE,
                         first_name VARCHAR(255) NOT NULL,
                         last_name VARCHAR(255) NOT NULL
);

-- Notification settings
CREATE TABLE notification_settings (
                                       notification_settings_id BIGSERIAL PRIMARY KEY,
                                       email_notifications_enabled BOOLEAN NOT NULL,
                                       sms_notifications_enabled BOOLEAN NOT NULL
);

-- User status
CREATE TABLE user_status (
                             status_id BIGSERIAL PRIMARY KEY,
                             status VARCHAR(255) NOT NULL UNIQUE
);

-- User
CREATE TABLE "user" (
                        user_id BIGSERIAL PRIMARY KEY,
                        username VARCHAR(255) NOT NULL UNIQUE,
                        password_hash VARCHAR(8) NOT NULL,
                        gender VARCHAR(255) NOT NULL CHECK (gender IN ('MALE','FEMALE')),
                        date_of_birth TIMESTAMP(6),
                        registration_date TIMESTAMP(6) NOT NULL,
                        last_login TIMESTAMP(6),
                        status_id BIGINT REFERENCES user_status(status_id),
                        contact_id BIGINT UNIQUE REFERENCES contact(contact_id),
                        notification_settings_id BIGINT UNIQUE REFERENCES notification_settings(notification_settings_id)
);

-- Role
CREATE TABLE role (
                      role_id BIGSERIAL PRIMARY KEY,
                      role_name VARCHAR(255) NOT NULL UNIQUE
);

-- User-Role join table
CREATE TABLE user_role (
                           role_id BIGINT NOT NULL REFERENCES role(role_id),
                           user_id BIGINT NOT NULL REFERENCES "user"(user_id),
                           PRIMARY KEY (role_id, user_id)
);

-- Document
CREATE TABLE document (
                          id BIGSERIAL PRIMARY KEY,
                          document_id BIGINT REFERENCES "user"(user_id),
                          number VARCHAR(20) NOT NULL UNIQUE,
                          document_type VARCHAR(255) CHECK (document_type IN ('PASSPORT','ID_CARD','VISA')),
                          issuance_date DATE NOT NULL,
                          expiry_date DATE NOT NULL,
                          holder BOOLEAN NOT NULL,
                          birth_place VARCHAR(255) NOT NULL,
                          nationality VARCHAR(255) NOT NULL,
                          issuance_country VARCHAR(255) NOT NULL,
                          issuance_location VARCHAR(255) NOT NULL,
                          validity_country VARCHAR(255) NOT NULL
);

-- Notification
CREATE TABLE notification (
                              id BIGSERIAL PRIMARY KEY,
                              type SMALLINT NOT NULL CHECK (type BETWEEN 0 AND 1),
                              status SMALLINT NOT NULL CHECK (status BETWEEN 0 AND 2),
                              message VARCHAR(255) NOT NULL,
                              receiver BIGINT NOT NULL REFERENCES "user"(user_id),
                              created_at TIMESTAMP(6) NOT NULL,
                              updated_at TIMESTAMP(6) NOT NULL
);

-- Booking
CREATE TABLE booking (
                         id BIGSERIAL PRIMARY KEY,
                         booking_number VARCHAR(255) NOT NULL UNIQUE,
                         booking_status SMALLINT NOT NULL CHECK (booking_status BETWEEN 0 AND 3),
                         booking_type SMALLINT NOT NULL CHECK (booking_type BETWEEN 0 AND 2),
                         from_location VARCHAR(255) NOT NULL,
                         to_location VARCHAR(255) NOT NULL,
                         arrival_date TIMESTAMP(6) NOT NULL,
                         departure_date TIMESTAMP(6) NOT NULL,
                         more_details VARCHAR(255),
                         price_details TEXT,
                         external_booking_id BIGINT NOT NULL,
                         flight_id BIGINT NOT NULL,
                         user_id BIGINT REFERENCES "user"(user_id),
                         contact_id BIGINT REFERENCES contact(contact_id),
                         pnr VARCHAR(255) NOT NULL,
                         created_at TIMESTAMP(6) NOT NULL,
                         updated_at TIMESTAMP(6) NOT NULL
);

-- Payment method
CREATE TABLE payment_method (
                                id BIGSERIAL PRIMARY KEY,
                                payment_name VARCHAR(255) NOT NULL UNIQUE,
                                description VARCHAR(255)
);

-- Transaction status
CREATE TABLE transaction_status (
                                    id BIGSERIAL PRIMARY KEY,
                                    status_name VARCHAR(255) NOT NULL UNIQUE,
                                    description VARCHAR(255)
);

-- Transaction
CREATE TABLE transaction (
                             id BIGSERIAL PRIMARY KEY,
                             currency SMALLINT NOT NULL CHECK (currency BETWEEN 0 AND 19),
                             total NUMERIC(38,2) NOT NULL,
                             intent VARCHAR(255) NOT NULL,
                             description VARCHAR(255),
                             approval_url VARCHAR(255),
                             cancel_url VARCHAR(255),
                             return_url VARCHAR(255),
                             success_url VARCHAR(255),
                             created_at TIMESTAMP(6) NOT NULL,
                             updated_at TIMESTAMP(6) NOT NULL,
                             payment_method_id BIGINT NOT NULL REFERENCES payment_method(id),
                             transaction_status_id BIGINT NOT NULL REFERENCES transaction_status(id)
);

-- Phone number
CREATE TABLE phone_number (
                              person_id BIGINT NOT NULL REFERENCES contact(contact_id),
                              number VARCHAR(255) NOT NULL UNIQUE,
                              country_calling_code VARCHAR(255) NOT NULL,
                              device_type VARCHAR(255) NOT NULL
);
