<%-- DetailContractView.jsp --%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>Chi Tiết Hợp Đồng</title>
</head>
<body>
    <h2>Chi Tiết Hợp Đồng</h2>
    <p>Tên Hợp Đồng: ${contract.contractName}</p>
    <p>Trạng Thái: ${contract.status}</p>

    <table border="1">
        <thead>
            <tr>
                <th>Mã Ca</th>
                <th>Ngày Làm</th>
                <th>Số Nhân Công</th>
                <th>Hành Động</th>
            </tr>
        </thead>
        <tbody>
            <c:forEach var="shift" items="${shiftList}">
                <tr>
                    <td>${shift.id}</td>
                    <td>${shift.workDate}</td>
                    <td>${shift.workerCount}</td>
                    <td>
                        <form action="/invoice/update" method="get">
                            <input type="hidden" name="shiftId" value="${shift.id}">
                            <input type="submit" value="Thanh Toán">
                        </form>
                    </td>
                </tr>
            </c:forEach>
        </tbody>
    </table>
    <a href="/contract/list?partnerId=${partnerId}">Quay Lại</a>
</body>
</html>