Part B
-------
1. Item(ItemID PRIMARY KEY, Name, Currently, First_Bid, Buy_Price, Num_Bids, Started, Ends, Description, SellerID)
   ItemCategory(ItemID, Category) // not sure what primary key is 
   User(UserID PRIMARY KEY, Rating, Location, Country)
   Bid(ItemID refs Item(ItemID), BidderID refs User(UserID), Time, Amount, (ItemID, BidderID, Time) PRIMARY KEY)

2. ItemID -> Name, Currently, First_Bid, Buy_Price, Num_Bids, Started, Ends, SellerID, Description
   ItemID, UserID, Time -> Amount
   UserID -> Rating, Location, Country
   ItemID -> Category // ??

3. Yes

4. Yes

