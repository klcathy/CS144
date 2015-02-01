#!/bin/bash

# Run the drop.sql batch file to drop existing tables
# Inside the drop.sql, you sould check whether the table exists. Drop them ONLY if they exists.
mysql CS144 < drop.sql

# Run the create.sql batch file to create the database and tables
mysql CS144 < create.sql

# Compile and run the parser to generate the appropriate load files
ant
ant run-all

# If the Java code does not handle duplicate removal, do this now
cat item.dat | uniq > temp_item.dat
cat temp_item.dat > item.dat

cat itemCategory.dat | uniq > temp_category.dat
cat temp_category.dat > itemCategory.dat

cat bidder.dat | uniq > temp_bidder.dat
cat temp_bidder.dat > bidder.dat

cat bid.dat | uniq > temp_bid.dat
cat temp_bid.dat > bid.dat

cat seller.dat | uniq > temp_seller.dat
cat temp_seller.dat > seller.dat

# Run the load.sql batch file to load the data
mysql CS144 < load.sql

# Remove all temporary files
rm *.dat
