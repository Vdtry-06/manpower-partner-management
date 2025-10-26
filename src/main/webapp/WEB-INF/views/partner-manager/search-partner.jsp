<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Tìm kiếm Đối tác</title>
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

        .search-card {
            background: white;
            border-radius: 15px;
            padding: 30px;
            box-shadow: 0 5px 20px rgba(0,0,0,0.08);
            margin-bottom: 30px;
        }

        .search-form {
            display: flex;
            gap: 15px;
            margin-bottom: 20px;
        }

        .search-input {
            flex: 1;
            padding: 14px 20px;
            border: 2px solid #e0e0e0;
            border-radius: 8px;
            font-size: 15px;
            transition: all 0.3s ease;
        }

        .search-input:focus {
            outline: none;
            border-color: #667eea;
            box-shadow: 0 0 0 3px rgba(102, 126, 234, 0.1);
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

        .btn-success {
            background: linear-gradient(135deg, #56ab2f 0%, #a8e063 100%);
            color: white;
        }

        .btn-success:hover {
            transform: translateY(-2px);
            box-shadow: 0 5px 15px rgba(86, 171, 47, 0.4);
        }

        .action-buttons {
            display: flex;
            gap: 15px;
            justify-content: flex-end;
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

        .alert-success {
            background: #d4edda;
            color: #155724;
            border: 1px solid #c3e6cb;
        }

        .results-card {
            background: white;
            border-radius: 15px;
            padding: 30px;
            box-shadow: 0 5px 20px rgba(0,0,0,0.08);
        }

        .partner-table {
            width: 100%;
            border-collapse: collapse;
            margin-top: 20px;
        }

        .partner-table thead {
            background: #f8f9fa;
        }

        .partner-table th {
            padding: 15px;
            text-align: left;
            font-weight: 600;
            color: #333;
            border-bottom: 2px solid #dee2e6;
        }

        .partner-table td {
            padding: 15px;
            border-bottom: 1px solid #dee2e6;
            color: #666;
        }

        .partner-table tbody tr {
            cursor: pointer;
            transition: background 0.2s ease;
        }

        .partner-table tbody tr:hover {
            background: #f8f9fa;
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
            <h1>🔍 Tìm kiếm đối tác để ký hợp đồng</h1>
            <p>Nhập tên đối tác hoặc một phần tên để tìm kiếm</p>
        </div>

        <div class="search-card">
            <c:if test="${not empty errorMessage}">
                <div class="alert alert-danger">
                    ${errorMessage}
                </div>
            </c:if>

            <div class="info-box">
                💡 Nhập tên hoặc một phần tên đối tác vào ô tìm kiếm và nhấn "Tìm kiếm"
            </div>

            <form action="${pageContext.request.contextPath}/partner-manager/contracts/search-partner"
                  method="post" class="search-form">
                <input type="text"
                       name="keyword"
                       class="search-input"
                       placeholder="Ví dụ: Cty X, Công ty ABC..."
                       value="${keyword}"
                       required
                       autofocus>
                <button type="submit" class="btn btn-primary">
                    🔍 Tìm kiếm
                </button>
            </form>

            <div class="action-buttons">
                <a href="${pageContext.request.contextPath}/partner-manager/partners/add"
                   class="btn btn-success">
                    ➕ Thêm đối tác mới
                </a>
                <a href="${pageContext.request.contextPath}/partner-manager/home"
                   class="btn btn-secondary">
                    ⬅ Quay lại
                </a>
            </div>
        </div>

        <c:if test="${not empty keyword}">
            <div class="results-card">
                <h2 style="margin-bottom: 10px; color: #333;">Kết quả tìm kiếm: "${keyword}"</h2>

                <c:choose>
                    <c:when test="${empty partners}">
                        <div class="no-data">
                            <p>❌ Không tìm thấy đối tác nào với từ khóa "${keyword}"</p>
                            <p style="margin-top: 10px;">Vui lòng thử lại với từ khóa khác hoặc thêm đối tác mới</p>
                        </div>
                    </c:when>
                    <c:otherwise>
                        <table class="partner-table">
                            <thead>
                                <tr>
                                    <th>STT</th>
                                    <th>Mã đối tác</th>
                                    <th>Tên đối tác</th>
                                    <th>Người đại diện</th>
                                    <th>SĐT</th>
                                    <th>Email</th>
                                    <th>Thao tác</th>
                                </tr>
                            </thead>
                            <tbody>
                                <c:forEach items="${partners}" var="partner" varStatus="status">
                                    <tr>
                                        <td>${status.index + 1}</td>
                                        <td>DT${partner.id}</td>
                                        <td><strong>${partner.namePartner}</strong></td>
                                        <td>${partner.partnerRepresentative}</td>
                                        <td>${partner.phoneNumber}</td>
                                        <td>${partner.email}</td>
                                        <td>
                                            <a href="${pageContext.request.contextPath}/partner-manager/contracts/select-partner/${partner.id}"
                                               class="btn btn-primary"
                                               style="padding: 8px 16px; font-size: 13px;">
                                                ✓ Chọn
                                            </a>
                                        </td>
                                    </tr>
                                </c:forEach>
                            </tbody>
                        </table>
                    </c:otherwise>
                </c:choose>
            </div>
        </c:if>
    </div>
</body>
</html>