<%-- ListTaskView.jsp --%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>Danh Sách Đầu Việc</title>
</head>
<body>
    <h2>Danh Sách Đầu Việc</h2>
    <table border="1">
        <thead>
            <tr>
                <th>ID</th>
                <th>Tên Đầu Việc</th>
                <th>Mô Tả</th>
                <th>Hành Động</th>
            </tr>
        </thead>
        <tbody>
            <c:forEach var="task" items="${tasks}">
                <tr>
                    <td>${task.id}</td>
                    <td>${task.nameTask}</td>
                    <td>${task.description}</td>
                    <td>
                        <form action="/task/select" method="post">
                            <input type="hidden" name="taskId" value="${task.id}">
                            <input type="hidden" name="contractId" value="${contractId}">
                            <input type="submit" value="Chọn">
                        </form>
                    </td>
                </tr>
            </c:forEach>
        </tbody>
    </table>
    <a href="/task/add?contractId=${contractId}">Thêm Đầu Việc</a><br>
    <a href="/partner-manager/sign-contract">Quay Lại</a>
</body>
</html>