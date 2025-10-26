<%-- AddPartnerView.jsp --%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Thêm Đối Tác</title>
</head>
<body>
    <h2>Thêm Thông Tin Đối Tác</h2>
    <form action="/partner/add" method="post">
        <label for="namePartner">Tên Đối Tác:</label>
        <input type="text" id="namePartner" name="namePartner" required><br><br>

        <label for="partnerRepresentative">Người Đại Diện:</label>
        <input type="text" id="partnerRepresentative" name="partnerRepresentative" required><br><br>

        <label for="phoneNumber">Số Điện Thoại:</label>
        <input type="text" id="phoneNumber" name="phoneNumber" required><br><br>

        <label for="email">Email:</label>
        <input type="email" id="email" name="email" required><br><br>

        <label for="address">Địa Chỉ:</label>
        <input type="text" id="address" name="address"><br><br>

        <label for="taxCode">Mã Số Thuế:</label>
        <input type="text" id="taxCode" name="taxCode" required><br><br>

        <label for="cooperationDate">Ngày Hợp Tác:</label>
        <input type="date" id="cooperationDate" name="cooperationDate" required><br><br>

        <label for="description">Mô Tả:</label>
        <textarea id="description" name="description"></textarea><br><br>

        <input type="submit" value="Lưu">
        <input type="button" value="Hủy" onclick="window.location.href='/partner/manage';">
    </form>
    <c:if test="${not empty error}">
        <p style="color:red">${error}</p>
    </c:if>
</body>
</html>