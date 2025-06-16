CREATE TABLE gate_access_codes
(
    id             UUID    NOT NULL,
    code           VARCHAR(255),
    expires_at     TIMESTAMP WITHOUT TIME ZONE,
    used           BOOLEAN NOT NULL,
    transaction_id UUID,
    CONSTRAINT pk_gate_access_codes PRIMARY KEY (id)
);

CREATE TABLE payment_transactions
(
    id                UUID NOT NULL,
    amount            DECIMAL,
    currency          VARCHAR(255),
    status            VARCHAR(255),
    description       VARCHAR(255),
    stripe_payment_id VARCHAR(255),
    user_id           UUID,
    created_at        TIMESTAMP WITHOUT TIME ZONE,
    CONSTRAINT pk_payment_transactions PRIMARY KEY (id)
);

CREATE TABLE users
(
    id       UUID NOT NULL,
    username VARCHAR(255),
    password VARCHAR(255),
    phone    VARCHAR(255),
    role     VARCHAR(255),
    CONSTRAINT pk_users PRIMARY KEY (id)
);

ALTER TABLE gate_access_codes
    ADD CONSTRAINT uc_gate_access_codes_transaction UNIQUE (transaction_id);

ALTER TABLE users
    ADD CONSTRAINT uc_users_username UNIQUE (username);

ALTER TABLE gate_access_codes
    ADD CONSTRAINT FK_GATE_ACCESS_CODES_ON_TRANSACTION FOREIGN KEY (transaction_id) REFERENCES payment_transactions (id);

ALTER TABLE payment_transactions
    ADD CONSTRAINT FK_PAYMENT_TRANSACTIONS_ON_USER FOREIGN KEY (user_id) REFERENCES users (id);