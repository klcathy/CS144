<!DOCTYPE html>
<%@ taglib prefix="c"
uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head lang="en">
    <meta charset="UTF-8">
    <meta name="viewport" content="initial-scale=1.0, user-scalable=no">
    <title>Item Results</title>
    <link rel="stylesheet" type="text/css" href="style.css">
    <script type="text/javascript"
          src="https://maps.googleapis.com/maps/api/js?key=AIzaSyBoHOUcuCmSA5HvNW6wkKsVXIFgJwspz7Q">
    </script>
    <script type= "text/javascript">
        function initialize(latitude, longitude) {
            var geocoder = new google.maps.Geocoder();
            var map;
            var latlng = new google.maps.LatLng(latitude, longitude);
            var myOptions = {
              zoom: 14, // default is 8
              center: latlng,
              mapTypeId: google.maps.MapTypeId.ROADMAP
            };
            map = new google.maps.Map(document.getElementById("map_canvas"),
                myOptions);

            geocoder.geocode( { 'location': latlng}, function(results, status) {
              if (status == google.maps.GeocoderStatus.OK) {
                map.setCenter(results[0].geometry.location);
                var marker = new google.maps.Marker({
                    map: map,
                    position: results[0].geometry.location
                });
              } else {
                alert("Geocode was not successful for the following reason: " + status);
              }
            });
        }
    </script>
</head>

<body onload="initialize(${Item.latitude}, ${Item.longitude})">
    <form action="/eBay/item" method="GET">
        <div> Please enter an item ID to search for </div>
        <input name="id" type="text"/>
        <input type="submit" value="Submit"/>
    </form>
    <div> Search Results for <b>${id}</b>:</div>
    <br/>

    <c:choose>
        <c:when test="${empty xml}">
            <h2> Item does not exist! </h2>
        </c:when>
        <c:otherwise>
            <div id="map_canvas"></div>
            <h2>ID: ${Item.itemID} </h2>
            <h2>Name: ${Item.name}</h2>
            <h2> Categories: </h2>
            <ul>
                <c:forEach var="category" items="${Item.categories}">
                    <li>${category}</li>
                </c:forEach>
            </ul>
            <h2>Currently: ${Item.currently}</h2>
            <h2>First Bid: ${Item.firstBid} </h2>
            <h2>Buy Price: ${Item.buyPrice}</h2>
            <h2>Num Bids: ${Item.numBids}</h2>
            <h2> Bids: </h2>
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