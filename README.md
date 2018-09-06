# pc-project

Requirement : 

Using Java, write a micro service that invokes AWS elastic search and make it available using API gateway.   

Information provided:

A sample dataset in csv format and a text file with a all the columns and their format.
Search should be allowed by Plan name, Sponsor name and Sponsor State.

Implementation Steps Involved:

AWS ES has different domain tiers with specific data upload limitations. I have used a micro instance for this project.

Conversion of CSV file to JSON in a format accepted by Elastic Search. This process can be found CsvToJson.java file.

Next step is to upload the dataset into AWS ES domain. The data upload can be done with single document or multiple documents. CsvToJson.java has the capability to upload single document into ES and also have capability to create limited rows JSON files.
Data upload refer here : https://docs.aws.amazon.com/elasticsearch-service/latest/developerguide/es-gsg-upload-data.html

The upload data can be confirmed with a feature provided by AWS ES, Kibana. Since ES is for data analytics, Kibana is wonderful tool to visualize and explore data. For more information, refer here: https://aws.amazon.com/elasticsearch-service/kibana/

Next, you have to use a service - API Gateway. I went ahead and created a resource and GET function. The endpoint in this case would be your ES domain and can be accessed on the AWS ES dashboard. For this project, there were three query parameters - Plan Name, Sponsor Name and Sponsor State. These can be created while creating the GET method.

Once, you've created the API, you can decide how you would like to execute the API - say through a Lambda function, HTTP, Mapping or another AWS service. For this application I went ahead and chose Lambda. Note: before selection, a Lambda function needs to be created.

Go ahead and access the Lambda AWS Service and create a function. I created a Java function for the service. This function gets triggered whenever the API is called. Please refer to the PlansSearch.java file.

Endpoints available for search via JAVA are:

Plan Name: If you search for WESTMEYER GROUP RETIREMENT PLAN, the encoded link will be : https://jlhpi980b7.execute-api.us-east-2.amazonaws.com/beta/getPlans?planName=WESTMEYER%20GROUP%20RETIREMENT%20PLAN
e.g. https://jlhpi980b7.execute-api.us-east-2.amazonaws.com/beta/getPlans?planName=<Your Plan Name>

Sponsor Name: If you search for WESTMEYER GROUP, INC., the link generated: https://jlhpi980b7.execute-api.us-east-2.amazonaws.com/beta/getPlans?sponsorName=WESTMEYER%20GROUP,%20INC.
e.g. https://jlhpi980b7.execute-api.us-east-2.amazonaws.com/beta/getPlans?sponsorName=<Your Sponsor Name>

Sponsor State: Similarly all the plans in TX would be as follows - https://jlhpi980b7.execute-api.us-east-2.amazonaws.com/beta/getPlans?sponsorState=TX
e.g. https://jlhpi980b7.execute-api.us-east-2.amazonaws.com/beta/getPlans?sponsorState=<Your Sponsor State>
