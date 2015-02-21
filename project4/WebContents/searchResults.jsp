<!DOCTYPE html>
<%@ taglib prefix="c"
uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head lang="en">
    <meta charset="UTF-8">
    <title>Search Results</title>
</head>
<body>
    <div> Search Results for Query: ${q}</div>
    <ul>
        <c:forEach var="result" items="${results}">
            <li>
                <b>Item: </b>${result.name}
                <b>ItemId: </b>${result.itemId}
            </li>
        </c:forEach>
    </ul>
</body>
</html>