CREATE TABLE kt_credit
(
    id                     BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    credit_code            UUID                                    NOT NULL,
    credit_value           DECIMAL                                 NOT NULL,
    day_first_installment  date                                    NOT NULL,
    number_of_installments INT                                     NOT NULL,
    status                 SMALLINT,
    customer_id            BIGINT,
    CONSTRAINT pk_kt_credit PRIMARY KEY (id)
);

ALTER TABLE kt_credit
    ADD CONSTRAINT uc_kt_credit_credit_code UNIQUE (credit_code);

ALTER TABLE kt_credit
    ADD CONSTRAINT FK_KT_CREDIT_ON_CUSTOMER FOREIGN KEY (customer_id) REFERENCES kt_customer (id);