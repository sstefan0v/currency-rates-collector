# currency-rates-collector

This service downloads info about currency rates. It has to connect to Postgres db and cache.
To create the infrastruture for that purpose, first download and build the https://github.com/sstefan0v/currency-rates-gateway

To build:
```
mvn install
```


This service works together with https://github.com/sstefan0v/currency-rates-gateway