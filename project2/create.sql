CREATE TABLE Seller (sid varchar(40) NOT NULL,
				     rating int,
				     PRIMARY KEY(sid)) ENGINE=INNODB;

CREATE TABLE Bidder (bid bigint NOT NULL,
					rating int,
					location varchar(256),
					country varchar(256),
					PRIMARY KEY(bid)) ENGINE=INNODB;

CREATE TABLE Item (iid bigint NOT NULL,
				   name varchar(256),
				   currently decimal(8,2),
				   first_bid decimal(8,2),
				   buy_price decimal(8,2),
				   num_bids int,
				   started timestamp,
				   ends timestamp,
				   description varchar(4000),
				   sid varchar(40),
				   location varchar(256),
				   country varchar(256),
				   latitude varchar(256),
				   longitude varchar(256),
				   FOREIGN KEY (sid) REFERENCES Seller(sid),
				   PRIMARY KEY(iid)) ENGINE=INNODB;

CREATE TABLE ItemCategory (iid bigint NOT NULL,
						  Category varchar(4000),
						  FOREIGN KEY (iid) REFERENCES Item(iid)) ENGINE=INNODB;

CREATE TABLE Bid (iid bigint NOT NULL,
				  bid int NOT NULL,
				  time timestamp NOT NULL,
				  amount decimal(8,2),
				  FOREIGN KEY (iid) REFERENCES Item(iid),
				  FOREIGN KEY (bid) REFERENCES Bidder(bid),
				  PRIMARY KEY(iid, bid, time)) ENGINE=INNODB;