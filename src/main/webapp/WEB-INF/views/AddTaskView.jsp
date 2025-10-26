<%-- AddTaskView.jsp --%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Thêm Đầu Việc</title>
</head>
<body>
    <h2>Thêm Đầu Việc</h2>
    <form action="/task/add" method="post">
        <label for="nameTask">Tên Đầu Việc:</label>
        <input type="text" id="nameTask" name="nameTask" required><br><br>

        <label for="description">Mô Tả:</label>
        <textarea id="description" name="description"></textarea><br><br>

        <input type="hidden" name="contractId" value="${contractId}">
        <input type="submit" value="Thêm">
        <a href="/task/list?contractId=${contractId}">Quay Lại</a>
    </form>
</body>
</html>