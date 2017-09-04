# Tranzfer

A basic (very, and also naive) microservice which handles accounts and supports being able to transfer money between those accounts.

## Requirements and Assumptions

The overall requirement is simply this:

    Design and implement a RESTful API (including data model and the backing implementation) for money
    transfers between accounts.
    
Which is pretty vague (although probably by design). So for the sake of making progress at the expense of asking loads of questions I've made the following assumptions:

* Accounts are known and managed as part of this system. 
* Money transfers are between internal accounts only (could do it via a client but then you have to start considering things like distributed transactions and all that joy).
* The data considerations are very basic. 
* Currency is not supported (although I did add it to one of the model definitions)
* The approach for handling transfers is done using a double entry accounting approach - so we have the debit account, the credit account, and the balance is adjusted for each. 
* The account balance is stored in the `Account` model, rather than being derived from all transactions against an account. This is more to cut down the number of models and keep it simple, although there would be performance implications in taking that approach in a production system. 
* `Account` holders can only add money to their account as part of their initial opening balance. There is no supported mechanism to add funds outside of them being transferred from someone who already has them. 
 
## System Overview

This has been built using the rather good [`Dropwizard`](http://www.dropwizard.io/1.1.4/docs/) which gives pretty much most of what you'd want from an HTTP based service. This bundles a Jetty instance so you don't need to worry about external containers/ servers. For persistence there is an embedded [`H2`](http://www.h2database.com/html/main.html) instance.

The system will handle database migrations on startup so there is nothing to be run outside of just starting the service. For the migrations I used [`Flyway`](https://flywaydb.org/) as I tend to prefer the `SQL` support for ease of use. 

### Running the project

##### Command Line
The project uses `maven` to manage dependencies and build everything. There is a plugin defined in the `pom.xml` that will build the project as part of the `package` command. If you want to run this from the command line (assuming `java` and `maven` are installed) you can run the following:

    // From project root
    mvn package
    // The .jar will be under `target`. You can run it as follows
    java -jar ./target/Tranzfer-1.0-SNAPSHOT.jar server ./src/main/resources/tranzfer_conf.yml
    
The `server` and location of the config files are used by `Dropwizard` to run and maintain the system. Nothing else should be required. 

##### Intellij IDEa
Running from within an IDE is a little less involved than running from the command line. If you're working in IntelliJ then you should click `Edit configurations` as part of the run menu at the top. Add a configuration for an `Application`, and ensure the `Main` class points to `TranzferApplication`. Once you have that you should add the following as `Program arguments` -> `server ./src/main/resources/tranzfer_conf.yml`.

No matter how you run this, the service will be running on localhost and bind to port `8080`. 

### REST Endpoints

A high level overview of the endpoints is given in the below table. The subsequent sections will go into more detail around these:

| Endpoint                   | Method  | Description                                          |
|:---------------------------|:-------:|:-----------------------------------------------------|
| /                          | GET     | Root of the system. Returns basic system information |
| /account                   | GET     | Get a list of all accounts defined in the system.    |
| /account                   | POST    | Create a new account                                 |
| /account/{account_number}  | GET     | Retrieve a specific account by the account number    |
| /account/{account_number}  | PUT     | Update a particular account (holder name only)       |
| /transfer                  | GET     | Retrieve a list of all transfers currently defined   |
| /transfer                  | POST    | Initiate a money transfer between two known accounts |
| /transfer/{transfer_id}    | GET     | Retrieve a specific transfer record                  |

##### /

Hitting the root url will simply return some very basic self-promotional System Information.

    curl localhost:8080
    // HTTP 200
    {
        "authorName": "Mike Holmes",
        "applicationName": "Tranzfer"
    }

##### /account

Hitting the `/account/` root url will return a listing of all `account`s currently defined in the system. If there are none you'll just get an empty array:

    curl localhost:8080/account
    // HTTP 200
    [
        {
            "id": 1,
            "account_number": "149b046a-b033-4132-b934-bf617e40d4ab",
            "account_holder": "Mike Holmes",
            "account_balance": 10000.56,
            "created_at": "17-Sep-04 13:10:36"
        },
        ...
    ]
    
You can create an account by passing an initial `account` object to the `/account` url. An account requires the `account_holder` field to be populated (which is the name of the person holding the account) but can also take an initial `account_balance` field (which currently is the only way of getting money into an account). What is returned is a far more complete `account` model.

    curl -X POST -d "{ \"account_holder\":\"Mike Holmes\", \"account_balance\": 10000.56}" localhost:8080/account
    // HTTP 201
    {
        "id": 1,
        "account_number": "149b046a-b033-4132-b934-bf617e40d4ab",
        "account_holder": "Mike Holmes",
        "account_balance": 10000.56,
        "created_at": "17-Sep-04 13:10:36"
    }

If you need to retrieve a specific account, you can make a request to `/account/{account_number}` using the `account_number` property that would be generated when creating the account. 

##### /transfer

The root url will return a listing of all `transfer` records in the system:

    curl lcoalhost:8080/transfer
    // HTTP 200
    [
        {
        	"credit_account_no": "caf85fbd-68da-4161-84ed-df9e3a00208d",
        	"debit_account_no": "5cac40c0-4e0c-4856-a5ab-69678ff49f4e",
        	"amount": 200.00,
        	"currency": "GBP"
        },
        ...
    ]
    
### Example Usage

#### A ~Real Life~ Totally Made Up Scenario

Rod, Jane and Freddie are friends. Each of them has an account with `Tranzfer` into which they have added what they have saved up. Rod has the most money:

    // POST /account
    {
    	"account_holder": "Rod",
    	"account_balance": 10000.00
    }
    
Jane is a female which means that she has approximately 9.4% less money than Rod for no other reason than she is a female (which incidentally is a travesty, although not one that will be solved here) [source](https://fullfact.org/economy/UK_gender_pay_gap/): 

    // POST /account
    {
    	"account_holder": "Jane",
    	"account_balance": 9400.00
    }
    
Freddie is pretty good on the guitar but has an unfortunate penchant for faberge eggs and as such has no money. Ever.

    // POST /account
    {
    	"account_holder": "Freddie"
    }
    
So now all 3 have an account in various degrees of financial solubility:

    // GET /account
    [
        {
            "id": 1,
            "account_number": "caf85fbd-68da-4161-84ed-df9e3a00208d",
            "account_holder": "Rod",
            "account_balance": 10000,
            "created_at": "17-Sep-04 13:21:37"
        },
        {
            "id": 2,
            "account_number": "5cac40c0-4e0c-4856-a5ab-69678ff49f4e",
            "account_holder": "Freddie",
            "account_balance": 0,
            "created_at": "17-Sep-04 13:25:15"
        },
        {
            "id": 3,
            "account_number": "07b39e27-ab7a-4bc3-b688-7ac9a1a7e2b2",
            "account_holder": "Jane",
            "account_balance": 9400,
            "created_at": "17-Sep-04 13:26:21"
        }
    ]
    
Freddie needed a loan to purchase a particular Faberge egg that he saw on ebay and as such asked to borrow £100 from Rod. Rod agreed and transferred the money to him:

    // POST /transfer
    {
    	"debit_account_no": "caf85fbd-68da-4161-84ed-df9e3a00208d",
    	"credit_account_no": "5cac40c0-4e0c-4856-a5ab-69678ff49f4e",
    	"amount": 100.00,
    	"currency": "GBP"
    }
    
We can now see that there is a single `transfer` defined in the system:

    // GET /transfer
    [
        {
            "amount": 100,
            "currency": "GBP",
            "id": 1,
            "debit_account_no": "caf85fbd-68da-4161-84ed-df9e3a00208d",
            "credit_account_no": "5cac40c0-4e0c-4856-a5ab-69678ff49f4e",
            "created_at": "17-Sep-04 13:33:49"
        }
    ]
    
Also we can see that the money has indeed been transferred between Rod and Freddie:

    // GET /account
    [
        {
            "id": 1,
            "account_number": "caf85fbd-68da-4161-84ed-df9e3a00208d",
            "account_holder": "Rod",
            "account_balance": 9900,
            "created_at": "17-Sep-04 13:21:37"
        },
        {
            "id": 2,
            "account_number": "5cac40c0-4e0c-4856-a5ab-69678ff49f4e",
            "account_holder": "Freddie",
            "account_balance": 100,
            "created_at": "17-Sep-04 13:25:15"
        }
    ]
    
Unknown to Freddie however, Rod makes his money as a loan shark charging oppressive levels of interest on relatively small amounts. As such, Rod informs Freddie that the amount owed back to him is actually now £200. Freddie tries to transfer the money to Freddie (knowing full well that he can't afford it):

    // POST /transfer
    {
    	"credit_account_no": "caf85fbd-68da-4161-84ed-df9e3a00208d",
    	"debit_account_no": "5cac40c0-4e0c-4856-a5ab-69678ff49f4e",
    	"amount": 200.00,
    	"currency": "GBP"
    }
    
    // HTTP 400 (Bad Request)
    // Account # 5cac40c0-4e0c-4856-a5ab-69678ff49f4e does not have  enough funds to make a £200.00 transfer
    
So Freddie had to request money from Jane to cover the oppressive interest charged by Rod:

    // POST /transfer
    {
    	"credit_account_no": "5cac40c0-4e0c-4856-a5ab-69678ff49f4e",
    	"debit_account_no": "07b39e27-ab7a-4bc3-b688-7ac9a1a7e2b2",
    	"amount": 100.00,
    	"currency": "GBP"
    }

So now Freddie checks his account and has enough to be able to pay Rod back:

    // GET /account/5cac40c0-4e0c-4856-a5ab-69678ff49f4e
    {
        "id": 2,
        "account_number": "5cac40c0-4e0c-4856-a5ab-69678ff49f4e",
        "account_holder": "Freddie",
        "account_balance": 200,
        "created_at": "17-Sep-04 13:25:15"
    }

Freddie can send the money now to Rod:

    // POST /transfer
    {
    	"credit_account_no": "caf85fbd-68da-4161-84ed-df9e3a00208d",
    	"debit_account_no": "5cac40c0-4e0c-4856-a5ab-69678ff49f4e",
    	"amount": 200.00,
    	"currency": "GBP"
    }
    
Finally we can see that Freddie is now back to having no money. Rod is the only one to have made any money out of the whole scenario. Balance has been restored. 

    // GET /account
    [
        {
            "id": 3,
            "account_number": "07b39e27-ab7a-4bc3-b688-7ac9a1a7e2b2",
            "account_holder": "Jane",
            "account_balance": 9300,
            "created_at": "17-Sep-04 13:26:21"
        },
        {
            "id": 2,
            "account_number": "5cac40c0-4e0c-4856-a5ab-69678ff49f4e",
            "account_holder": "Freddie",
            "account_balance": 0,
            "created_at": "17-Sep-04 13:25:15"
        },
        {
            "id": 1,
            "account_number": "caf85fbd-68da-4161-84ed-df9e3a00208d",
            "account_holder": "Rod",
            "account_balance": 10100,
            "created_at": "17-Sep-04 13:21:37"
        }
    ]