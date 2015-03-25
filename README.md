CS144: Web Applications
=====

This is a repository for the projects assigned in Professor Cho's Winter 2015
class. 

### Project 1: Setup Your Environment
----------------------------------------
This project was a simple warm-up to VirtualBox, MySQL and Java. 

For the MySQL portion, we created an Actors table and made a query to get the names of all actors in the movie 'Die Another Day'. 

For the Java portion, we implemented a program that computed the SHA-1 hash over the content of input file.

### Project 2: Data Conversion and Loading
----------------------------------------
We were given a large amount of real data (~20k auctions) from the eBay website stored as XML files.

Our tasks included:
  1. Designing a good (BCNF/4NF) relational schema based off the data
  2. Transforming the data into MySQL's load format, conforming to our relational schema
  3. Constructing various queries to test our MySQL database with the given expected results

### Project 3: Search and Retrieval
----------------------------------------
Continuing off of the previous project, we provided search functionality to the data. 

We supported two types of queries: keyword-based search over text fields using Apache Lucene and search for items located within a geographic region using MySQL spatial indexes. 

Furthermore, we made our search interface available as a SOAP-based Web service using Apache Tomcat and Axis2.

### Project 4: Building a Website
----------------------------------------
We built a website that integrated three Web services: 
* eBay Web service built in Project 3
* Google Map service to display location of auction items
* Google Suggest service to help users formulate keyword queries

Screenshots demonstrating the functionality of the website is provided in the repo.

### Project 5: Enabling Secure Transaction
----------------------------------------
We updated the website, so that the website was still secure to use when transmitting sensitive information such as credit card information.

### Authors
----------------------------------------
Kailin Chang <br/>
Roger Chen
