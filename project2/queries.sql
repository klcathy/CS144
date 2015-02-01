-- Find number of users

-- Find the number of items in "New York"
SELECT COUNT(*)
FROM Item
WHERE location = 'New York';

-- Find the number of auctions belonging to exactly four categories
SELECT COUNT(*)
FROM ItemCategory
GROUP BY iid
HAVING COUNT(*) = 4; 


-- Find the ID(s) of current (unsold) auction(s) with the highest bid

-- Find the number of sellers whose rating is higher than 1000
SELECT COUNT(*)
FROM Seller
WHERE rating > 1000;

-- Find the number of users who are both sellers and bidders
SELECT COUNT(*)
FROM Seller S, Bidder B
WHERE S.sid = B.bid;

-- Find the number of categories that include at least one item with
-- a bid of more than $100
