<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>Danh Sách Đối Tác</title>
</head>
<body>
    <h2>Danh Sách Đối Tác</h2>
    <table border="1">
        <thead>
            <tr>
                <th>ID</th>
                <th>Tên Đối Tác</th>
                <th>Người Đại Diện</th>
                <th>Số Điện Thoại</th>
                <th>Email</th>
                <th>Hành Động</th>
            </tr>
        </thead>
        <tbody>
            <c:forEach var="partner" items="${partners}">
                <tr>
                    <td>${partner.id}</td>
                    <td>${partner.namePartner}</td>
                    <td>${partner.partnerRepresentative}</td>
                    <td>${partner.phoneNumber}</td>
                    <td>${partner.email}</td>
                    <td>
                        <form action="/contract/create" method="post">
                            <input type="hidden" name="partnerId" value="${partner.id}">
                            <input type="submit" value="Tạo Hợp Đồng">
                        </form>
                    </td>
                </tr>
            </c:forEach>
        </tbody>
    </table>
    <a href="/partner-manager/sign-contract">Quay Lại</a>
</body>
</html>