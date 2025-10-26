<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Partner Manager Home</title>
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

        .user-info {
            text-align: right;
        }

        .user-name {
            font-weight: 600;
            font-size: 16px;
        }

        .user-role {
            font-size: 12px;
            opacity: 0.9;
        }

        .btn-logout {
            background: rgba(255,255,255,0.2);
            color: white;
            border: 2px solid white;
            padding: 8px 20px;
            border-radius: 6px;
            cursor: pointer;
            font-weight: 600;
            transition: all 0.3s ease;
            text-decoration: none;
            display: inline-block;
        }

        .btn-logout:hover {
            background: white;
            color: #667eea;
        }

        .container {
            max-width: 1200px;
            margin: 40px auto;
            padding: 0 20px;
        }

        .welcome-card {
            background: white;
            border-radius: 15px;
            padding: 40px;
            box-shadow: 0 5px 20px rgba(0,0,0,0.08);
            margin-bottom: 30px;
        }

        .welcome-card h1 {
            color: #333;
            font-size: 32px;
            margin-bottom: 15px;
        }

        .welcome-card p {
            color: #666;
            font-size: 16px;
            line-height: 1.6;
        }

        .dashboard-grid {
            display: grid;
            grid-template-columns: repeat(auto-fit, minmax(280px, 1fr));
            gap: 25px;
            margin-top: 30px;
        }

        .dashboard-card {
            background: white;
            border-radius: 15px;
            padding: 30px;
            box-shadow: 0 5px 20px rgba(0,0,0,0.08);
            transition: transform 0.3s ease, box-shadow 0.3s ease;
            cursor: pointer;
            text-decoration: none;
            color: inherit;
            display: block;
        }

        .dashboard-card:hover {
            transform: translateY(-5px);
            box-shadow: 0 10px 30px rgba(0,0,0,0.15);
        }

        .card-icon {
            width: 60px;
            height: 60px;
            border-radius: 12px;
            display: flex;
            align-items: center;
            justify-content: center;
            font-size: 28px;
            margin-bottom: 20px;
        }

        .card-icon.partners {
            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
        }

        .card-icon.contracts {
            background: linear-gradient(135deg, #f093fb 0%, #f5576c 100%);
        }

        .card-icon.tasks {
            background: linear-gradient(135deg, #4facfe 0%, #00f2fe 100%);
        }

        .card-icon.reports {
            background: linear-gradient(135deg, #43e97b 0%, #38f9d7 100%);
        }

        .dashboard-card h3 {
            color: #333;
            font-size: 20px;
            margin-bottom: 10px;
        }

        .dashboard-card p {
            color: #666;
            font-size: 14px;
            line-height: 1.6;
        }
    </style>
</head>
<body>
    <nav class="navbar">
        <div class="navbar-brand">
            Partner Management
        </div>
        <div class="navbar-user">
            <div class="user-info">
                <div class="user-name">${fullname}</div>
                <div class="user-role">Partner Manager</div>
            </div>
            <a href="${pageContext.request.contextPath}/auth/logout" class="btn-logout">
                Đăng xuất
            </a>
        </div>
    </nav>

    <div class="container">
        <div class="welcome-card">
            <h1>Xin chào, ${fullname}!</h1>
            <p>Chào mừng bạn đến với hệ thống quản lý đối tác. Bạn có thể quản lý đối tác, hợp đồng và theo dõi các công việc từ đây.</p>
        </div>

        <div class="dashboard-grid">
            <a href="${pageContext.request.contextPath}/partner-manager/partners" class="dashboard-card">
                <div class="card-icon partners">👥</div>
                <h3>Quản lý Đối tác</h3>
                <p>Xem, thêm, sửa và quản lý thông tin các đối tác của công ty</p>
            </a>

            <a href="#" class="dashboard-card">
                <div class="card-icon contracts">📄</div>
                <h3>Ký Hợp đồng</h3>
                <p>Theo dõi và quản lý các hợp đồng với đối tác</p>
            </a>

            <!-- <a href="#" class="dashboard-card">
                <div class="card-icon tasks">✅</div>
                <h3>Quản lý Công việc</h3>
                <p>Phân công và theo dõi tiến độ các công việc</p>
            </a> -->

            <!-- <a href="#" class="dashboard-card">
                <div class="card-icon reports">📊</div>
                <h3>Báo cáo</h3>
                <p>Xem báo cáo thống kê và phân tích dữ liệu</p>
            </a> -->
        </div>
    </div>
</body>
</html>