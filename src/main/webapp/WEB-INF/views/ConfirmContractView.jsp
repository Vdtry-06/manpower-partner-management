<%-- ConfirmContractView.jsp --%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>Xác Nhận Hợp Đồng</title>
</head>
<body>
    <h2>Xác Nhận Hợp Đồng</h2>
    <p>Tên Hợp Đồng: ${contract.contractName}</p>
    <p>Mô Tả: ${contract.description}</p>
    <p>Thông Tin Đối Tác: ${contract.partnerId.namePartner} - ${contract.partnerId.partnerRepresentative}</p>
    <p>Nhân Viên Đại Diện: ${contract.partnerManagerId.fullname}</p>

    <table border="1">
        <thead>
            <tr>
                <th>STT</th>
                <th>Mã Ca</th>
                <th>Tên Đầu Việc</th>
                <th>Ngày Làm</th>
                <th>Giờ Bắt Đầu</th>
                <th>Giờ Kết Thúc</th>
                <th>Số Nhân Công</th>
                <th>Đơn Giá (VNĐ)</th>
            </tr>
        </thead>
        <tbody>
            <c:forEach var="item" items="${shiftTaskList}" varStatus="loop">
                <tr>
                    <td>${loop.count}</td>
                    <td>${item.shiftResponse.id}</td>
                    <td>${item.taskResponse.nameTask}</td>
                    <td>${item.shiftResponse.workDate}</td>
                    <td>${item.shiftResponse.startTime}</td>
                    <td>${item.shiftResponse.endTime}</td>
                    <td>${item.shiftResponse.workerCount}</td>
                    <td>${item.shiftResponse.shiftUnitPrice}</td>
                </tr>
            </c:forEach>
        </tbody>
    </table>
    <p>Tổng Giá Trị: ${contract.totalContractValue} VNĐ</p>

    <form action="/contract/update" method="post">
        <input type="hidden" name="contractId" value="${contract.id}">
        <input type="submit" value="Lưu Hợp Đồng">
    </form>
    <a href="/task/list?contractId=${contractId}">Sửa Đổi</a><br>
    <a href="/partner-manager/sign-contract">Hủy</a><br>
    <button onclick="window.print()">In Hợp Đồng</button>
</body>
</html>