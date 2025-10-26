<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Sửa Ca làm việc</title>
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
            max-width: 900px;
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

        .task-badge {
            display: inline-block;
            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
            color: white;
            padding: 8px 16px;
            border-radius: 20px;
            font-weight: 600;
            margin-top: 10px;
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

        .form-card {
            background: white;
            border-radius: 15px;
            padding: 40px;
            box-shadow: 0 5px 20px rgba(0,0,0,0.08);
        }

        .form-group {
            margin-bottom: 25px;
        }

        .form-group label {
            display: block;
            margin-bottom: 8px;
            color: #333;
            font-weight: 600;
            font-size: 14px;
        }

        .form-group label .required {
            color: #dc3545;
            margin-left: 3px;
        }

        .form-group input {
            width: 100%;
            padding: 12px 15px;
            border: 2px solid #e0e0e0;
            border-radius: 8px;
            font-size: 14px;
            transition: all 0.3s ease;
        }

        .form-group input:focus {
            outline: none;
            border-color: #667eea;
            box-shadow: 0 0 0 3px rgba(102, 126, 234, 0.1);
        }

        .form-group .error {
            color: #dc3545;
            font-size: 13px;
            margin-top: 5px;
            display: block;
        }

        .form-group input.error-input {
            border-color: #dc3545;
        }

        .form-row {
            display: grid;
            grid-template-columns: 1fr 1fr;
            gap: 20px;
        }

        .form-actions {
            display: flex;
            gap: 15px;
            margin-top: 30px;
            padding-top: 30px;
            border-top: 2px solid #f0f0f0;
        }

        .btn {
            padding: 14px 30px;
            border-radius: 8px;
            font-weight: 600;
            font-size: 15px;
            cursor: pointer;
            transition: all 0.3s ease;
            border: none;
            text-decoration: none;
            display: inline-block;
        }

        .btn-primary {
            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
            color: white;
            flex: 1;
        }

        .btn-primary:hover {
            transform: translateY(-2px);
            box-shadow: 0 5px 15px rgba(102, 126, 234, 0.4);
        }

        .btn-secondary {
            background: #e0e0e0;
            color: #333;
        }

        .btn-secondary:hover {
            background: #d0d0d0;
        }

        .form-info {
            background: #fff3cd;
            border-left: 4px solid #ffc107;
            padding: 15px;
            margin-bottom: 25px;
            border-radius: 5px;
            font-size: 14px;
            color: #856404;
        }

        @media (max-width: 768px) {
            .form-row {
                grid-template-columns: 1fr;
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
            <h1>✏️ Sửa ca làm việc</h1>
            <p>Cập nhật thông tin ca làm việc</p>
            <span class="task-badge">📌 ${taskName}</span>
        </div>

        <div class="form-card">
            <c:if test="${not empty errorMessage}">
                <div class="alert alert-danger">
                    ${errorMessage}
                </div>
            </c:if>

            <div class="form-info">
                ⚠️ Chỉnh sửa thông tin ca làm việc và nhấn "Cập nhật" để lưu thay đổi
            </div>

            <form:form action="${pageContext.request.contextPath}/partner-manager/contracts/task-contracts/${taskContractId}/shifts/${shiftId}/edit"
                       method="post"
                       modelAttribute="shiftRequest">

                <div class="form-row">
                    <div class="form-group">
                        <label for="workDate">
                            Ngày làm việc <span class="required">*</span>
                        </label>
                        <form:input path="workDate"
                                   type="date"
                                   id="workDate"
                                   cssClass="${not empty errors.workDate ? 'error-input' : ''}" />
                        <form:errors path="workDate" cssClass="error" />
                    </div>

                    <div class="form-group">
                        <label for="workerCount">
                            Số lượng nhân công <span class="required">*</span>
                        </label>
                        <form:input path="workerCount"
                                   type="number"
                                   id="workerCount"
                                   min="1"
                                   cssClass="${not empty errors.workerCount ? 'error-input' : ''}" />
                        <form:errors path="workerCount" cssClass="error" />
                    </div>
                </div>

                <div class="form-row">
                    <div class="form-group">
                        <label for="startTime">
                            Giờ bắt đầu <span class="required">*</span>
                        </label>
                        <form:input path="startTime"
                                   type="time"
                                   id="startTime"
                                   cssClass="${not empty errors.startTime ? 'error-input' : ''}" />
                        <form:errors path="startTime" cssClass="error" />
                    </div>

                    <div class="form-group">
                        <label for="endTime">
                            Giờ kết thúc <span class="required">*</span>
                        </label>
                        <form:input path="endTime"
                                   type="time"
                                   id="endTime"
                                   cssClass="${not empty errors.endTime ? 'error-input' : ''}" />
                        <form:errors path="endTime" cssClass="error" />
                    </div>
                </div>

                <div class="form-group">
                    <label for="shiftUnitPrice">
                        Đơn giá (VNĐ) <span class="required">*</span>
                    </label>
                    <form:input path="shiftUnitPrice"
                               type="number"
                               id="shiftUnitPrice"
                               min="0"
                               cssClass="${not empty errors.shiftUnitPrice ? 'error-input' : ''}" />
                    <form:errors path="shiftUnitPrice" cssClass="error" />
                </div>

                <div class="form-group">
                    <label for="description">
                        Ghi chú
                    </label>
                    <form:input path="description"
                               id="description"
                               placeholder="Ghi chú về ca làm việc..." />
                </div>

                <div class="form-actions">
                    <button type="submit" class="btn btn-primary">
                        💾 Cập nhật
                    </button>
                    <a href="${pageContext.request.contextPath}/partner-manager/contracts/task-contracts/${taskContractId}/add-shift"
                       class="btn btn-secondary">
                        ❌ Hủy bỏ
                    </a>
                </div>
            </form:form>
        </div>
    </div>
</body>
</html>