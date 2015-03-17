<!DOCTYPE html>
<%@ taglib prefix="c"
uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head lang="en">
    <meta charset="UTF-8">
    <title>Payment Information</title>
    <link rel="stylesheet" type="text/css" href="css/bootstrap.min.css">
    <link rel="stylesheet" type="text/css" href="css/main.css">
</head>
<body>
    <div style = "width: 30%;height: 65%;margin-left: auto;margin-right: auto; margin-top: 15%;">
        <p>ItemID: <c:out value="${sessionScope.itemId}"/></p>

        <p>Item Name: <c:out value="${sessionScope.name}"/></p>

        <p>Buy Price: <c:out value="${sessionScope.buyPrice}"/></p>

        <p>Credit Card: <c:out value="${sessionScope.creditCardNum}"/></p>

   </div>
</body>
</html>