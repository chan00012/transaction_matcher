# Transaction Matcher
Author: Christian Salvador T. Robles
Email: cstrobles@gmail.com

## Introduction
This is a backend system containing several APIs which aim to perform transaction matching/reconciliation of two sets of CSV data. This enables third-party clients to integrate with these APIs whenever they are in need of such features.

This system contains 3 APIs which are:

-   **Generate Report API -** generate and evaluate report asynchronously and save it on a memory storage.
-   **Get Match Report API** - get the matched report indicating the total number of entries, matched, and unmatched on both files.
-   **Get Unmatch Report API** - get the unmatched report indicating the entries with possible matches and entries with no matches at all.

## Demo
This application is deployed on [Heroku](https://dashboard.heroku.com/apps) for demonstration purposes. With these you can test this app without building it. You can download this [postman collection](https://drive.google.com/file/d/1EY5YngLqfmQ-uGrURxV9XmZd2l7s26St/view?usp=sharing)  to test the APIs. (No postman? You can get it  [here](https://www.postman.com/downloads/))

You can also see the swagger documentation here: [transaction-matcher-documentation](https://transaction-mapper.herokuapp.com/swagger-ui.html)

Note: Heroku will put the application into sleep whenever there is no activity detected in 30 minutes. You may encounter some delay, the first time you sent a request application. The subsequent request will provide a faster response.

## Build Instructions
This project requires Maven with version at least 3.0 and JDK 8 to be built. Run the following command:

    $mvn clean install
if you want to build with skipping the test, run the follwing command:

    $mvn -Dmaven.test.skip=true clean install
this project is also equipped with code coverage. To execute the code coverage run the following command:

      $mvn clean verify -Pjacoco

code coverage reports can be found at:

    /target/site/jacoco-it/index.html

## Run Instructions
Once the project has been built successfully, it will generate a jar file which you can run by the following command:

    java -jar transactionmapper-1.0.0.jar

After this, the application is ready to use. You can start testing the application through http://localhost:8080 . You may use my postman collection to test or navigate on swagger documentation on http://localhost:8080/swagger-ui.html.

## Implementation
This project was built using Java 8 + Springboot. Third-party dependencies were also used, which are the following:

-   **lombok** - removing boilerplate codes
-   **jackson**- dataformat-csv - mapping csv into POJO
-   **commons**- io - file processing
-   **guava** - data processing
-   **awaitility** - asynchronous test
-   **common-langs3** - String manipulation
-   **swagger** - API documentation

## Features

-   The generation of report is asynchronous. Third-party can do anything first and fetch the result once available.
-   Configurable properties - ex. file size, matcher thresholds, etc.
-   Can handle custom fields - needs to handle in code level (please refer on Extendibility)
-   Known file handling cases
	-  works with CSV files only
	-  works fine with invalid value entries
	-  works fine with excess value entries (will disregard excess values with no matching column)
	-  works fine with trailing commas
	-  works fine with header-only file
	- works fine with different order headers
	- works fine with case insensitive headers
	- works fine with empty space
	-  works fine with different order column
	-  handles invalid file types
	-  	 handles unrecognized columns
	-   handles empty file

## Process Design

The reconciliation process mainly focuses on the concept called  “**negative matching**”  in which each property of two transactions will be compared. Those properties that don’t match will be tagged with their appropriate discrepancy. Once the comparison is done and discrepancies on both transactions are within an acceptable range of discrepancy, they will be marked as possible matches.

But the comparison between the two transactions doesn’t end there. It also undergoes “**threshold matching**”. Each of the transaction’s fields has a corresponding matcher class which extends **AbstractAdaptiveMatcher**. Under this, you can customize how each transaction’s fields can match. You can match it equally, by a certain threshold, or any custom logic on how you want to match them.  
_(Go to Extendibility section to know how to scale this application dynamically)_

But the whole processing logic of this backend app are executed in logic in the following order.

1.	Reference file and compare file will be first translated into a list POJOs called **Transaction**. This pojo hold a single entry of each row per file.

2.	Once the csv files are now translated into **List<Transaction\>** which will be the ***refTxns*** and ***comTxns***. Each of them will undergo into a method ***getInitialAndTagDuplicateTransactions()*** which tags all duplicate transaction and flatten them into a **Set<TaggedTransaction\>** so only a single transaction will exist but will have a count indicator on how many times they appear on the list. With these it will produce 2 set which are the ***refTagTxnSet*** and ***comTagTxnSet***.

3.	The ***refTagTxnSet*** and ***comTagTxnSet*** will perform a retain method in which the common transactions between them will remain and will be saved on another set which will be the ***matchedTransactionSets***, these are the set of perfectly match transactions we will not process this once since this transactions are already fine. ***refTagTxnSet*** and ***comTagTxnSet*** will be updated and those transactions present from ***matchedTransactionSet*** will be removed from each one of them which each set will remain those transactions that has no perfect match with each other.

4.  ***refTagTxnSet*** and ***comTagTxnSet*** will now under go into a method called *"**adaptive matching**"* in which each record of ***refTagTxnSet*** will perform a negative matching with each record of ***comTagTxnSet***. The entry on ***comTagTxnSet*** with least negative match with the ***refTagTxnSet*** will be qualified as possible match. If ever an entry is qualified as a threshold match, it will be added matched transactions. Once adaptive match is finished it will return objects which are the ff:

	- **DiscrepancyMatchTransactions** - transactions with possible matches
	- **MatchedTransactions** - transactions considered as equal since they pass adapative match's threshold, this will be also added on the ***matchTransactionSet***

5.	lastly ***refTagTxnSet*** and ***comTagTxnSet*** will also be updated, leaving only those transactions that has no matches in between them and the will be combies into a single set which is the **NoMatchTransactions**.

## Extendibility
Once a good developer said:

> *A great code comes with a great extendibility*

I tried my best to make this backend application dynamic as possible. One extendible feature of this backend is that you can add or remove fields to be reconcile and implement custom logic on how to reconcile a field. You can do this by following these easy steps.

1.	Add your desired new field on **Transaction.class**  please be sure that the field you added match the column name you wanted. (This is case insentive)
	  ![enter image description here](https://lh3.googleusercontent.com/0KQ3B9gVJgkDb-45m0t1DE1fcdCKZxqDm7-KI6AJR7MV2UNfj98bd3Ht8JXO5K87SHOGNOmGGHLTLcrkSHYHV2UnvsObk8Dm4lLl6-3I3XNJjIpbqllHM4EzKmtrIkyWwJtW6NEfiw=w2400)

2. Supply the new field on the **equals()** and **hasCode()**
   ![enter image description here](https://lh3.googleusercontent.com/9MDzoyrBRxUaoPmKoKkX89nMqfQi7vCBBrwtF4W2LC1GcQhV4kp1tHFg3ugVlHXyoy1IlXcJUed2Di3KCjRxIJgTJFrfiwrcQrfAQWV0uATukWBKbYs8zU5pOQPunMmG4LuWxP6WTA=w2400)

3. Supply proper discrepancy on the **Tag.class**
   ![enter image description here](https://lh3.googleusercontent.com/6WWSaYdfnaXRBzsXKEG33NmQ5JpkGUmKmFuhpVIPLzYM3Hkkvs6p-J1AhZ5ysGOeEUgZvJnlYy19gOdf5DAfZDaZHOTp7wVXwnzVGfu96qxnyPOCpCdQvKZh4FPIMPwfahNgP1ryvw=w2400)

4. Create your own logic for the field matcher by extending this class **AbstractAdaptiveMatcher.class** supply the enum we've created on step 3 on the constructor and override the thresholdMatching() method and implement your own method on how to match the field.
   ![enter image description here](https://lh3.googleusercontent.com/VpfMQxj3ADmvjTGx-G50nQDH64lXcwW_ffzE_4ao6c_nPLbF5WLy0HABBwEqkjwZTcK1LiyKAtj7BSfRTbwaBY9wsf917R2iLDgaqwx5zM2kA1ilgK65UxkEntcXH5MJ-yQgzwMnYA=w2400)

5. Compile and run the project. If it works then you've successfully extend the code. But if not, please file a ticket and let me fix it :)

By default there are 8 out-of-the-box matchers and I've made their threshold values configurable and you can find it on the *application.properies*

    #MATCHER PROPERTIES  
    matcher.date.duration=5 
    matcher.date.timeUnit=Minutes  
    matcher.date.datePattern=yyyy-MM-dd HH:mm:ss  
    matcher.amount.amountThreshold=100.00  
    matcher.description.allowedDescriptor[0]=DEDUCT  
    matcher.description.allowedDescriptor[1]=REVERSAL  
    matcher.id.charThreshold=6  
    matcher.narrative.charThreshold=8  
    matcher.type.allowedType[0]=0  
    matcher.type.allowedType[1]=1


# APIs

Swagger documentation [here](https://transaction-mapper.herokuapp.com/swagger-ui.html)

## Generate Report API ([POST] v1/reports/generate)
This API is used to generate and evaluate the transaction reconciliation between 2 input files. This API is asynchronous in which it will immediately return a report reference number which can be used later to retrieved the report. Once the API is hit it will trigger a separate thread that evaluate the files at background process and save it in the memory once done, making it a good use for large files which take longer time to process. Integrating client can do any task first after they trigger the API and retrieve it once it is ready.

Sample cURL Request:

    curl --location --request POST 'https://transaction-mapper.herokuapp.com/v1/reports/generate' \
    
    --form 'referenceFile=@"/C:/Users/Christian/IdeaProjects/transactionmapper/src/test/resources/files/excess_entry_file.csv"' \
    
    --form 'compareFile=@"/D:/Downloads/tutukamarkofffile20140113.csv"'

Sample JSON Response:

    {
        "status": "Success",
        "code": "0",
        "data": {
            "rrn": "1167959003"
        }
    }

|Field|Type  |Description|Value
|--|--|--|--|
| status |String  |indicate the transaction's status|Success, Error, Waring
|code|String|indicate transaction's code| 0 = Success
|data|Object|payload of the response
|rrn|String|report reference number which can be used to pull report|numeric

## Get Match Report ([GET] v1/reports/match)
This API is used to retrieve the match report from memory from the Generate Report API

Sample cURL Request:

    curl --location --request GET 'https://transaction-mapper.herokuapp.com/v1/reports/match?rrn=6576438103'

Sample JSON Response:

    {
        "status": "Success",
        "code": "0",
        "data": {
            "file1": {
                "fileName": "clientmarkofffile20140113.csv",
                "totalRecords": 306,
                "matchingRecords": 289,
                "unmatchedRecords": 17
            },
            "file2": {
                "fileName": "tutukamarkofffile20140113.csv",
                "totalRecords": 305,
                "matchingRecords": 289,
                "unmatchedRecords": 16
            }
        }
    }

|Field|Type  |Description|Value
|--|--|--|--|
| status |String  |indicate the transaction's status|Success, Error, Waring
|code|String|indicate transaction's code| 0 = Success
|data|Object|payload of the response
|file1|Object|reference file object details|FileResult
|file2|Object|compare file object details|FileResult

## Get Unmatch Report ([GET] v1/reports/unmatch)

This API is used to retrieve the unmatch report from memory from the Generate Report API

Sample cURL Request:

    curl --location --request GET 'https://transaction-mapper.herokuapp.com/v1/reports/unmatch?rrn=1167959003'

Sample JSON Response:

    {
        "status": "Success",
        "code": "0",
        "data": {
            "discrepancyMatchedTransactions": [
                {
                    "referenceTransaction": {
                        "source": "clientmarkofffile20140113.csv",
                        "transaction": {
                            "rowNum": 153,
                            "profileName": "Card Campaign",
                            "transactionDate": "2014-01-12 15:03:05",
                            "transactionAmount": "-6984",
                            "transactionNarrative": "128552 P G TIMBERS MAUN   BOTSWANA      BW",
                            "transactionDescription": "DEDUCT",
                            "transactionId": "0084012397854064",
                            "transactionType": "0",
                            "walletReference": ""
                        }
                    },
                    "possibleMatchTransactions": [
                        {
                            "source": "tutukamarkofffile20140113.csv",
                            "transaction": {
                                "rowNum": 152,
                                "profileName": "Card Campaign",
                                "transactionDate": "2014-01-12 15:03:05",
                                "transactionAmount": "-6984",
                                "transactionNarrative": "128552 P G TIMBERS MAUN   BOTSWANA      BW",
                                "transactionDescription": "DEDUCT",
                                "transactionId": "0084012397854064",
                                "transactionType": "0",
                                "walletReference": "P_Nzc1MDA3MTFfMTM4MTQ4MjU0MC4zMTc2"
                            },
                            "discrepancies": {
                                "WALLET_REFERENCE_DISCREPANCY": "Please check the wallet reference difference."
                            }
                        }
                    ]
                }
            ],
            "noMatchTransactions": [
                {
                    "source": "clientmarkofffile20140113.csv",
                    "transaction": {
                        "rowNum": 65,
                        "profileName": "Card Campaign",
                        "transactionDate": "2014-01-12 14:21:22",
                        "transactionAmount": "-5466",
                        "transactionNarrative": "*RED SQUARE SZ",
                        "transactionDescription": "DEDUCT",
                        "transactionId": "8948594584958495",
                        "transactionType": "0",
                        "walletReference": "P_NzIxMzExMDZfMTM4NjMyNzk1MS4wNDE2"
                    },
                    "discrepancies": {
                        "NO_MATCH": "No match"
                    }
                }
            ]
        }
    }

|Field|Type  |Description|Value
|--|--|--|--|
| status |String  |indicate the transaction's status|Success, Error, Waring
|code|String|indicate transaction's code| 0 = Success
|data|Object|payload of the response
|discrepancyMatchedTransactions|List<EvaluatedTransaction\>|list of transactions with qualified match posibility
|noMatchTransactions|List<EvaluatedTransaction\>|list of transactions with no matches|

## Object Models

**FileResult**
|Field|Type  |Description|Value
|--|--|--|--|
|fileName|String|filename on the corresponding file object|alphanumeric
|totalRecords|int|number of total records on the corresponding file object
|matchingRecords|int|number of matched records on both file
|unmatchedRecords|int| number of unmatched records on the corresponding file object

**Transaction**
|Field|Type  |Description|Value
|--|--|--|--|
|rowNum|int|row number of transaction on its source
|profileName|String|profile name|alpha
|transactionDate|String|trasaction's date|date (YYYY-MM-dd HH:mm:ss)
|transactionAmount|String|transaction's amount| double
|transactionNarrative|String|transaction's merchant|alphanumeric
|transactionDescription|String|transaction's descriptor type|DEDUCT, REVERSAL
|transactionId|String|transaction's id|numeric
|transactionType|String|transaction's type|1,0
|walletReference|String| transaction's wallet reference|alphanumeric

**Evaluated Transaction**
|Field|Type  |Description|Value|Nullable
|--|--|--|--|--|
|count|int|number of duplicates of the current transaction
|source|String|filename which transaction belongs|alphanumeric
|transaction|Object|Single entry of file| Transaction
|discrepancies|Map<Tag, String>|combined discrepancy of source and possible||true

**DiscrepancyMatchTransaction**
|Field|Type  |Description|Value
|--|--|--|--|
|referernceTransaction|EvaluatedTransaction|reference transaction based on source file
|possibleMatchTransactions|List<EvaluatedTransaction\>|list of possible match transactions based on reference transaction
|discrepancies|Map<Tag, String>|map of discrepancies with correspoding message


**Tags**
|Values|Spiel|
|--|--
|NO_MATCH|No match
|DATE_DISCREPANCY|Please check the transaction date difference.
|AMOUNT_DISCREPANCY|Please check the transaction amount difference.
|NARRATIVE_DISCREPANCY|Please check the transaction narrative difference.
|TYPE_DISCREPANCY|Please check the transaction type difference.
|WALLET_REFERENCE_DISCREPANCY|Please check the wallet reference difference.
|ID_DISCREPANCY|Please check the transaction id difference.
|DESCRIPTION_DISCREPANCY|Please check the transaction description difference.
|DUPLICATE_REFERENCE|Please check reference transaction having duplicates.
|DUPLICATE_COMPARE|Please check compared transaction having duplicates.

## Error Response

    {
        "status": "Error",
        "code": "TMBE001",
        "error": {
            "reference": "3E772ABD9",
            "message": "Report does not exist."
        }
    }


|Field|Type  |Description|Value
|--|--|--|--|
| status |String  |indicate the transaction's status|Success, Error, Waring
|code|String|indicate transaction's code| 0 = Success
|error|Object|payload of the error response
|reference|String|reference number to for debugging purposes
|message|String|error message

## Error Codes
|Code|Error Type|Cause| Message
|--|--|--|--
| TMIE999 |Internal Error|Uncaught error|Internal error, please try again later.
|TMBE000|Business Error|Processing error|Report generation failed.
|TMBE001|Business Error|Report not exist|Report does not exist.
|TMBE002|Business Error|File error|Invalid file.
|TMBE003|Business Error|Invalid Column|Invalid column. [%s]
|TMBE004|Business Error|Empty Header|Empty header file.

