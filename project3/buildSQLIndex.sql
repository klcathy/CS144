--ItemLocation table stores all latitude, longitude info of Item--
CREATE TABLE IF NOT EXISTS ItemLocation (iid varchar(40) NOT NULL,
						   location POINT NOT NULL
						   SPATIAL INDEX(location),
						   PRIMARY KEY (iid, location),
						   FOREIGN KEY (iid) REFERENCES Item(iid)
						   ) ENGINE=MyISAM;

INSERT INTO ItemLocation (iid, location)
SELECT iid, POINT(latitude, longitude) 
FROM Item
WHERE latitude IS NOT NULL AND longitude IS NOT NULL;