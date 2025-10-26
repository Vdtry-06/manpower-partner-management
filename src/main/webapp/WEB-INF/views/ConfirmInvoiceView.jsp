<%-- ConfirmInvoiceView.jsp --%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Xác Nhận Hóa Đơn</title>
</head>
<body>
    <h2>Xác Nhận Hóa Đơn</h2>
    <p>Tên Đối Tác: ${invoice.namePatner}</p>
    <p>Tên Hợp Đồng: ${invoice.nameContract}</p>
    <p>Nhân Viên Kế Toán: ${invoice.nameAccountant}</p>
    <p>Ngày Hóa Đơn: ${invoice.invoiceResponse.invoiceDate}</p>
    <p>Số Tiền Thanh Toán: ${invoice.invoiceResponse.paymentAmount} VNĐ</p>
    <p>Phương Thức Thanh Toán: ${invoice.invoiceResponse.paymentMethod}</p>
    <p>Số Tiền Còn Lại: ${invoice.invoiceResponse.remainingAmount} VNĐ</p>

    <form action="/invoice/confirm" method="post">
        <input type="hidden" name="id" value="${invoice.invoiceResponse.id}">
        <input type="submit" value="Xác Nhận">
    </form>
    <a href="/contract/detail?contractId=${contractId}">Quay Lại</a>
    <button onclick="window.print()">In Hóa Đơn</button>
</body>
</html>