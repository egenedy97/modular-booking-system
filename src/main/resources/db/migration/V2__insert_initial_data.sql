-- Insert into user_status
INSERT INTO user_status (status) VALUES
                                     ('ACTIVE'),
                                     ('INACTIVE'),
                                     ('PENDING'),
                                     ('DELETED'),
                                     ('BLOCKED'),
                                     ('SUSPENDED');

-- Insert into role
INSERT INTO role (role_name) VALUES
                                 ('ADMIN'),
                                 ('USER'),
                                 ('MANAGER'),
                                 ('SUPPORT');

-- Insert into transaction_status
INSERT INTO transaction_status (status_name, description) VALUES
                                                              ('PENDING', 'Waiting for processing'),
                                                              ('COMPLETED', 'Transaction completed successfully'),
                                                              ('FAILED', 'Transaction failed'),
                                                              ('CANCELLED', 'Transaction was cancelled by user');

-- Insert into payment_method
INSERT INTO payment_method (payment_name, description) VALUES
                                                           ('CREDIT_CARD', 'Visa, MasterCard, AMEX'),
                                                           ('PAYPAL', 'PayPal account payments'),
                                                           ('BANK_TRANSFER', 'Wire transfer'),
                                                           ('CASH', 'Cash payment');
