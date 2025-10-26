<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Chi tiết hợp đồng</title>
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

        .container {
            max-width: 1400px;
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

        .info-section {
            background: #f8f9fa;
            padding: 20px;
            border-radius: 10px;
            margin-bottom: 20px;
        }

        .info-section h3 {
            color: #11998e;
            margin-bottom: 10px;
            font-size: 18px;
        }

        .info-section p {
            color: #555;
            margin: 5px 0;
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
            padding: 12px;
            text-align: left;
            font-weight: 600;
            color: #333;
            border-bottom: 2px solid #dee2e6;
            font-size: 14px;
        }

        td {
            padding: 12px;
            border-bottom: 1px solid #dee2e6;
            color: #555;
            font-size: 14px;
        }

        tr:hover {
            background: #f8f9fa;
        }

        .clickable-row {
            cursor: pointer;
        }

        .clickable-row:hover {
            background: #e9ecef;
        }

        .total-section {
            background: #e8f5e9;
            padding: 20px;
            border-radius: 10px;
            margin-top: 20px;
            text-align: right;
        }

        .total-section h3 {
            color: #2e7d32;
            font-size: 22px;
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

        .remaining-badge {
            padding: 4px 10px;
            border-radius: 15px;
            font-size: 12px;
            font-weight: 600;
        }

        .remaining-full {
            background: #dc3545;
            color: white;
        }

        .remaining-partial {
            background: #ffc107;
            color: #333;
        }

        .remaining-paid {
            background: #28a745;
            color: white;
        }
    </style>
</head>
<body>
    <nav class="navbar">
        <div class="navbar-brand">
            Partner Management - Chi tiết hợp đồng
        </div>
        <div class="navbar-user">
            ${fullname}
        </div>
    </nav>

    <div class="container">
        <c:if test="${success != null}">
            <div class="alert alert-success">${success}</div>
        </c:if>
        <c:if test="${error != null}">
            <div class="alert alert-danger">${error}</div>
        </c:if>

        <div class="card">
            <h2 class="card-title">📄 Chi tiết hợp đồng</h2>

            <div class="info-section">
                <h3>Thông tin đối tác: ${contract.partnerName} – ${contract.partnerRepresentative}</h3>
                <p><strong>SĐT:</strong> ${contract.partnerPhone}</p>
                <p><strong>Email:</strong> ${contract.partnerEmail}</p>
            </div>

            <div class="info-section">
                <h3>Hợp đồng: ${contract.contractName}</h3>
                <p><strong>Mã hợp đồng:</strong> HĐ${contract.id}</p>
                <p><strong>Ngày bắt đầu:</strong> ${contract.startDate}</p>
                <p><strong>Ngày kết thúc:</strong> ${contract.endDate}</p>
            </div>

            <h3 style="color: #333; margin: 25px 0 15px 0;">Danh sách ca làm việc & đầu việc</h3>

            <div class="table-responsive">
                <table>
                    <thead>
                        <tr>
                            <th>STT</th>
                            <th>Mã CLV</th>
                            <th>Mã ĐV</th>
                            <th>Tên đầu việc</th>
                            <th>Ngày làm</th>
                            <th>Giờ BĐ</th>
                            <th>Giờ KT</th>
                            <th>SL nhân công</th>
                            <th>Đơn giá (VNĐ)</th>
                            <th>Số tiền còn thiếu</th>
                        </tr>
                    </thead>
                    <tbody>
                        <c:set var="stt" value="1"/>
                        <c:forEach var="taskContract" items="${contract.taskContracts}">
                            <c:forEach var="shift" items="${taskContract.shifts}">
                                <tr class="clickable-row"
                                    onclick="window.location.href='${pageContext.request.contextPath}/accountant/update-invoice/${shift.id}'">
                                    <td>${stt}</td>
                                    <td>CLV${shift.id}</td>
                                    <td>DV${taskContract.taskId}</td>
                                    <td><strong>${taskContract.taskName}</strong></td>
                                    <td>${shift.workDate}</td>
                                    <td>${shift.startTime}</td>
                                    <td>${shift.endTime}</td>
                                    <td>${shift.workerCount}</td>
                                    <td><fmt:formatNumber value="${shift.shiftUnitPrice}" type="number" /></td>
                                    <td>
                                        <c:choose>
                                            <c:when test="${shift.remainingAmount == 0}">
                                                <span class="remaining-badge remaining-paid">Đã thanh toán</span>
                                            </c:when>
                                            <c:when test="${shift.remainingAmount == shift.workerCount}">
                                                <span class="remaining-badge remaining-full">
                                                    <fmt:formatNumber value="${shift.remainingAmount * shift.shiftUnitPrice}" type="number" /> VNĐ
                                                </span>
                                            </c:when>
                                            <c:otherwise>
                                                <span class="remaining-badge remaining-partial">
                                                    <fmt:formatNumber value="${shift.remainingAmount * shift.shiftUnitPrice}" type="number" /> VNĐ
                                                </span>
                                            </c:otherwise>
                                        </c:choose>
                                    </td>
                                </tr>
                                <c:set var="stt" value="${stt + 1}"/>
                            </c:forEach>
                        </c:forEach>

                        <c:if test="${empty contract.taskContracts || not contract.hasShifts}">
                            <tr>
                                <td colspan="10" style="text-align: center; padding: 30px; color: #999;">
                                    Chưa có ca làm việc nào
                                </td>
                            </tr>
                        </c:if>
                    </tbody>
                </table>
            </div>

            <div class="total-section">
                <h3>Tổng giá trị hợp đồng: <fmt:formatNumber value="${contract.totalContractValue}" type="number" /> VNĐ</h3>
            </div>

            <div style="margin-top: 25px;">
                <a href="javascript:history.back()" class="btn btn-secondary">Quay lại</a>
            </div>
        </div>
    </div>
</body>
</html>