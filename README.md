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

# Case infinite loop on dev error

In this case `N=1`. To make things easier create a topic with 1 partition.

```
bin/kafka-topics.sh --create --zookeeper localhost:2181 --replication-factor 1 --partitions 1 --topic test-topic-case1
```

Start a producer:

```
bin/kafka-console-producer.sh --broker-list localhost:9092 -topic test-topic-case1
```

Start the app. Send several messages and check the echo on the console.
Send a message with text crash. The app is effectively in infinite loop now.

# Case inconsistent consumption

In this case `N=2`. To make things easier create a topic with 1 partition.

```
bin/kafka-topics.sh --create --zookeeper localhost:2181 --replication-factor 1 --partitions 1 --topic test-topic-case2
```

Start a producer:

```
bin/kafka-console-producer.sh --broker-list localhost:9092 -topic test-topic-case2
```

* Start the app. 
* Send several messages and check the echo on the console.
* Send a message with text `crash`. 
* Stop the app. 
* Start the app again. 

The faulty message is reconsumed.

Clean up all topics. Create `test-topic-case2`.


* Start the app. 
* Send several messages and check the echo on the console.
* Send a message with text crash. 
* Send a message with text `hello`.
* Stop the app. 
* Start the app again. 
* The faulty message is NOT reconsumed.

# Clean up all topics

From the directory of the project:

```
docker-compose -f docker-compose.yaml down
docker container prune
```

Press `y`.