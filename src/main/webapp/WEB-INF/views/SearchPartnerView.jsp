<%-- SearchPartnerView.jsp --%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>Tìm Kiếm Đối Tác</title>
</head>
<body>
    <h2>Tìm Kiếm Đối Tác để Thanh Toán</h2>
    <form action="/partner/search-result" method="get">
        <label for="search">Tên Đối Tác:</label>
        <input type="text" id="search" name="search">
        <input type="submit" value="Tìm Kiếm">
    </form>

    <c:if test="${not empty partners}">
        <table border="1">
            <thead>
                <tr>
                    <th>ID</th>
                    <th>Tên Đối Tác</th>
                    <th>Người Đại Diện</th>
                    <th>Hành Động</th>
                </tr>
            </thead>
            <tbody>
                <c:forEach var="partner" items="${partners}">
                    <tr>
                        <td>${partner.id}</td>
                        <td>${partner.namePartner}</td>
                        <td>${partner.partnerRepresentative}</td>
                        <td>
                            <form action="/contract/list" method="get">
                                <input type="hidden" name="partnerId" value="${partner.id}">
                                <input type="submit" value="Chọn">
                            </form>
                        </td>
                    </tr>
                </c:forEach>
            </tbody>
        </table>
    </c:if>
    <a href="/accountant">Quay Lại</a>
</body>
</html>