<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Xác nhận Hợp đồng</title>
    <style>
        * {
            margin: 0;
            padding: 0;
            box-sizing: border-box;
        }

        body {
            font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
            background: #f5f7fa;
        }

        .navbar {
            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
            color: white;
            padding: 15px 30px;
            display: flex;
            justify-content: space-between;
            align-items: center;
            box-shadow: 0 2px 10px rgba(0,0,0,0.1);
        }

        .navbar-brand {
            font-size: 24px;
            font-weight: 600;
        }

        .navbar-user {
            display: flex;
            align-items: center;
            gap: 20px;
        }

        .user-name {
            font-weight: 600;
        }

        .btn-logout {
            background: rgba(255,255,255,0.2);
            color: white;
            border: 2px solid white;
            padding: 8px 20px;
            border-radius: 6px;
            text-decoration: none;
            font-weight: 600;
            transition: all 0.3s ease;
        }

        .btn-logout:hover {
            background: white;
            color: #667eea;
        }

        .container {
            max-width: 1400px;
            margin: 30px auto;
            padding: 0 20px;
        }

        .page-header {
            background: white;
            border-radius: 15px;
            padding: 30px;
            box-shadow: 0 5px 20px rgba(0,0,0,0.08);
            margin-bottom: 30px;
            text-align: center;
        }

        .page-header h1 {
            color: #333;
            font-size: 32px;
            margin-bottom: 10px;
        }

        .page-header p {
            color: #666;
            font-size: 16px;
        }

        .alert {
            padding: 15px 20px;
            border-radius: 8px;
            margin-bottom: 20px;
        }

        .alert-danger {
            background: #f8d7da;
            color: #721c24;
            border: 1px solid #f5c6cb;
        }

        .contract-card {
            background: white;
            border-radius: 15px;
            padding: 40px;
            box-shadow: 0 5px 20px rgba(0,0,0,0.08);
            margin-bottom: 30px;
        }

        .contract-title {
            text-align: center;
            margin-bottom: 30px;
            padding-bottom: 20px;
            border-bottom: 3px solid #667eea;
        }

        .contract-title h2 {
            color: #333;
            font-size: 26px;
            margin-bottom: 10px;
        }

        .contract-title p {
            color: #666;
            font-size: 15px;
        }

        .info-section {
            margin-bottom: 30px;
        }

        .info-section h3 {
            color: #667eea;
            font-size: 18px;
            margin-bottom: 15px;
            padding-bottom: 10px;
            border-bottom: 2px solid #f0f0f0;
        }

        .info-grid {
            display: grid;
            grid-template-columns: 1fr 1fr;
            gap: 20px;
            margin-bottom: 20px;
        }

        .info-item {
            padding: 15px;
            background: #f8f9fa;
            border-radius: 8px;
        }

        .info-item label {
            display: block;
            font-weight: 600;
            color: #666;
            font-size: 13px;
            margin-bottom: 5px;
            text-transform: uppercase;
        }

        .info-item .value {
            color: #333;
            font-size: 16px;
            font-weight: 500;
        }

        .tasks-section {
            margin-top: 30px;
        }

        .task-block {
            background: #f8f9fa;
            border-radius: 12px;
            padding: 25px;
            margin-bottom: 25px;
            border-left: 5px solid #667eea;
        }

        .task-header {
            display: flex;
            justify-content: space-between;
            align-items: center;
            margin-bottom: 20px;
            padding-bottom: 15px;
            border-bottom: 2px solid #e0e0e0;
        }

        .task-header h4 {
            color: #333;
            font-size: 20px;
        }

        .task-total {
            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
            color: white;
            padding: 8px 16px;
            border-radius: 20px;
            font-weight: 600;
            font-size: 14px;
        }

        .shift-table {
            width: 100%;
            border-collapse: collapse;
            margin-top: 15px;
            background: white;
            border-radius: 8px;
            overflow: hidden;
        }

        .shift-table thead {
            background: #667eea;
            color: white;
        }

        .shift-table th {
            padding: 12px 15px;
            text-align: left;
            font-weight: 600;
            font-size: 13px;
        }

        .shift-table td {
            padding: 12px 15px;
            border-bottom: 1px solid #dee2e6;
            color: #666;
            font-size: 14px;
        }

        .shift-table tbody tr:last-child td {
            border-bottom: none;
        }

        .shift-table tbody tr:hover {
            background: #f8f9fa;
        }

        .summary-box {
            background: linear-gradient(135deg, #56ab2f 0%, #a8e063 100%);
            color: white;
            padding: 30px;
            border-radius: 15px;
            text-align: center;
            margin-top: 30px;
        }

        .summary-box h3 {
            font-size: 18px;
            margin-bottom: 10px;
            opacity: 0.9;
        }

        .summary-box .total-value {
            font-size: 42px;
            font-weight: 700;
            margin-bottom: 5px;
        }

        .action-buttons {
            display: flex;
            gap: 15px;
            justify-content: center;
            margin-top: 40px;
            padding-top: 30px;
            border-top: 3px solid #f0f0f0;
        }

        .btn {
            padding: 16px 40px;
            border-radius: 10px;
            font-weight: 600;
            font-size: 16px;
            cursor: pointer;
            transition: all 0.3s ease;
            border: none;
            text-decoration: none;
            display: inline-block;
        }

        .btn-success {
            background: linear-gradient(135deg, #56ab2f 0%, #a8e063 100%);
            color: white;
        }

        .btn-success:hover {
            transform: translateY(-3px);
            box-shadow: 0 8px 20px rgba(86, 171, 47, 0.4);
        }

        .btn-warning {
            background: linear-gradient(135deg, #f093fb 0%, #f5576c 100%);
            color: white;
        }

        .btn-warning:hover {
            transform: translateY(-3px);
            box-shadow: 0 8px 20px rgba(240, 147, 251, 0.4);
        }

        .btn-secondary {
            background: #6c757d;
            color: white;
        }

        .btn-secondary:hover {
            background: #5a6268;
            transform: translateY(-2px);
        }

        @media print {
            .navbar, .action-buttons, .btn-logout {
                display: none;
            }

            body {
                background: white;
            }

            .contract-card {
                box-shadow: none;
            }
        }

        @media (max-width: 768px) {
            .info-grid {
                grid-template-columns: 1fr;
            }

            .action-buttons {
                flex-direction: column;
            }

            .btn {
                width: 100%;
            }
        }
    </style>
</head>
<body>
    <nav class="navbar">
        <div class="navbar-brand">Partner Management</div>
        <div class="navbar-user">
            <span class="user-name">${fullname}</span>
            <a href="${pageContext.request.contextPath}/auth/logout" class="btn-logout">Đăng xuất</a>
        </div>
    </nav>

    <div class="container">
        <div class="page-header">
            <h1>📋 Xác nhận Hợp đồng</h1>
            <p>Kiểm tra kỹ thông tin trước khi lưu hợp đồng</p>
        </div>

        <c:if test="${not empty errorMessage}">
            <div class="alert alert-danger">
                ${errorMessage}
            </div>
        </c:if>

        <div class="contract-card">
            <div class="contract-title">
                <h2>${contractDetail.contractName}</h2>
                <p><strong>Mô tả:</strong> ${contractDetail.description != null ? contractDetail.description : 'Không có mô tả'}</p>
            </div>

            <div class="info-section">
                <h3>📊 Thông tin đối tác</h3>
                <div class="info-grid">
                    <div class="info-item">
                        <label>Tên đối tác</label>
                        <div class="value">${contractDetail.partnerName}</div>
                    </div>
                    <div class="info-item">
                        <label>Người đại diện</label>
                        <div class="value">${contractDetail.partnerRepresentative}</div>
                    </div>
                    <div class="info-item">
                        <label>Số điện thoại</label>
                        <div class="value">${contractDetail.partnerPhone}</div>
                    </div>
                    <div class="info-item">
                        <label>Email</label>
                        <div class="value">${contractDetail.partnerEmail}</div>
                    </div>
                </div>
            </div>

            <div class="info-section">
                <h3>👤 Thông tin nhân viên đại diện công ty</h3>
                <div class="info-grid">
                    <div class="info-item">
                        <label>Họ và tên</label>
                        <div class="value">${contractDetail.managerName}</div>
                    </div>
                    <div class="info-item">
                        <label>Chức vụ</label>
                        <div class="value">${contractDetail.managerPosition}</div>
                    </div>
                </div>
            </div>

            <div class="tasks-section">
                <h3 style="color: #667eea; font-size: 22px; margin-bottom: 25px;">📋 Danh sách đầu việc & Ca làm việc</h3>

                <c:forEach items="${contractDetail.taskContracts}" var="tc" varStatus="taskStatus">
                    <div class="task-block">
                        <div class="task-header">
                            <h4>📌 ${tc.taskName}</h4>
                            <span class="task-total">Tổng: ${tc.totalValue} VNĐ</span>
                        </div>

                        <c:if test="${not empty tc.taskDescription}">
                            <p style="color: #666; margin-bottom: 15px; font-style: italic;">
                                ${tc.taskDescription}
                            </p>
                        </c:if>

                        <table class="shift-table">
                            <thead>
                                <tr>
                                    <th>STT</th>
                                    <th>Mã CLV</th>
                                    <th>Ngày làm</th>
                                    <th>Giờ bắt đầu</th>
                                    <th>Giờ kết thúc</th>
                                    <th>Số lượng NC</th>
                                    <th>Đơn giá (VNĐ)</th>
                                    <th>Thành tiền (VNĐ)</th>
                                </tr>
                            </thead>
                            <tbody>
                                <c:forEach items="${tc.shifts}" var="shift" varStatus="shiftStatus">
                                    <tr>
                                        <td>${shiftStatus.index + 1}</td>
                                        <td>CLV${shift.id}</td>
                                        <td>${shift.workDate}</td>
                                        <td>${shift.startTime}</td>
                                        <td>${shift.endTime}</td>
                                        <td>${shift.workerCount}</td>
                                        <td>${shift.shiftUnitPrice}</td>
                                        <td><strong>${shift.totalValue}</strong></td>
                                    </tr>
                                </c:forEach>
                            </tbody>
                        </table>
                    </div>
                </c:forEach>
            </div>

            <div class="summary-box">
                <h3>TỔNG GIÁ TRỊ HỢP ĐỒNG</h3>
                <div class="total-value">
                    <c:set var="totalValue" value="0" />
                    <c:forEach items="${contractDetail.taskContracts}" var="tc">
                        <c:set var="totalValue" value="${totalValue + tc.totalValue}" />
                    </c:forEach>
                    ${totalValue} VNĐ
                </div>
                <p style="opacity: 0.9; margin-top: 5px;">Bằng chữ:
                    <c:choose>
                        <c:when test="${totalValue < 1000}">
                            ${totalValue} đồng
                        </c:when>
                        <c:otherwise>
                            (Viết bằng chữ theo quy định)
                        </c:otherwise>
                    </c:choose>
                </p>
            </div>

            <div class="action-buttons">
                <form action="${pageContext.request.contextPath}/partner-manager/contracts/finalize"
                      method="post"
                      onsubmit="return confirm('Bạn có chắc chắn muốn lưu hợp đồng này? Sau khi lưu sẽ không thể chỉnh sửa.')">
                    <button type="submit" class="btn btn-success">
                        ✓ Lưu hợp đồng
                    </button>
                </form>

                <a href="${pageContext.request.contextPath}/partner-manager/contracts/tasks"
                   class="btn btn-warning">
                    ✏️ Sửa đổi
                </a>

                <a href="${pageContext.request.contextPath}/partner-manager/contracts/cancel"
                   class="btn btn-secondary"
                   onclick="return confirm('Bạn có chắc muốn hủy hợp đồng này? Tất cả dữ liệu sẽ bị xóa.')">
                    ❌ Hủy hợp đồng
                </a>
            </div>
        </div>
    </div>

    <script>
        // Tự động in trang khi nhấn Ctrl+P
        document.addEventListener('keydown', function(e) {
            if (e.ctrlKey && e.key === 'p') {
                e.preventDefault();
                window.print();
            }
        });
    </script>
</body>
</html>