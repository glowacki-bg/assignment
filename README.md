# Java assignment 1 - Bartosz Głowacki

### What does this service do?
Service calculates total price after discount for given order data:
* productId - UUID
* amount - integer value greater than 1
* unit price - decimal value with precision = 12 and scale = 2, greater than 0.01.

### How to configure discounts?
Discounts can be configured via application properties (in real world it would be probably configurable via REST API) and comes preconfigured with defult values:

```
discount:
  type: PERCENTAGE
  amount[0]:
    items: 10
    cash-discount: 2
  amount[1]:
    items: 100
    cash-discount: 5
  percentage[0]:
    items: 10
    percentage-discount: 3
  percentage[1]:
    items: 50
    percentage-discount: 5
```
    
Property discount.type decides which policy is used. 
If AMOUNT is chosen, for each discount.amount.items threshold, discount of discount.amount.cash-discount is granted
Examples:
* For request:
```
{
    "productId": "02acee19-1df7-4621-8f9d-de2215b75716",
    "amount": 12,
    "unitPrice": 2
}
```
totalPrice will be equal to 22.00 (cash discount for items number above 10 is 2$)
* For request:
```
{
    "productId": "02acee19-1df7-4621-8f9d-de2215b75716",
    "amount": 102,
    "unitPrice": 2
}
```
totalPrice will be equal 199.00 (cash discount for items number above 100 is 5$)

If PERCENTAGE is chosen, for each discount.percentage.items threshold, discount of discount.percentage.percentage-discount is granted
Examples:
* For request:
```
{
    "productId": "02acee19-1df7-4621-8f9d-de2215b75716",
    "amount": 12,
    "unitPrice": 2
}
```
totalPrice will be equal to 23.28 (percentage discount for items number above 10 is 3%)
* For request:
```
{
    "productId": "02acee19-1df7-4621-8f9d-de2215b75716",
    "amount": 102,
    "unitPrice": 2
}
```

totalPrice will be equal to 193.80 (percentage discount for items number above 50 is 5%)


### How to launch it?
I order to run this application in docker, please follow below steps in directory /assignment:
* `./mvnw spring-boot:build-image`
* `docker run -it -p8080:8080 assignment:0.0.1`

to override default discount configuration, additional arguments can be used, e.g.:

* `docker run -it -p8080:8080 assignment:0.0.1 --discount.type=AMOUNT` uses default amount discount configuration
* `docker run -it -p8080:8080 assignment:0.0.1 --discount.type=AMOUNT --discount.amount[0].items=2 --discount.amount[0].cash-discount=1` uses custom amount discount configuration



