Part B
-------
1. Item(ItemID PRIMARY KEY, Name, Currently, First_Bid, Buy_Price, Num_Bids, Started, Ends, Description,
      SellerID refs Seller(SellerID), Location, Country, Latitude?, Longitude)
   ItemCategory(ItemID refs Item(ItemID), Category)
   Seller(SellerID, Rating)
   Bidder(BidderID, Rating, Location, Country)
   Bid(ItemID refs Item(ItemID), BidderID refs Bidder(BidderID), Time, Amount, (ItemID, BidderID, Time) PRIMARY KEY)

2. // Need to update relations
   ItemID -> Name, Currently, First_Bid, Buy_Price, Num_Bids, Started, Ends, SellerID, Description
   ItemID, UserID, Time -> Amount
   UserID -> Rating, Location, Country // need to change
   ItemID -> Category // ??

3. Yes

4. Yes

