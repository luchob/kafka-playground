# Welcome

This is just a playground project which I use to play around with Kafka. The project is equipped with a docker image of Kafka. 
spotify/kafka is used. The kafka shell script are needed for the example.

# Starting the docker image

From the directory of the project:

```
docker-compose -f docker-compose.yaml up
```

# Navigating the cases

* Stop the app.
* In `application.yaml` change the `demo.case` to `caseN` where `N` is the number of the case which should be emulated.

