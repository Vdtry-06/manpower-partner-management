<%-- ListContractView.jsp --%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>Danh Sách Hợp Đồng</title>
</head>
<body>
    <h2>Danh Sách Hợp Đồng</h2>
    <table border="1">
        <thead>
            <tr>
                <th>ID</th>
                <th>Tên Hợp Đồng</th>
                <th>Trạng Thái</th>
                <th>Hành Động</th>
            </tr>
        </thead>
        <tbody>
            <c:forEach var="contract" items="${contracts}">
                <tr>
                    <td>${contract.id}</td>
                    <td>${contract.contractName}</td>
                    <td>${contract.status}</td>
                    <td>
                        <form action="/contract/detail" method="get">
                            <input type="hidden" name="contractId" value="${contract.id}">
                            <input type="submit" value="Chi Tiết">
                        </form>
                    </td>
                </tr>
            </c:forEach>
        </tbody>
    </table>
    <a href="/accountant/receive-payment">Quay Lại</a>
</body>
</html>