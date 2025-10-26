<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Chọn Đầu việc</title>
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
            max-width: 1200px;
            margin: 30px auto;
            padding: 0 20px;
        }

        .page-header {
            background: white;
            border-radius: 15px;
            padding: 30px;
            box-shadow: 0 5px 20px rgba(0,0,0,0.08);
            margin-bottom: 30px;
        }

        .page-header h1 {
            color: #333;
            font-size: 28px;
            margin-bottom: 10px;
        }

        .page-header p {
            color: #666;
            font-size: 14px;
        }

        .alert {
            padding: 15px 20px;
            border-radius: 8px;
            margin-bottom: 20px;
        }

        .alert-success {
            background: #d4edda;
            color: #155724;
            border: 1px solid #c3e6cb;
        }

        .alert-danger {
            background: #f8d7da;
            color: #721c24;
            border: 1px solid #f5c6cb;
        }

        .content-card {
            background: white;
            border-radius: 15px;
            padding: 30px;
            box-shadow: 0 5px 20px rgba(0,0,0,0.08);
            margin-bottom: 20px;
        }

        .section-title {
            font-size: 20px;
            color: #333;
            margin-bottom: 20px;
            padding-bottom: 10px;
            border-bottom: 2px solid #f0f0f0;
        }

        .task-grid {
            display: grid;
            grid-template-columns: repeat(auto-fill, minmax(300px, 1fr));
            gap: 20px;
            margin-bottom: 30px;
        }

        .task-card {
            background: #f8f9fa;
            border: 2px solid #e0e0e0;
            border-radius: 10px;
            padding: 20px;
            transition: all 0.3s ease;
            cursor: pointer;
        }

        .task-card:hover {
            border-color: #667eea;
            box-shadow: 0 5px 15px rgba(102, 126, 234, 0.2);
            transform: translateY(-2px);
        }

        .task-card h3 {
            color: #333;
            font-size: 18px;
            margin-bottom: 10px;
        }

        .task-card p {
            color: #666;
            font-size: 14px;
            margin-bottom: 15px;
            line-height: 1.6;
        }

        .task-card form {
            margin: 0;
        }

        .btn {
            padding: 10px 20px;
            border-radius: 8px;
            font-weight: 600;
            font-size: 14px;
            cursor: pointer;
            transition: all 0.3s ease;
            border: none;
            text-decoration: none;
            display: inline-block;
            width: 100%;
            text-align: center;
        }

        .btn-primary {
            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
            color: white;
        }

        .btn-primary:hover {
            transform: translateY(-2px);
            box-shadow: 0 5px 15px rgba(102, 126, 234, 0.4);
        }

        .btn-success {
            background: linear-gradient(135deg, #56ab2f 0%, #a8e063 100%);
            color: white;
        }

        .btn-success:hover {
            transform: translateY(-2px);
            box-shadow: 0 5px 15px rgba(86, 171, 47, 0.4);
        }

        .btn-secondary {
            background: #e0e0e0;
            color: #333;
        }

        .btn-secondary:hover {
            background: #d0d0d0;
        }

        .btn-warning {
            background: linear-gradient(135deg, #f093fb 0%, #f5576c 100%);
            color: white;
        }

        .btn-warning:hover {
            transform: translateY(-2px);
            box-shadow: 0 5px 15px rgba(240, 147, 251, 0.4);
        }

        .action-buttons {
            display: flex;
            gap: 15px;
            justify-content: flex-end;
            padding-top: 20px;
            border-top: 2px solid #f0f0f0;
        }

        .no-data {
            text-align: center;
            padding: 40px;
            color: #999;
            font-size: 16px;
        }

        .info-box {
            background: #e7f3ff;
            border-left: 4px solid #2196F3;
            padding: 15px;
            margin-bottom: 25px;
            border-radius: 5px;
            font-size: 14px;
            color: #1976D2;
        }

        .selected-tasks {
            background: #f0f9ff;
            border-left: 4px solid #0ea5e9;
            padding: 20px;
            border-radius: 8px;
            margin-bottom: 30px;
        }

        .selected-tasks h3 {
            color: #0369a1;
            margin-bottom: 15px;
        }

        .selected-task-item {
            background: white;
            padding: 15px;
            border-radius: 8px;
            margin-bottom: 10px;
            display: flex;
            justify-content: space-between;
            align-items: center;
            border: 1px solid #e0e0e0;
        }

        .selected-task-item:last-child {
            margin-bottom: 0;
        }

        .selected-task-info {
            flex: 1;
        }

        .selected-task-info strong {
            color: #333;
            font-size: 16px;
        }

        .selected-task-info p {
            color: #666;
            font-size: 13px;
            margin-top: 5px;
        }

        .badge {
            padding: 5px 12px;
            border-radius: 20px;
            font-size: 12px;
            font-weight: 600;
            background: #d4edda;
            color: #155724;
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
            <h1>📋 Chọn đầu việc cho hợp đồng</h1>
            <p>Chọn các đầu việc mà đối tác cần thực hiện</p>
        </div>

        <c:if test="${not empty successMessage}">
            <div class="alert alert-success">
                ${successMessage}
            </div>
        </c:if>

        <c:if test="${not empty errorMessage}">
            <div class="alert alert-danger">
                ${errorMessage}
            </div>
        </c:if>

        <c:if test="${not empty selectedTaskContracts}">
            <div class="selected-tasks">
                <h3>✓ Các đầu việc đã chọn (${selectedTaskContracts.size()})</h3>
                <c:forEach items="${selectedTaskContracts}" var="tc">
                    <div class="selected-task-item">
                        <div class="selected-task-info">
                            <strong>${tc.taskName}</strong>
                            <p>Số ca làm việc: ${tc.shiftCount} | Tổng giá trị: ${tc.totalValue} VNĐ</p>
                        </div>
                        <span class="badge">✓ Đã chọn</span>
                    </div>
                </c:forEach>
            </div>
        </c:if>

        <div class="content-card">
            <div class="info-box">
                💡 Nhấn vào đầu việc để thêm vào hợp đồng và thiết lập ca làm việc
            </div>

            <h2 class="section-title">Danh sách đầu việc có sẵn</h2>

            <c:choose>
                <c:when test="${empty tasks}">
                    <div class="no-data">
                        <p>📋 Chưa có đầu việc nào trong hệ thống</p>
                        <p style="margin-top: 10px;">Nhấn "Thêm đầu việc mới" để tạo đầu việc</p>
                    </div>
                </c:when>
                <c:otherwise>
                    <div class="task-grid">
                        <c:forEach items="${tasks}" var="task">
                            <div class="task-card">
                                <h3>📌 ${task.nameTask}</h3>
                                <p>${task.description != null ? task.description : 'Không có mô tả'}</p>
                                <form action="${pageContext.request.contextPath}/partner-manager/contracts/tasks/select/${task.id}"
                                      method="post">
                                    <button type="submit" class="btn btn-primary">
                                        ➕ Chọn đầu việc này
                                    </button>
                                </form>
                            </div>
                        </c:forEach>
                    </div>
                </c:otherwise>
            </c:choose>

            <div class="action-buttons">
                <a href="${pageContext.request.contextPath}/partner-manager/contracts/tasks/add-new"
                   class="btn btn-success">
                    ➕ Thêm đầu việc mới
                </a>

                <c:if test="${not empty selectedTaskContracts}">
                    <a href="${pageContext.request.contextPath}/partner-manager/contracts/confirm"
                       class="btn btn-warning">
                        👁️ Xem & Xác nhận hợp đồng
                    </a>
                </c:if>

                <a href="${pageContext.request.contextPath}/partner-manager/contracts/cancel"
                   class="btn btn-secondary"
                   onclick="return confirm('Bạn có chắc muốn hủy hợp đồng này?')">
                    ❌ Hủy bỏ
                </a>
            </div>
        </div>
    </div>
</body>
</html>