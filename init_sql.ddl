CREATE TABLE Role (
                      id BIGINT AUTO_INCREMENT PRIMARY KEY,
                      role_name VARCHAR(255) NOT NULL UNIQUE
);

CREATE TABLE User (
                      id BIGINT AUTO_INCREMENT PRIMARY KEY,
                      username VARCHAR(255) NOT NULL UNIQUE,
                      password VARCHAR(255) NOT NULL
);

CREATE TABLE UserRole (
                          user_id BIGINT,
                          role_id BIGINT,
                          PRIMARY KEY (user_id, role_id),
                          FOREIGN KEY (user_id) REFERENCES User(id),
                          FOREIGN KEY (role_id) REFERENCES Role(id)
);

CREATE TABLE `Function` (
                            id BIGINT AUTO_INCREMENT PRIMARY KEY,
                            function_name VARCHAR(255) NOT NULL UNIQUE
);

-- This table allows functions to be linked directly to users or roles
CREATE TABLE FunctionPermission (
                                    id BIGINT AUTO_INCREMENT PRIMARY KEY,
                                    function_id BIGINT NOT NULL,
                                    user_id BIGINT NULL,
                                    role_id BIGINT NULL,
                                    FOREIGN KEY (function_id) REFERENCES `Function`(id),
                                    FOREIGN KEY (user_id) REFERENCES User(id),
                                    FOREIGN KEY (role_id) REFERENCES Role(id),
                                    CHECK (user_id IS NOT NULL OR role_id IS NOT NULL) -- At least one must be non-null
);

INSERT INTO Role (role_name) VALUES ('ADMIN'), ('USER');

-- Assume passwords are already hashed for storage
INSERT INTO User (username, password) VALUES ('admin', 'hashed_admin_password'), ('user', 'hashed_user_password');

-- After executing the above, you'll need to query the DB to find the exact IDs of inserted roles
-- Here, we're assuming the IDs for simplicity
INSERT INTO UserRole (user_id, role_id) VALUES
                                            (1, 1), -- admin is an ADMIN
                                            (2, 2); -- user is a USER

INSERT INTO `Function` (function_name) VALUES ('CREATE_POST'), ('EDIT_POST'), ('DELETE_POST'), ('VIEW_POST');

-- Assuming function IDs follow their insertion order and assigning some permissions
-- Permissions might need to be adjusted based on your actual application logic and role setup
INSERT INTO FunctionPermission (function_id, role_id) VALUES
                                                          (1, 1), -- ADMIN can CREATE_POST
                                                          (2, 1), -- ADMIN can EDIT_POST
                                                          (3, 1), -- ADMIN can DELETE_POST
                                                          (4, 1), -- ADMIN can VIEW_POST
                                                          (4, 2); -- USER can VIEW_POST