# v6-gradle-springboot-restapi

### ビルド
```
$ gradle build
```

### サーバー起動
```
$ ./gradlew bootRun
```

### curlで確認
```
$ curl -v localhost:8080/employees | json_pp

  % Total    % Received % Xferd  Average Speed   Time    Time     Time  Current
                                 Dload  Upload   Total   Spent    Left  Speed
  0     0    0     0    0     0      0      0 --:--:-- --:--:-- --:--:--
 0*   Trying 127.0.0.1:8080...
* TCP_NODELAY set
* Connected to localhost (127.0.0.1) port 8080 (#0)
> GET /employees HTTP/1.1
> Host: localhost:8080
> User-Agent: curl/7.68.0
> Accept: */*
>
  0     0    0     0    0     0      0      0 --:--:-- --:--:-- --:--:--
  0     0    0     0    0     0      0      0 --:--:--  0:00:01 --:--:--
  0     0    0     0    0     0      0      0 --:--:--  0:00:02 --:--:--
  0     0    0     0    0     0      0      0 --:--:--  0:00:03 --:--:--
  0     0    0     0    0     0      0      0 --:--:--  0:00:04 --:--:--
 0* Mark bundle as not supporting multiuse
< HTTP/1.1 200
< Content-Type: application/hal+json
< Transfer-Encoding: chunked
< Date: Mon, 21 Feb 2022 14:32:12 GMT
<
{ [432 bytes data]
03
* Connection #0 to host localhost left intact
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
