<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Tìm kiếm đối tác</title>
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
            background: linear-gradient(135deg, #11998e 0%, #38ef7d 100%);
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
            gap: 15px;
        }

        .user-name {
            font-weight: 600;
        }

        .container {
            max-width: 1200px;
            margin: 40px auto;
            padding: 0 20px;
        }

        .card {
            background: white;
            border-radius: 15px;
            padding: 30px;
            box-shadow: 0 5px 20px rgba(0,0,0,0.08);
            margin-bottom: 30px;
        }

        .card-title {
            font-size: 24px;
            color: #333;
            margin-bottom: 20px;
            padding-bottom: 15px;
            border-bottom: 2px solid #f0f0f0;
        }

        .search-form {
            display: flex;
            gap: 15px;
            margin-bottom: 20px;
        }

        .form-group {
            flex: 1;
        }

        .form-label {
            display: block;
            font-weight: 600;
            color: #555;
            margin-bottom: 8px;
        }

        .form-control {
            width: 100%;
            padding: 12px 15px;
            border: 2px solid #e0e0e0;
            border-radius: 8px;
            font-size: 14px;
            transition: border-color 0.3s ease;
        }

        .form-control:focus {
            outline: none;
            border-color: #11998e;
        }

        .btn-group {
            display: flex;
            gap: 10px;
            margin-top: 28px;
        }

        .btn {
            padding: 12px 30px;
            border: none;
            border-radius: 8px;
            font-weight: 600;
            cursor: pointer;
            transition: all 0.3s ease;
            text-decoration: none;
            display: inline-block;
            text-align: center;
        }

        .btn-primary {
            background: linear-gradient(135deg, #11998e 0%, #38ef7d 100%);
            color: white;
        }

        .btn-primary:hover {
            transform: translateY(-2px);
            box-shadow: 0 5px 15px rgba(17, 153, 142, 0.3);
        }

        .btn-secondary {
            background: #6c757d;
            color: white;
        }

        .btn-secondary:hover {
            background: #5a6268;
        }

        .table-responsive {
            overflow-x: auto;
            margin-top: 20px;
        }

        table {
            width: 100%;
            border-collapse: collapse;
        }

        th {
            background: #f8f9fa;
            padding: 15px;
            text-align: left;
            font-weight: 600;
            color: #333;
            border-bottom: 2px solid #dee2e6;
        }

        td {
            padding: 15px;
            border-bottom: 1px solid #dee2e6;
            color: #555;
        }

        tr:hover {
            background: #f8f9fa;
            cursor: pointer;
        }

        .no-data {
            text-align: center;
            padding: 40px;
            color: #999;
            font-size: 16px;
        }

        .alert {
            padding: 15px 20px;
            border-radius: 8px;
            margin-bottom: 20px;
        }

        .alert-info {
            background: #d1ecf1;
            color: #0c5460;
            border: 1px solid #bee5eb;
        }

        .alert-warning {
            background: #fff3cd;
            color: #856404;
            border: 1px solid #ffeaa7;
        }
    </style>
</head>
<body>
    <nav class="navbar">
        <div class="navbar-brand">
            Partner Management - Tìm kiếm đối tác
        </div>
        <div class="navbar-user">
            <span class="user-name">${fullname}</span>
        </div>
    </nav>

    <div class="container">
        <div class="card">
            <h2 class="card-title">🔍 Tìm kiếm đối tác</h2>

            <form action="${pageContext.request.contextPath}/accountant/search-partner" method="get" class="search-form">
                <div class="form-group">
                    <label class="form-label">Tên đối tác:</label>
                    <input type="text"
                           name="keyword"
                           class="form-control"
                           placeholder="Nhập tên đối tác hoặc một phần tên..."
                           value="${keyword}"
                           required>
                </div>
                <div class="btn-group">
                    <button type="submit" class="btn btn-primary">Tìm kiếm</button>
                    <a href="${pageContext.request.contextPath}/accountant/home" class="btn btn-secondary">Quay lại</a>
                </div>
            </form>
        </div>

        <c:if test="${searched != null && searched}">
            <div class="card">
                <h2 class="card-title">Kết quả tìm kiếm</h2>

                <c:if test="${partners != null && not empty partners}">
                    <div class="alert alert-info">
                        Tìm thấy ${partners.size()} đối tác phù hợp với từ khóa "<strong>${keyword}</strong>"
                    </div>

                    <div class="table-responsive">
                        <table>
                            <thead>
                                <tr>
                                    <th>STT</th>
                                    <th>Mã đối tác</th>
                                    <th>Tên đối tác</th>
                                    <th>Người đại diện</th>
                                    <th>SĐT</th>
                                    <th>Email</th>
                                </tr>
                            </thead>
                            <tbody>
                                <c:forEach var="partner" items="${partners}" varStatus="status">
                                    <tr onclick="window.location.href='${pageContext.request.contextPath}/accountant/list-contract/${partner.id}'">
                                        <td>${status.index + 1}</td>
                                        <td>DT${partner.id}</td>
                                        <td><strong>${partner.namePartner}</strong></td>
                                        <td>${partner.partnerRepresentative}</td>
                                        <td>${partner.phoneNumber}</td>
                                        <td>${partner.email}</td>
                                    </tr>
                                </c:forEach>
                            </tbody>
                        </table>
                    </div>
                </c:if>

                <c:if test="${partners == null || empty partners}">
                    <div class="alert alert-warning">
                        Không tìm thấy đối tác nào phù hợp với từ khóa "<strong>${keyword}</strong>"
                    </div>
                    <div class="no-data">
                        <p>Vui lòng thử lại với từ khóa khác</p>
                    </div>
                </c:if>
            </div>
        </c:if>

        <c:if test="${searched == null || !searched}">
            <div class="card">
                <div class="no-data">
                    <p>📋 Nhập tên đối tác và nhấn "Tìm kiếm" để bắt đầu</p>
                </div>
            </div>
        </c:if>
    </div>
</body>
</html>