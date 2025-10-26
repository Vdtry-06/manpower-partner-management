<%-- AddShiftView.jsp --%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Thêm Ca Làm Việc</title>
</head>
<body>
    <h2>Thêm Ca Làm Việc</h2>
    <form action="/shift/add" method="post">
        <label for="workDate">Ngày Làm:</label>
        <input type="date" id="workDate" name="workDate" min="${today}" required><br><br>

        <label for="startTime">Giờ Bắt Đầu:</label>
        <input type="time" id="startTime" name="startTime" required><br><br>

        <label for="endTime">Giờ Kết Thúc:</label>
        <input type="time" id="endTime" name="endTime" required><br><br>

        <label for="workerCount">Số Nhân Công:</label>
        <input type="number" id="workerCount" name="workerCount" min="1" required><br><br>

        <label for="shiftUnitPrice">Đơn Giá (VNĐ):</label>
        <input type="number" id="shiftUnitPrice" name="shiftUnitPrice" min="0" required><br><br>

        <input type="hidden" name="contractId" value="${contractId}">
        <input type="submit" value="Lưu Ca">
    </form>
    <a href="/shift/complete-task?contractId=${contractId}">Hoàn Thành Đầu Việc</a><br>
    <a href="/task/list?contractId=${contractId}">Quay Lại</a>
</body>
</html>