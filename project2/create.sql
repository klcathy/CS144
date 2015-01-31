CREATE TABLE Item (ItemID int,
				   Name varchar(4000),
				   Currently decimal(8,2),
				   First_bid decimal(8,2),
				   Buy_price decimal(8,2),
				   Num_bids int,
				   Started timestamp,
				   Ends timestamp,
				   Description varchar(4000),
				   SellerID varchar(4000),
				   PRIMARY KEY(ItemID))

CREATE TABLE ItemCategory (ID int,
						   category varchar(4000),
						  FOREIGN KEY (ID) references Item(ItemID))

CREATE TABLE User (UserID int,
				   Location varchar(4000),
				   Country varchar(4000),
				   PRIMARY KEY(UserID))

CREATE TABLE Seller (UID int,
				     Rating int,
				     FOREIGN KEY (UID) references User(UserID))

CREATE TABLE Buyer (UID int,
					Rating int.
					FOREIGN KEY (UID) references User(UserID))

CREATE TABLE Bid (IID int,
				  BidderID int,
				  Time timestamp,
				  Amount decimal(8,2),
				  PRIMARY KEY(IID, BidderID, Time),
				  FOREIGN KEY (IID) references Item(ItemID),
				  FOREIGN KEY (BidderID) references User(UserID))