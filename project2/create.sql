CREATE TABLE Item (iid bigint,
				   name varchar(256),
				   currently decimal(8,2),
				   first_bid decimal(8,2),
				   buy_price decimal(8,2),
				   num_bids int,
				   started timestamp,
				   ends timestamp,
				   description varchar(4000),
				   sid bigint,
				   location varchar(256),
				   country varchar(256),
				   latitude varchar(256),
				   longitude varchar(256),
				   FOREIGN KEY (sid) references Seller(sid),
				   PRIMARY KEY(iid));

CREATE TABLE ItemCategory (iid bigint,
						  Category varchar(4000),
						  FOREIGN KEY (iid) references Item(iid));

CREATE TABLE Seller (sid varchar(40),
				     rating int,
				     PRIMARY KEY(sid));

CREATE TABLE Bidder (bid bigint,
					rating int,
					location varchar(256),
					country varchar(256),
					PRIMARY KEY(bid));

CREATE TABLE Bid (iid bigint,
				  bid int,
				  time timestamp,
				  amount decimal(8,2),
				  PRIMARY KEY(iid, bid, time),
				  FOREIGN KEY (iid) references Item(iid),
				  FOREIGN KEY (bid) references Bidder(bid));