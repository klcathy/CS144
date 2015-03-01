<!DOCTYPE html>
<%@ taglib prefix="c"
uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head lang="en">
    <meta charset="UTF-8">
    <title>Search Results</title>
    <link rel="stylesheet" type="text/css" href="css/main.css">
</head>
<body>
    <a href="/eBay">Home</a>
    <form action="/eBay/search" method="GET">
        <div class="searchContainer">
            <div>
                <span>Please enter keywords to search for:</span> <br/>
                <input name="q" type="text" id="searchBox"/>
                <input id="submit" type="submit" value="Submit"/>
            </div>
            <div class="suggestions"></div>
        </div>
        <input name="numResultsToSkip" type="hidden" value="0"/>
        <input name="numResultsToReturn" type="hidden" value="10"/>
    </form>
    <div> Search Results for <b>${q}</b>:</div>
    <c:choose>
        <c:when test="${empty results}">
            <h2> No results found! </h2>
        </c:when>
        <c:otherwise>
            <ul>
                <c:forEach var="result" items="${results}">
                    <li>
                        <b>Item: </b>${result.name}
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
      <script type="text/javascript" src="js/autosuggest.js"></script>
                <script type="text/javascript" src="js/suggestions.js"></script>
                <script type="text/javascript">
                    window.onload = function () {
                        var oTextbox = new AutoSuggestControl(document.getElementById("searchBox"), new SuggestionList());
                    }
                </script>
</body>
</html>