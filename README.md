# Superdevs Java Challenge

### Access via swagger
#### Data import:
http://localhost:8080/swagger-ui.html#/data-import-controller/importDataUsingPOST

#### Calculated metrics
1. CTR
http://localhost:8080/swagger-ui.html#/metrics-controller/getCTRUsingGET

2. Daily impressions over time
http://localhost:8080/swagger-ui.html#/metrics-controller/getImpressionsOverTimeUsingGET

3.  Total Clicks
http://localhost:8080/swagger-ui.html#/metrics-controller/getTotalClicksUsingGET



### Sample access via curl
#### Data import
```
curl -X POST "http://localhost:8080/import" -H "accept: */*" -H "Content-Type: multipart/form-data" -F "file=@PIxSyyrIKFORrCXfMYqZBI.csv;type=application/vnd.ms-excel"
```

#### Calculated metrics
1. CTR
```
curl -X GET "http://localhost:8080/metrics/ctr?datasource=Twitter%20Ads&date_from=01-01-2000&date_to=08-24-2020&group_by=campaign&group_by=datasource" -H "accept: */*"
```

2. Daily impressions over time
```
curl -X GET "http://localhost:8080/metrics/impressions_over_time?datasource=Twitter%20Ads&date_from=01-01-2000&date_to=08-24-2020&group_by=campaign&group_by=datasource" -H "accept: */*"
```

3.  Total Clicks
```
curl -X GET "http://localhost:8080/metrics/total_clicks?datasource=Twitter%20Ads&date_from=01-01-2000&date_to=08-24-2020&group_by=campaign&group_by=datasource" -H "accept: */*"
```
