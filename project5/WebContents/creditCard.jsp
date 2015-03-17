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
    <div style = "width: 40%;height: 65%;margin-left: auto;margin-right: auto; margin-top: 15%;">
      <div class="form-group">
        <label class="col-md-4 control-label">ItemID</label>
        <div class="col-md-8">
          <p class="form-control-static"><c:out value="${sessionScope.itemId}"/></p>
        </div>
      </div>

    <div class="form-group">
      <label class="col-md-4 control-label">Item Name</label>
      <div class="col-md-8">
        <p class="form-control-static"><c:out value="${sessionScope.name}"/></p>
      </div>
    </div>

    <div class="form-group">
      <label class="col-md-4 control-label">Buy Price</label>
      <div class="col-md-8">
        <p class="form-control-static"><c:out value="${sessionScope.buyPrice}"/></p>
      </div>
    </div>

    <form action="/eBay/session" method="POST">
      <div class="form-group">
        <label for="credit_card" class="col-md-4 control-label">Credit Card</label>
        <div class="col-md-8">
          <input type="text" class="form-control" name="credit_card" placeholder="Enter your credit card number">
        </div>
      </div>

      <div class="form-group">
          <div class="col-md-offset-4 col-md-5">
            <input style="width:40%; margin-top: 15px;" class="btn btn-primary" type="submit" value="Submit"/>
          </div>
      </div>
    </form>
  </div>
</body>
</html>