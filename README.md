# v6-gradle-springboot-restapi

### ビルド
```
$ ./gradlew build
```

### サーバー起動
```
$ ./gradlew bootRun
```

### curlで確認
```
$ curl -v localhost:8080/employees | json_pp
{
   "_embedded" : {
      "employeeList" : [
         {
            "_links" : {
               "employees" : {
                  "href" : "http://localhost:8080/employees"
               },
               "self" : {
                  "href" : "http://localhost:8080/employees/1"
               }
            },
            "id" : 1,
            "name" : "Bilbo Baggins",
            "role" : "burglar"
         },
         {
            "_links" : {
               "employees" : {
                  "href" : "http://localhost:8080/employees"
               },
               "self" : {
                  "href" : "http://localhost:8080/employees/2"
               }
            },
            "id" : 2,
            "name" : "Frodo Baggins",
            "role" : "thief"
         }
      ]
   },
   "_links" : {
      "self" : {
         "href" : "http://localhost:8080/employees"
      }
   }
}
```

```
$ curl -v http://localhost:8080/orders | json_pp
{
   "_embedded" : {
      "orderList" : [
         {
            "_links" : {
               "orders" : {
                  "href" : "http://localhost:8080/orders/3"
               },
               "self" : {
                  "href" : "http://localhost:8080/orders/3"
               }
            },
            "description" : "MacBook Pro",
            "id" : 3,
            "status" : "COMPLETED"
         },
         {
            "_links" : {
               "cancel" : {
                  "href" : "http://localhost:8080/orders/4/cancel"
               },
               "complete" : {
                  "href" : "http://localhost:8080/orders/4/complete"
               },
               "orders" : {
                  "href" : "http://localhost:8080/orders/4"
               },
               "self" : {
                  "href" : "http://localhost:8080/orders/4"
               }
            },
            "description" : "iPhone",
            "id" : 4,
            "status" : "IN_PROGRESS"
         }
      ]
   },
   "_links" : {
      "self" : {
         "href" : "http://localhost:8080/orders"
      }
   }
}
```

```
$ curl -v -X DELETE http://localhost:8080/orders/4/cancel | json_pp
{
   "_links" : {
      "orders" : {
         "href" : "http://localhost:8080/orders/4"
      },
      "self" : {
         "href" : "http://localhost:8080/orders/4"
      }
   },
   "description" : "iPhone",
   "id" : 4,
   "status" : "CANCELLED"
}
```
