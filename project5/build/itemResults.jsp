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
        <div class="row" style="padding-bottom: 25px">
            <form action="/eBay/item" method="GET">
                <div class="col-md-12 text-center">
                    <div>
                        <span>Please enter an item ID to search for:</span> <br/>
                        <input type="text" name="id" placeholder="Enter an item ID"/>
                        <input class="btn btn-primary" id="submit" type="submit" value="Submit"/>
                    </div>
                </div>
            </form>
        </div>

    <h1 class="text-center"> Search Results for <b>${id}</b>:</h1>

    <c:choose>
        <c:when test="${empty xml}">
            <div class="row">
                <div class="col-md-12 text-center">
                    <span> Item does not exist! Please try again. </span>
                </div>
            </div>
        </c:when>
        <c:otherwise>
        <div class="container">
            <div class="row row-centered">
                <div class="col-md-4 col-centered col-fixed">
                <div id="map_canvas"></div>
                <p><b>Location:</b> ${Item.location} </p>
                 <p><b>Country:</b> ${Item.country}</p>
                 <p> <b>Coordinates(latitude, longitude):</b>
                     <c:choose>
                        <c:when test="${empty Item.latitude and empty Item.longitude}">N/A</c:when>
                        <c:otherwise>(${Item.latitude}, ${Item.longitude})</c:otherwise>
                     </c:choose>
                 </p>
                </div>

            <div class="col-md-4 col-centered col-fixed">
                <p><b>Name:</b> ${Item.name}</p>
                <p><b>ID:</b> ${Item.itemID} </p>
                <p> <b>Categories:</b> </p>
                <ul>
                    <c:forEach var="category" items="${Item.categories}">
                        <li>${category}</li>
                    </c:forEach>
                </ul>
                <p><b>Description:</b> ${Item.description}</p>
            </div>


            <div class="col-md-4 col-centered col-fixed">
                <p><b>Currently:</b> ${Item.currently}</p>

                 <p><b>Started:</b> ${Item.started}</p>
                 <p><b>Ends:</b> ${Item.ends} </p>
                 <p><b>SellerID:</b> ${Item.sellerID} </p>
                 <p><b>Rating:</b> ${Item.rating}</p>
                <p><b>First Bid:</b>
                <c:choose>
                    <c:when test="${empty Item.firstBid}">N/A</c:when>
                    <c:otherwise>${Item.firstBid}</c:otherwise>
                </c:choose>
                </p>
                <p><b>Buy Price:</b>
                    <c:choose>
                        <c:when test="${empty Item.buyPrice}">N/A</c:when>
                        <c:otherwise>${Item.buyPrice}</c:otherwise>
                    </c:choose>
                </p>
                <p> <b>Bids:</b>
                    <c:choose>
                        <c:when test="${empty Item.numBids}">0</c:when>
                        <c:otherwise>${Item.numBids}</c:otherwise>
                    </c:choose></p>
                </p>
                <ol>
                    <c:forEach var="bid" items="${Item.bids}">
                        <li>
                            <p> <i>Time:</i> ${bid.bidTime} </p>
                            <p> <i>Amount:</i> ${bid.bidAmount}</p>
                            <p> <i>User:</i> ${bid.bidderID} </p>
                            <p> <i>Rating:</i> ${bid.bidderRating} </p>
                            <p> <i>Bidder Location:</i> ${bid.bidderLocation} </p>
                            <p> <i>Bidder Country:</i> ${bid.bidderCountry} </p>
                        </li>
                    </c:forEach>
                </ol>

                <c:choose>
                    <c:when test="${empty Item.buyPrice}"></c:when>
                    <c:otherwise><a href="creditCard.jsp">Pay Now</a></c:otherwise>
                </c:choose>

            </div>
            </div>
        </div>
        </c:otherwise>
    </c:choose>

    <script type="text/javascript"
          src="https://maps.googleapis.com/maps/api/js?key=AIzaSyBoHOUcuCmSA5HvNW6wkKsVXIFgJwspz7Q">
    </script>
    <script type="text/javascript" src="js/maps.js"></script>
</body>
</html>