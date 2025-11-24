INSERT INTO "tbl_employee" (username, password, fullname, position, phone_number) VALUES
('admin01', 'admin', 'Nguyễn Văn A', 'ADMIN', '0123456789'),
('manager01', 'manager01', 'Trần Thị B', 'PARTNER_MANAGER', '0123456788'),
('manager02', 'manager02', 'Lê Văn C', 'PARTNER_MANAGER', '0123456787'),
('accountant01', 'accountant01', 'Phạm Thị D', 'ACCOUNTANT', '0123456786'),
('accountant02', 'accountant02', 'Hoàng Văn E', 'ACCOUNTANT', '0123456785');
SELECT * FROM tbl_employee;

INSERT INTO "tbl_partner_manager" (employee_id) VALUES
(2),
(3);
SELECT * FROM tbl_partner_manager;

INSERT INTO "tbl_accountant" (employee_id) VALUES
(4),
(5);
SELECT * FROM tbl_accountant;

INSERT INTO "tbl_partner" (name_partner, partner_representative, phone_number, email, address, tax_code, connperation_date, description, partner_manager_id) VALUES
('Công ty TNHH ABC', 'Nguyễn Minh H', '0123456784', 'contact@abc.com', '123 Đường Láng, Đống Đa, Hà Nội', '0123456789', '2025-01-15', 'Đối tác cung cấp nhân lực', 2),
('Công ty CP XYZ', 'Trần Thị M', '0123456783', 'info@xyz.com', '456 Nguyễn Trãi, Thanh Xuân, Hà Nội', '0123456788', '2025-03-20', 'Đối tác dịch vụ outsourcing', 2),
('Công ty TNHH DEF', 'Lê Văn N', '0123456782', 'hello@def.com', '789 Giải Phóng, Hai Bà Trưng, Hà Nội', '0123456787', '2025-05-10', 'Đối tác kỹ thuật', 3),
('Công ty CP GHI', 'Phạm Thị O', '0123456781', 'contact@ghi.com', '321 Cầu Giấy, Cầu Giấy, Hà Nội', '0123456786', '2025-07-25', 'Đối tác tư vấn', 3);
SELECT * FROM tbl_partner;

INSERT INTO "tbl_contract" (contract_name, start_date, end_date, total_contract_value, contract_status, description, partner_manager_id, partner_id) VALUES
('Hợp đồng cung cấp nhân lực Q1-2025', '2025-01-15', '2025-06-30', 500000000, 'COMPLETED', 'Cung cấp 20 nhân viên IT', 2, 1),
('Hợp đồng outsourcing Q2-2025', '2025-03-20', '2025-09-20', 750000000, 'COMPLETED', 'Dịch vụ phát triển phần mềm', 2, 2),
('Hợp đồng kỹ thuật 2025', '2025-05-10', '2025-12-31', 1000000000, 'ACTIVE', 'Tư vấn và triển khai hệ thống', 3, 3),
('Hợp đồng tư vấn Q3-2025', '2024-07-25', '2025-01-25', 300000000, 'CANCELLED', 'Tư vấn chiến lược kinh doanh', 3, 4);
SELECT * FROM tbl_contract;

INSERT INTO "tbl_task" (name_task, description) VALUES
('Lập trình Backend', 'Phát triển API và xử lý logic nghiệp vụ'),
('Lập trình Frontend', 'Phát triển giao diện người dùng'),
('Kiểm thử phần mềm', 'Test và đảm bảo chất lượng sản phẩm'),
('Phân tích hệ thống', 'Phân tích yêu cầu và thiết kế hệ thống'),
('Quản lý dự án', 'Điều phối và quản lý tiến độ dự án');
SELECT * FROM tbl_task;

INSERT INTO "tbl_task_contract" (task_unit_price, contract_id, task_id) VALUES
(25000000, 1, 1), -- Backend cho hợp đồng 1
(20000000, 1, 2), -- Frontend cho hợp đồng 1
(15000000, 2, 3), -- Testing cho hợp đồng 2
(30000000, 2, 4), -- Phân tích cho hợp đồng 2
(35000000, 3, 5), -- Quản lý cho hợp đồng 3
(25000000, 3, 1), -- Backend cho hợp đồng 3
(20000000, 4, 4); -- Phân tích cho hợp đồng 4
SELECT * FROM tbl_task_contract;

INSERT INTO "tbl_invoice" (invoice_date, payment_amount, remaining_amount, payment_method, invoice_status, accountant_id) VALUES
('2025-02-15 10:30:00', 100000000, 0, 'TRANSFER', 'PAID', 4),
('2025-03-15 14:20:00', 150000000, 0, 'TRANSFER', 'PAID', 4),
('2025-04-20 09:15:00', 200000000, 0, 'TRANSFER', 'PAID', 5),
('2025-05-25 16:45:00', 150000000, 400000000, 'CASH', 'PARTIALLY_PAID', 5),
('2025-06-30 11:00:00', 250000000, 750000000, 'TRANSFER', 'PARTIALLY_PAID', 4);
SELECT * FROM tbl_invoice;

INSERT INTO "tbl_shift" (work_date, shift_type, description, invoice_id) VALUES
('2025-02-01', 'MORNING', 'Ca làm việc buổi sáng - Backend development', 1),
('2025-02-01', 'AFTERNOON', 'Ca làm việc buổi chiều - Frontend development', 1),
('2025-02-02', 'MORNING', 'Ca làm việc buổi sáng - Testing', 1),
('2025-03-01', 'MORNING', 'Ca làm việc buổi sáng - System analysis', 2),
('2025-03-01', 'AFTERNOON', 'Ca làm việc buổi chiều - Project management', 2),
('2025-04-01', 'NIGHT', 'Ca làm việc đêm - Backend development', 3),
('2025-04-02', 'MORNING', 'Ca làm việc buổi sáng - Frontend development', 3),
('2025-05-01', 'AFTERNOON', 'Ca làm việc buổi chiều - Testing', 4),
('2025-06-01', 'EVENING', 'Ca làm việc tối - System implementation', 5);
SELECT * FROM tbl_shift;

INSERT INTO "tbl_shift_task_contract" (worker_count, shift_unit_price, task_contract_id, shift_id) VALUES
(5, 25000000, 1, 1), -- 5 backend developers
(3, 20000000, 2, 2), -- 3 frontend developers
(2, 15000000, 3, 3), -- 2 testers
(2, 30000000, 4, 4), -- 2 analysts
(1, 35000000, 5, 5), -- 1 project manager
(8, 25000000, 6, 6), -- 8 backend developers
(4, 20000000, 6, 7), -- 4 frontend developers
(3, 15000000, 3, 8), -- 3 testers
(5, 25000000, 6, 9); -- 5 developers for implementation
SELECT * FROM tbl_shift_task_contract;