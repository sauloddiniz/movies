echo Creating  DynamoDb \'movies\' table ...
echo $(awslocal dynamodb create-table --cli-input-json '{"TableName":"movies", "KeySchema":[{"AttributeName":"movieId","KeyType":"HASH"}], "AttributeDefinitions":[{"AttributeName":"movieId","AttributeType":"S"}],"BillingMode":"PAY_PER_REQUEST"}')

echo $(awslocal dynamodb create-table --cli-input-json '{"TableName":"artist", "KeySchema":[{"AttributeName":"artistId","KeyType":"HASH"}], "AttributeDefinitions":[{"AttributeName":"artistId","AttributeType":"S"}],"BillingMode":"PAY_PER_REQUEST"}')

# --> List DynamoDb Tables
echo Listing tables ...
echo $(awslocal dynamodb list-tables)
