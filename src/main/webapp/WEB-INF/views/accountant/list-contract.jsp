<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Danh sách hợp đồng</title>
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

        .partner-info {
            background: #f8f9fa;
            padding: 20px;
            border-radius: 10px;
            margin-bottom: 25px;
        }

        .partner-info h3 {
            color: #11998e;
            margin-bottom: 10px;
            font-size: 20px;
        }

        .partner-info p {
            color: #555;
            margin: 5px 0;
            font-size: 15px;
        }

        .partner-info strong {
            color: #333;
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

        .status-badge {
            padding: 5px 12px;
            border-radius: 20px;
            font-size: 12px;
            font-weight: 600;
            display: inline-block;
        }

        .status-active {
            background: #d4edda;
            color: #155724;
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
        }

        .btn-secondary {
            background: #6c757d;
            color: white;
        }

        .btn-secondary:hover {
            background: #5a6268;
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
            Partner Management - Danh sách hợp đồng
        </div>
        <div class="navbar-user">
            ${fullname}
        </div>
    </nav>

    <div class="container">
        <div class="card">
            <h2 class="card-title">📋 Danh sách hợp đồng</h2>

            <div class="partner-info">
                <h3>Đối tác: ${partner.namePartner}</h3>
                <p><strong>Thông tin liên hệ:</strong> ${partner.partnerRepresentative} – ${partner.phoneNumber}</p>
                <p><strong>Email:</strong> ${partner.email}</p>
            </div>

            <h3 style="color: #333; margin-bottom: 15px;">Danh sách hợp đồng hiện tại</h3>

            <c:if test="${contracts != null && not empty contracts}">
                <div class="table-responsive">
                    <table>
                        <thead>
                            <tr>
                                <th>STT</th>
                                <th>Mã HĐ</th>
                                <th>Tên HĐ</th>
                                <th>Ngày BĐ</th>
                                <th>Ngày KT</th>
                                <th>Tổng GTHĐ</th>
                                <th>Trạng thái hợp đồng</th>
                            </tr>
                        </thead>
                        <tbody>
                            <c:forEach var="contract" items="${contracts}" varStatus="status">
                                <tr onclick="window.location.href='${pageContext.request.contextPath}/accountant/detail-contract/${contract.id}'">
                                    <td>${status.index + 1}</td>
                                    <td>HĐ${contract.id}</td>
                                    <td><strong>${contract.contractName}</strong></td>
                                    <td>${contract.startDate}</td>
                                    <td>${contract.endDate}</td>
                                    <td><fmt:formatNumber value="${contract.totalContractValue}" type="number" /> VNĐ</td>
                                    <td>
                                        <c:choose>
                                            <c:when test="${contract.status == 'ACTIVE'}">
                                                <span class="status-badge status-active">Hoạt động</span>
                                            </c:when>
                                            <c:otherwise>
                                                <span class="status-badge">${contract.status}</span>
                                            </c:otherwise>
                                        </c:choose>
                                    </td>
                                </tr>
                            </c:forEach>
                        </tbody>
                    </table>
                </div>
            </c:if>

            <c:if test="${contracts == null || empty contracts}">
                <div class="alert alert-warning">
                    Đối tác này chưa có hợp đồng nào đang hoạt động
                </div>
                <div class="no-data">
                    <p>📋 Không có hợp đồng nào</p>
                </div>
            </c:if>

            <div style="margin-top: 25px;">
                <a href="${pageContext.request.contextPath}/accountant/search-partner" class="btn btn-secondary">Quay lại</a>
            </div>
        </div>
    </div>
</body>
</html>