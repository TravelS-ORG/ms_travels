-- 1. Khởi tạo Tổ chức nền tảng và Tổ chức khách hàng mẫu (NAB)
INSERT INTO spacehub_platform.tenants (id, name, domain, status) VALUES
('system-root', 'SpaceHub Platform Operator', 'admin.spacehub.com', 'ACTIVE'),
('nab-vietnam', 'National Australia Bank Vietnam', 'nab.spacehub.com', 'ACTIVE')
ON CONFLICT (id) DO NOTHING;

-- 2. Khởi tạo danh mục Quyền tĩnh của hệ thống
INSERT INTO spacehub_rbac.spacehub_permission (id, permission_key, description) VALUES
(1, 'TENANT_CREATE', 'Tạo mới doanh nghiệp thuê phần mềm'),
(2, 'TENANT_SUSPEND', 'Khóa doanh nghiệp quá hạn thanh toán'),
(3, 'FLOOR_PLAN_WRITE', 'Cấu hình sơ đồ tầng, upload file bản vẽ SVG'),
(4, 'FLOOR_PLAN_READ', 'Xem sơ đồ mặt bằng hiển thị trạng thái ghế'),
(5, 'RESOURCE_LOCK', 'Khóa/Mở khóa ghế để dọn dẹp hoặc bảo trì thiết bị'),
(6, 'BOOKING_CREATE', 'Chọn chấm xanh trên bản đồ để giữ chỗ và đặt ghế'),
(7, 'BOOKING_CANCEL_ANY', 'Hủy lịch đặt của bất kỳ nhân viên nào (Quyền Admin tòa nhà)')
ON CONFLICT (id) DO NOTHING;

SELECT setval(pg_get_serial_sequence('spacehub_rbac.spacehub_permission', 'id'), COALESCE(MAX(id), 1)) FROM spacehub_rbac.spacehub_permission;

-- 3. Khởi tạo các Vai trò cốt lõi cho từng Tenant
INSERT INTO spacehub_rbac.spacehub_role (id, tenant_id, role_name, description) VALUES
(1, 'system-root', 'CRM_MANAGER', 'Quản trị viên tối cao của toàn hệ thống SaaS'),
(2, 'nab-vietnam', 'TENANT_ADMIN', 'Quản lý vận hành không gian làm việc của NAB'),
(3, 'nab-vietnam', 'TENANT_USER',  'Nhân viên NAB sử dụng app để đặt chỗ ngồi')
ON CONFLICT (tenant_id, role_name) DO NOTHING;

SELECT setval(pg_get_serial_sequence('spacehub_rbac.spacehub_role', 'id'), COALESCE(MAX(id), 1)) FROM spacehub_rbac.spacehub_role;

-- 4. Áp ma trận gán Quyền vào Vai trò tương ứng
INSERT INTO spacehub_rbac.spacehub_role_permission (role_id, permission_id) VALUES
(1, 1), (1, 2), -- CRM_MANAGER sở hữu quyền quản lý Tenant
(2, 3), (2, 4), (2, 5), (2, 7), -- TENANT_ADMIN sở hữu các quyền cấu hình và xử lý sự cố tòa nhà
(3, 4), (3, 6)  -- TENANT_USER sở hữu quyền đọc map và đặt chỗ
ON CONFLICT (role_id, permission_id) DO NOTHING;
