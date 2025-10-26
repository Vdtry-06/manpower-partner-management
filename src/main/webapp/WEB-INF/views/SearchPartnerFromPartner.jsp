<%-- SearchPartnerFromPartner.jsp --%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Search Partner From Partner</title>
</head>
<body>
    <h2>Search Partner From Partner</h2>
    <form action="/partner/search" method="get">
        <label for="search">Search:</label>
        <input type="text" id="search" name="search"><br><br>

        <input type="submit" value="Search Partner">
        <input type="button" value="Back" onclick="window.location.href='/accountant';">
    </form>
</body>
</html>