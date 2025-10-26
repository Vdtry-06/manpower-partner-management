<%-- UpdateInvoiceView.jsp --%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Cập Nhật Hóa Đơn</title>
</head>
<body>
    <h2>Cập Nhật Hóa Đơn</h2>
    <form action="/invoice/update" method="post">
        <label for="paymentMethod">Phương Thức Thanh Toán:</label>
        <select id="paymentMethod" name="paymentMethod">
            <option value="CASH">Tiền Mặt</option>
            <option value="BANK_TRANSFER">Chuyển Khoản</option>
        </select><br><br>

        <label for="paymentAmount">Số Tiền Thanh Toán (VNĐ):</label>
        <input type="number" id="paymentAmount" name="paymentAmount" min="0" required><br><br>

        <input type="hidden" name="shiftId" value="${shiftId}">
        <input type="submit" value="Lưu">
        <a href="/contract/detail?contractId=${contractId}">Quay Lại</a>
    </form>
</body>
</html>