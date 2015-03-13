<!DOCTYPE html>
<%@ taglib prefix="c"
uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head lang="en">
    <meta charset="UTF-8">
    <title>Search Results</title>
    <link rel="stylesheet" type="text/css" href="css/suggest.css">
    <link rel="stylesheet" type="text/css" href="css/bootstrap.min.css">
    <link rel="stylesheet" type="text/css" href="css/main.css">
</head>
<body>
    <div class="row" style="padding-bottom: 25px">
        <div class="col-md-12 text-center">
            <a href="/eBay">Home</a>
        </div>
    </div>
    <div class="row" style="padding-bottom: 25px">
        <form action="/eBay/search" method="GET">
            <div class="col-md-12 text-center">
                <div>
                    <span>Please enter keywords to search for:</span> <br/>
                    <input type="text" name="q" id="searchBox" placeholder="Enter keywords"/>
                    <input class="btn btn-primary" id="submit" type="submit" value="Submit"/>
                </div>
                <div class="suggestions"></div>
            </div>
            <input name="numResultsToSkip" type="hidden" value="0"/>
            <input name="numResultsToReturn" type="hidden" value="10"/>
        </form>
    </div>
    <div class="row">
        <div class="col-md-12 text-center">
            <h1> Search Results for <b>${q}</b> </h1>
            <c:choose>
                <c:when test="${empty results}">
                    <span> No results found! </span>
                </c:when>
                <c:otherwise>
                    <ul>
                        <c:forEach var="result" items="${results}">
                            <li>
                                <b>Item: </b>${result.name} &nbsp;&nbsp;&nbsp;&nbsp;
                                <b>ItemId: </b> <a id="${result.itemId}" href="item?id=${result.itemId}">${result.itemId}</a>
                            </li>
                        </c:forEach>
                    </ul>
                    <br/>
                    <a id="prev" href="search?q=${q}&numResultsToSkip=${numResultsToSkip-numResultsToReturn}&numResultsToReturn=${numResultsToReturn}">
                    Previous</a>
                    <a id="next" href="search?q=${q}&numResultsToSkip=${numResultsToSkip+numResultsToReturn}&numResultsToReturn=${numResultsToReturn}">
                        Next</a>

                    <script type="text/javascript">
                        var numResultsToSkip = parseInt("${numResultsToSkip}");

                        var prev = document.getElementById("prev");

                        if (numResultsToSkip <= 0) {
                            prev.innerHTML = "";
                        }

                        var next = document.getElementById("next");
                        if (!${hasMore}) {
                            next.innerHTML = "";
                        }
                    </script>
                </c:otherwise>
            </c:choose>
        </div>
    </div>
    <script type="text/javascript" src="js/autosuggest.js"></script>
    <script type="text/javascript" src="js/suggestions.js"></script>
    <script type="text/javascript">
        window.onload = function () {
            var oTextbox = new AutoSuggestControl(document.getElementById("searchBox"), new SuggestionList());
        }
    </script>
</body>
</html>