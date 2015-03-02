<!DOCTYPE html>
<%@ taglib prefix="c"
uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head lang="en">
    <meta charset="UTF-8">
    <meta name="viewport" content="initial-scale=1.0, user-scalable=no">
    <title>Item Results</title>
    <link rel="stylesheet" type="text/css" href="css/maps.css">
    <link rel="stylesheet" type="text/css" href="css/bootstrap.min.css">
    <link rel="stylesheet" type="text/css" href="css/main.css">
</head>

<body onload="initialize('${Item.location}', '${Item.latitude}', '${Item.longitude}')">
    <div class="row" style="padding-bottom: 25px">
        <div class="col-md-12 text-center">
            <a href="/eBay">Home</a>
        </div>
    </div>
    <form action="/eBay/item" method="GET">
        <div> Please enter an item ID to search for </div>
        <input name="id" type="text"/>
        <input type="submit" value="Submit"/>
    </form>
    <div> Search Results for <b>${id}</b>:</div>
    <br/>

    <c:choose>
        <c:when test="${empty xml}">
            <p> Item does not exist! </p>
        </c:when>
        <c:otherwise>
            <div id="map_canvas"></div>
            <p>ID: ${Item.itemID} </p>
            <p>Name: ${Item.name}</p>
            <p> Categories: </p>
            <ul>
                <c:forEach var="category" items="${Item.categories}">
                    <li>${category}</li>
                </c:forEach>
            </ul>
            <p>Description: ${Item.description}</p>
            <p>Currently: ${Item.currently}</p>
            <p>First Bid:
            <c:choose>
                <c:when test="${empty Item.firstBid}">N/A</c:when>
                <c:otherwise>${Item.firstBid}</c:otherwise>
            </c:choose>
            </p>
            <p>Buy Price:
                <c:choose>
                    <c:when test="${empty Item.buyPrice}">N/A</c:when>
                    <c:otherwise>${Item.buyPrice}</c:otherwise>
                </c:choose>
            </p>
            <p> Bids:
                <c:choose>
                    <c:when test="${empty Item.numBids}">0</c:when>
                    <c:otherwise>${Item.numBids}</c:otherwise>
                </c:choose></p>
            </p>
            <ol>
                <c:forEach var="bid" items="${Item.bids}">
                    <li>
                        <p> Time: ${bid.bidTime} </p>
                        <p> Amount: ${bid.bidAmount}</p>
                        <p> User: ${bid.bidderID} </p>
                        <p> Rating: ${bid.bidderRating} </p>
                        <p> Bidder Location: ${bid.bidderLocation} </p>
                        <p> Bidder Country: ${bid.bidderCountry} </p>
                    </li>
                </c:forEach>
            </ol>
            <p> Coordinates(latitude, longitude):
                 <c:choose>
                    <c:when test="${empty Item.latitude and empty Item.longitude}">N/A</c:when>
                    <c:otherwise>(${Item.latitude}, ${Item.longitude})</c:otherwise>
                 </c:choose>
            </p>
            <p>Location: ${Item.location} </p>
            <p>Country: ${Item.country}</p>
            <p>Started: ${Item.started}</p>
            <p>Ends: ${Item.ends} </p>
            <p>SellerID: ${Item.sellerID} </p>
            <p>Rating: ${Item.rating}</p>
        </c:otherwise>
    </c:choose>
    <script type="text/javascript"
          src="https://maps.googleapis.com/maps/api/js?key=AIzaSyBoHOUcuCmSA5HvNW6wkKsVXIFgJwspz7Q">
    </script>
    <script type="text/javascript" src="js/maps.js"></script>
</body>
</html>