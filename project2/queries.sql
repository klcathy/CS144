-- Find number of users
SELECT COUNT(*)
FROM ((SELECT sid FROM Seller) UNION (SELECT bid FROM Bidder)) as Users;

-- Find the number of items in "New York"
SELECT COUNT(*)
FROM Item
WHERE BINARY location = 'New York';

-- WRONG Find the number of auctions belonging to exactly four categories
SELECT COUNT(*)
FROM (SELECT iid
      FROM ItemCategory
      GROUP BY iid
      HAVING COUNT(*) = 4) as ItemCount;

-- Find the ID(s) of current (unsold) auction(s) with the highest bid
SELECT iid
FROM Item
WHERE num_bids > 0 AND started < "2001-12-20 00:00:00" AND ends > "2001-12-20 00:00:01" AND
currently = (SELECT MAX(CURRENTLY) FROM Item WHERE num_bids > 0 AND started < "2001-12-20 00:00:00" AND ends > "2001-12-20 00:00:01");

-- Find the number of sellers whose rating is higher than 1000
SELECT COUNT(*)
FROM Seller
WHERE rating > 1000;

-- Find the number of users who are both sellers and bidders
SELECT COUNT(*)
FROM Seller S, Bidder B
WHERE S.sid = B.bid;

-- Find the number of categories that include at least one item with a bid of more than $100
SELECT COUNT(*)
FROM (SELECT COUNT(*)
      FROM ItemCategory ic, Bid b
      WHERE ic.iid = b.iid AND b.amount > 100
      GROUP BY ic.category) as IC;
