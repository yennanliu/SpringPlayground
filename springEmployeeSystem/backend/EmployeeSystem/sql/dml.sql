-- database : employee_system

-- DML for users table

-- truncate users;

INSERT INTO `users` (id, email, first_name, last_name, password, role, departement_id, manager_id)
VALUES
(1001, "tom@google.com", "tom", "chen", "123", "USER", 1, 1004),
(1002, "lisa@google.com", "lisa", "park", "123", "USER", 1, 1004),
(1003, "shohei@google.com", "shohei", "ohtani", "123", "USER", 1, 1004),
(1004, "k@google.com", "k", "k", "123", "MANAGER", 1, 0),
(1005, 'john@fb.com', 'John', 'Doe', '123', 'USER', 2, 1008),
(1006, 'jane@fb.com', 'Jane', 'Smith', '123', 'USER', 2, 1008),
(1007, 'alex@fb.com', 'Alex', 'Johnson', '123', 'USER', 2, 1008),
(1008, 'm1004@fb.com', 'Manager', 'Doe', '123', 'MANAGER', 2, 0);


-- truncate department;

INSERT INTO `department` (id, name)
VALUES
(1, "TECH"),
(2, "PROD"),
(3, "MKT"),
(4, "SALES"),
(5, "DESIGN");


-- truncate vacation;

INSERT INTO `vacation` (id, start_date, end_date, status, type, user_id)
VALUES
(1, "2023-12-01", "2023-12-02", "PENDING", "normal", 1001),
(2, "2024-12-10", "2027-12-11", "PENDING", "normal", 1002),
(3, "2023-12-10", "2023-12-11", "REJECTED", "normal", 1002),
(4, "2023-12-30", "2023-12-31", "APPROVED", "normal", 1003),
(5, "2024-12-01", "2024-12-02", "CANCELLED", "normal", 1004);

-- truncate tickets;

CREATE TABLE tickets (
    id INT AUTO_INCREMENT PRIMARY KEY,
    subject VARCHAR(255),
    description TEXT,
    user_id INT,
    assigned_to INT,
    status VARCHAR(50),
    tag VARCHAR(50)
);

INSERT INTO `tickets` (id, description, status, subject, tag, user_id, assigned_to)
VALUES
(1001, "this is a ticket ...", "PENDING", "subject 1", "tag1", 1001, 1001),
(1002, "this is a small ticket ...", "PENDING", "subject 2", "tag1", 1001, 1003),
(1003, "this is a big ticket ...", "PENDING", "subject 3", "tag1", 1002, 1004);


-- truncate option_schema;

INSERT INTO `option_schema` (id, active, column_name, schema_name)
VALUES
(1001, 0, "test_col", "test_schema"),
(1002, 1, "test_col_2", "test_schema_2"),
(2001, 1, "normal", "vacation"),
(2002, 1, "business", "vacation"),
(2003, 1, "others", "vacation"),
(3001, 1, "urgent", "ticket"),
(3002, 1, "normal", "ticket"),
(3003, 1, "backlog", "ticket");

-- truncate check_in;

INSERT INTO `check_in` (id, user_id, create_time)
VALUES
(1001, 1, now()),
(1002, 2, now()),
(1003, 3, now());