<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Xác nhận hóa đơn</title>
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
            max-width: 1000px;
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
            text-align: center;
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
            font-size: 16px;
        }

        .info-section p {
            color: #555;
            margin: 5px 0;
            font-size: 14px;
        }

        .table-responsive {
            overflow-x: auto;
            margin: 20px 0;
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
            font-size: 13px;
        }

        td {
            padding: 12px;
            border-bottom: 1px solid #dee2e6;
            color: #555;
            font-size: 14px;
        }

        .payment-summary {
            background: #e8f5e9;
            padding: 25px;
            border-radius: 10px;
            margin: 20px 0;
            text-align: center;
        }

        .payment-summary h3 {
            color: #2e7d32;
            font-size: 20px;
            margin-bottom: 10px;
        }

        .payment-summary p {
            color: #1b5e20;
            font-size: 16px;
            margin: 5px 0;
        }

        .payment-amount {
            font-size: 28px;
            font-weight: 700;
            color: #1b5e20;
            margin: 15px 0;
        }

        .btn-group {
            display: flex;
            gap: 10px;
            justify-content: center;
            margin-top: 30px;
        }

        .btn {
            padding: 15px 40px;
            border: none;
            border-radius: 8px;
            font-weight: 600;
            cursor: pointer;
            transition: all 0.3s ease;
            text-decoration: none;
            display: inline-block;
            font-size: 16px;
        }

        .btn-success {
            background: linear-gradient(135deg, #2e7d32 0%, #66bb6a 100%);
            color: white;
        }

        .btn-success:hover {
            transform: translateY(-2px);
            box-shadow: 0 5px 15px rgba(46, 125, 50, 0.3);
        }

        .btn-danger {
            background: #dc3545;
            color: white;
        }

        .btn-danger:hover {
            background: #c82333;
        }

        .btn-print {
            background: #007bff;
            color: white;
        }

        .btn-print:hover {
            background: #0056b3;
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

        .confirm-icon {
            text-align: center;
            font-size: 60px;
            margin-bottom: 20px;
        }

        .method-badge {
            display: inline-block;
            padding: 8px 16px;
            border-radius: 20px;
            font-weight: 600;
            font-size: 14px;
        }

        .method-cash {
            background: #fff3cd;
            color: #856404;
        }

        .method-transfer {
            background: #d1ecf1;
            color: #0c5460;
        }

        @media print {
            body {
                background: white;
            }

            .navbar {
                display: none;
            }

            .btn-group {
                display: none !important;
            }

            .alert {
                display: none;
            }

            .card {
                box-shadow: none;
                padding: 20px;
            }
        }
    </style>
</head>
<body>
    <nav class="navbar">
        <div class="navbar-brand">
            Partner Management - Xác nhận hóa đơn
        </div>
        <div class="navbar-user">
            ${fullname}
        </div>
    </nav>

    <div class="container">
        <c:if test="${error != null}">
            <div class="alert alert-danger">${error}</div>
        </c:if>

        <div class="card">
            <div class="confirm-icon">✅</div>
            <h2 class="card-title">Xác nhận thông tin hóa đơn</h2>

            <div class="info-section">
                <h3>Thông tin đối tác</h3>
                <p><strong>Tên đối tác:</strong> ${invoice.partnerName}</p>
                <p><strong>Người đại diện:</strong> ${invoice.partnerRepresentative}</p>
                <p><strong>Số điện thoại:</strong> ${invoice.partnerPhone}</p>
            </div>

            <div class="info-section">
                <h3>Thông tin hợp đồng</h3>
                <p><strong>Mã hợp đồng:</strong> HĐ${invoice.contractId}</p>
                <p><strong>Tên hợp đồng:</strong> ${invoice.contractName}</p>
            </div>

            <div class="info-section">
                <h3>Nhân viên phụ trách</h3>
                <p><strong>Kế toán:</strong> ${invoice.accountantName}</p>
                <p><strong>Ngày lập hóa đơn:</strong>
                    <jsp:useBean id="now" class="java.util.Date"/>
                    <fmt:formatDate value="${now}" pattern="dd/MM/yyyy"/>
                </p>
            </div>

            <h3 style="color: #333; margin: 25px 0 15px 0; text-align: center;">Ca làm việc thanh toán</h3>

            <div class="table-responsive">
                <table>
                    <thead>
                        <tr>
                            <th>STT</th>
                            <th>Mã CLV</th>
                            <th>Ngày làm</th>
                            <th>Giờ bắt đầu</th>
                            <th>Giờ kết thúc</th>
                            <th>Số lượng nhân công</th>
                            <th>Đơn giá (VNĐ)</th>
                            <th>Số tiền còn thiếu</th>
                        </tr>
                    </thead>
                    <tbody>
                        <tr>
                            <td>1</td>
                            <td>CLV${invoice.shiftId}</td>
                            <td>${invoice.workDate}</td>
                            <td>${invoice.startTime}</td>
                            <td>${invoice.endTime}</td>
                            <td>${invoice.workerCount}</td>
                            <td><fmt:formatNumber value="${invoice.shiftUnitPrice}" type="number" /></td>
                            <td>
                                <c:choose>
                                    <c:when test="${invoice.remainingAmountAfter == 0}">
                                        <strong style="color: #28a745;">0 VNĐ (Đã thanh toán đủ)</strong>
                                    </c:when>
                                    <c:otherwise>
                                        <fmt:formatNumber value="${invoice.remainingAmountAfter * invoice.shiftUnitPrice}" type="number" /> VNĐ
                                    </c:otherwise>
                                </c:choose>
                            </td>
                        </tr>
                    </tbody>
                </table>
            </div>

            <div class="payment-summary">
                <h3>💰 Thông tin thanh toán</h3>
                <div class="payment-amount">
                    <fmt:formatNumber value="${invoice.paymentAmount}" type="number" /> VNĐ
                </div>
                <p><strong>Phương thức thanh toán:</strong>
                    <c:choose>
                        <c:when test="${invoice.paymentMethod == 'CASH'}">
                            <span class="method-badge method-cash">Tiền mặt</span>
                        </c:when>
                        <c:when test="${invoice.paymentMethod == 'TRANSFER'}">
                            <span class="method-badge method-transfer">Chuyển khoản</span>
                        </c:when>
                        <c:otherwise>
                            ${invoice.paymentMethod}
                        </c:otherwise>
                    </c:choose>
                </p>
            </div>

            <form action="${pageContext.request.contextPath}/accountant/finalize-invoice/${invoice.invoiceId}" method="post">
                <div class="btn-group">
                    <button type="button" onclick="window.print()" class="btn btn-print">🖨️ In hóa đơn</button>
                    <button type="submit" class="btn btn-success">✅ Xác nhận & Hoàn tất</button>
                    <a href="${pageContext.request.contextPath}/accountant/detail-contract/${invoice.contractId}"
                       class="btn btn-danger">❌ Hủy bỏ</a>
                </div>
            </form>
        </div>
    </div>

    <script>
        // Ẩn các nút khi in
        window.onbeforeprint = function() {
            document.querySelectorAll('.btn-group').forEach(el => el.style.display = 'none');
            document.querySelectorAll('.navbar').forEach(el => el.style.display = 'none');
        };

        window.onafterprint = function() {
            document.querySelectorAll('.btn-group').forEach(el => el.style.display = 'flex');
            document.querySelectorAll('.navbar').forEach(el => el.style.display = 'flex');
        };
    </script>
</body>
</html>