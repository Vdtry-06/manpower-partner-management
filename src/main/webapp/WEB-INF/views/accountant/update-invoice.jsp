<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Cập nhật hóa đơn</title>
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
        }

        .form-control {
            width: 100%;
            padding: 8px 12px;
            border: 2px solid #e0e0e0;
            border-radius: 6px;
            font-size: 14px;
        }

        .form-control:focus {
            outline: none;
            border-color: #11998e;
        }

        .form-select {
            width: 100%;
            padding: 8px 12px;
            border: 2px solid #e0e0e0;
            border-radius: 6px;
            font-size: 14px;
            cursor: pointer;
        }

        .btn-group {
            display: flex;
            gap: 10px;
            margin-top: 25px;
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

        .alert-info {
            background: #d1ecf1;
            color: #0c5460;
            border: 1px solid #bee5eb;
        }

        .input-hint {
            font-size: 12px;
            color: #6c757d;
            margin-top: 5px;
        }
    </style>
</head>
<body>
    <nav class="navbar">
        <div class="navbar-brand">
            Partner Management - Cập nhật hóa đơn
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
            <h2 class="card-title">💰 Cập nhật hóa đơn</h2>

            <div class="info-section">
                <h3>Thông tin ca làm việc</h3>
                <p><strong>Mã ca làm:</strong> CLV${invoice.shiftId}</p>
                <p><strong>Đầu việc:</strong> ${invoice.taskName}</p>
                <p><strong>Ngày làm:</strong> ${invoice.workDate}</p>
                <p><strong>Giờ làm:</strong> ${invoice.startTime} - ${invoice.endTime}</p>
                <p><strong>Số lượng nhân công:</strong> ${invoice.workerCount} người</p>
                <p><strong>Đơn giá:</strong> <fmt:formatNumber value="${invoice.shiftUnitPrice}" type="number" /> VNĐ</p>
                <p><strong>Tổng giá trị ca:</strong> <fmt:formatNumber value="${invoice.totalShiftValue}" type="number" /> VNĐ</p>
            </div>

            <div class="alert alert-info">
                <strong>Số tiền còn thiếu của ca làm này:</strong>
                <fmt:formatNumber value="${invoice.remainingAmountAfter * invoice.shiftUnitPrice}" type="number" /> VNĐ
                (${invoice.remainingAmountAfter} công nhân chưa thanh toán)
            </div>

            <form action="${pageContext.request.contextPath}/accountant/save-invoice/${invoice.invoiceId}" method="post">
                <div class="table-responsive">
                    <table>
                        <thead>
                            <tr>
                                <th>STT</th>
                                <th>Mã hóa đơn</th>
                                <th>Mã Ca</th>
                                <th>Số tiền còn thiếu</th>
                                <th>Số tiền thanh toán</th>
                                <th>Phương thức thanh toán</th>
                                <th>Trạng thái hóa đơn</th>
                            </tr>
                        </thead>
                        <tbody>
                            <tr>
                                <td>1</td>
                                <td>HDN${invoice.invoiceId}</td>
                                <td>CLV${invoice.shiftId}</td>
                                <td><fmt:formatNumber value="${invoice.remainingAmountAfter * invoice.shiftUnitPrice}" type="number" /></td>
                                <td>
                                    <input type="number"
                                           name="paymentAmount"
                                           class="form-control"
                                           placeholder="Nhập số tiền"
                                           min="1"
                                           max="${invoice.remainingAmountAfter * invoice.shiftUnitPrice}"
                                           required>
                                </td>
                                <td>
                                    <select name="paymentMethod" class="form-select" required>
                                        <option value="">Chọn...</option>
                                        <option value="CASH">Tiền mặt</option>
                                        <option value="TRANSFER">Chuyển khoản</option>
                                    </select>
                                </td>
                                <td>Chưa thanh toán</td>
                            </tr>
                        </tbody>
                    </table>
                </div>

                <div class="input-hint">
                    * Số tiền thanh toán không được vượt quá số tiền còn thiếu: <strong><fmt:formatNumber value="${invoice.remainingAmountAfter * invoice.shiftUnitPrice}" type="number" /> VNĐ</strong>
                </div>

                <div class="btn-group">
                    <button type="submit" class="btn btn-primary">Lưu</button>
                    <a href="${pageContext.request.contextPath}/accountant/detail-contract/${invoice.contractId}"
                       class="btn btn-secondary">Hủy bỏ</a>
                </div>
            </form>
        </div>
    </div>
</body>
</html>