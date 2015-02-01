Team Name: RKC

Part B
-------
1. Item(ItemID PRIMARY KEY, Name, Currently, First_Bid, Buy_Price, Num_Bids, Started, Ends, Description,
      SellerID refs Seller(SellerID), Location, Country, Latitude, Longitude)
   ItemCategory(ItemID refs Item(ItemID), Category)
   Seller(SellerID PRIMARY KEY, Rating)
   Bidder(BidderID PRIMARY KEY, Rating, Location, Country)
   Bid(ItemID refs Item(ItemID), BidderID refs Bidder(BidderID), Time, Amount, (ItemID, BidderID, Time) PRIMARY KEY)

2. There are no nontrivial function dependencies, but these are the relations for our keys.
   ItemID -> Name, Currently, First_Bid, Buy_Price, Num_Bids, Started, Ends, SellerID, Description, Location, Country, Latitude, Longitude
   ItemID, BidderID, Time -> Amount
   BidderID -> Rating, Location, Country
   SellerID -> Rating 
   ItemID -> Category

3. Yes

4. Yes

