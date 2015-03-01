<!DOCTYPE html>
<%@ taglib prefix="c"
uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head lang="en">
    <meta charset="UTF-8">
    <title>Item Results</title>
</head>

<body>
    <div> Search Results for <b>${id}</b>:</div>

    <div> ${xml} </div>
    <br/>

    <c:choose>
        <c:when test="${empty xml}">
            <h2> Item does not exist! </h2>
        </c:when>
        <c:otherwise>
            <h2>ID: ${Item.itemID} </h2>
            <h2>Name: ${Item.name}</h2>
            <h2>Currently: ${Item.currently}</h2>
            <h2>First Bid: ${Item.firstBid} </h2>
            <h2>Buy Price: ${Item.buyPrice}</h2>
            <h2>Num Bids: ${Item.numBids}</h2>
            <h2>Location: ${Item.location} </h2>
            <h2>Country: ${Item.country}</h2>
            <h2>Started: ${Item.started}</h2>
            <h2>Ends: ${Item.ends} </h2>
            <h2>Longitude: ${Item.longitude}</h2>
            <h2>Latitude: ${Item.latitude}</h2>
            <h2>SellerID: ${Item.sellerID} </h2>
            <h2>Rating: ${Item.rating}</h2>
            <h2>Description: ${Item.description}</h2>
        </c:otherwise>
    </c:choose>
</body>

</html>