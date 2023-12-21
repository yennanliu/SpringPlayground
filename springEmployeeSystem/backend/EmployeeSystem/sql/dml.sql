-- database : employee_system

-- DML for users table

-- truncate users;

INSERT INTO `users` (id, email, first_name, last_name, password, role, departement_id, manager_id)
VALUES
(1001, "tom@google.com", "tom", "chen", "123", "USER", 1, 4),
(1002, "lisa@google.com", "lisa", "park", "123", "USER", 2, 4),
(1003, "shohei@google.com", "shohei", "ohtani", "123", "USER", 3, 4),
(1004, "k@google.com", "k", "k", "123", "MANAGER", 3, 0),
(1005, 'user1001@example.com', 'John', 'Doe', '123', 'USER', 1, 4),
(1006, 'user1002@example.com', 'Jane', 'Smith', '123', 'USER', 2, 4),
(1007, 'user1003@example.com', 'Alex', 'Johnson', '123', 'USER', 3, 4),
(1008, 'manager1004@example.com', 'Manager', 'Doe', '123', 'MANAGER', 3, 0);

-- Retrieve and display the inserted data
SELECT * FROM `users` WHERE id BETWEEN 1001 AND 1050;


-- truncate department;

INSERT INTO `department` (id, name)
VALUES
(1, "tech"),
(2, "prod"),
(3, "mkt"),
(4, "sales"),
(5, "design");


-- truncate vacation;

INSERT INTO `vacation` (id, start_date, end_date, status, type, user_id)
VALUES
(1, "2023-12-01", "2023-12-02", "PENDING", "normal", 1001),
(2, "2024-12-10", "2027-12-11", "PENDING", "normal", 1002),
(3, "2023-12-10", "2023-12-11", "REJECTED", "normal", 1002),
(4, "2023-12-30", "2023-12-31", "APPROVED", "normal", 1003),
(5, "2024-12-01", "2024-12-02", "CANCELLED", "normal", 1004);