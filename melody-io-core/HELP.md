## Spring Boot testing
1. package must difference with main for testing rSocket 

ex. com.melody.test

2. Use difference application boot

https://gfajardo.medium.com/use-rsocket-to-send-data-from-a-cloud-pub-sub-subscription-to-a-recharts-ui-67fa1b201112

## Middleware

Client <-> Python-RSocket <-> NATS-Stream <-> Golang-CQRS

## Python Enviroment
https://medium.com/codex/python-version-management-with-pyenv-and-pyenv-virtualenv-linux-ecd6578b7bbf

virtualenv --python=/Users/tjikaljedy/.pyenv/versions/3.9.1/bin/python ~/melody-io-env
source ~/melody-io-env/bin/activate

## Python RSocket

The main reason not using golang in middleware, golang version RSocket still far from perfect

#### Module

pip install rsocket
pip install pyjwt
pip install nats-py
pip install transitions

sudo adduser <username> sudo

sudo usermod -a -G sudo <username>


# Golang Framework

## CQRS

### Repository handle
https://outcrawl.com/go-microservices-cqrs-docker
https://shijuvar.medium.com/building-microservices-with-event-sourcing-cqrs-in-go-using-grpc-nats-streaming-and-cockroachdb-983f650452aa

### RSocket
https://github.com/jjeffcaii

### MongoDB
https://github.com/AleksK1NG
https://github.com/avelino/awesome-go#database

### Example CQRS Financial
https://github.com/pperaltaisern	
https://github.com/shijuvar

### Golang init project
go mod init <name>
go mod init melody-io/core-es  
go mod init melody-io/midware-es  

go get github.com/modernice/goes/...@main
go mod tidy
go mod edit -replace melody-io/core-es/=core-es

# Manual Install Kafka
1. Install jdk 13
a. download from sunsdk and run installation, execute following:
b. source ~/.zshrc
c. check java version:
	java -version

Or openjdk

> brew cask
> brew tap adoptopenjdk/openjdk
> brew install adoptopenjdk13
> brew autoremove

2. Kafka
https://www.tutorialkart.com/apache-kafka/install-apache-kafka-on-mac/
https://kafka.apache.org/quickstart
https://david-yappeter.medium.com/golang-pass-by-value-vs-pass-by-reference-e48aac8b2716

3. Kafka UI
https://github.com/provectus/kafka-ui

## Build Kafka UI
./mvnw clean install -Pprod

## Run Kafka
$ sh bin/zookeeper-server-start.sh config/zookeeper.properties
$ sh bin/kafka-server-start.sh config/server.properties

## Run Nats
export GO111MODULE=on
export GOMODCACHE=~/golang/pkg/mod

sudo killall -HUP mDNSResponder

brew services stop mongodb-community
nats-streaming-server
kill $(lsof -ti:4222)

# Run Todo Examples
export MONGO_URL="mongodb://localhost:27017/eventstore"
export NATS_URL="nats://localhost:4222" 

# Installing multiple Go versions
You can install multiple Go versions on the same machine. For example, you might want to test your code on multiple Go versions. For a list of versions you can install this way, see the download page.

Note: To install using the method described here, you'll need to have git installed.

## To install additional Go versions, 
run the go install command, specifying the download location of the version you want to install. The following example illustrates with version 1.10.7:

$ go install golang.org/dl/go1.10.7@latest
$ go1.10.7 download
To run go commands with the newly-downloaded version, append the version number to the go command, as follows:

$ go1.10.7 version
go version go1.10.7 linux/amd64
When you have multiple versions installed, you can discover where each is installed, look at the version's GOROOT value. For example, run a command such as the following:

$ go1.10.7 env GOROOT
To uninstall a downloaded version, just remove the directory specified by its GOROOT environment variable and the goX.Y.Z binary.

## Uninstalling Go linux / macOS / FreeBSD
Delete the go directory.
This is usually /usr/local/go.

Remove the Go bin directory from your PATH environment variable.
Under Linux and FreeBSD, edit /etc/profile or $HOME/.profile. If you installed Go with the macOS package, remove the /etc/paths.d/go file

##VPN
tjikal.jedy
KelasPintar@2022!


## Install MYSQL in MacOS

https://flaviocopes.com/mysql-how-to-install/
https://www.positronx.io/how-to-install-mysql-on-mac-configure-mysql-in-terminal/
root password: K3l4spintar@123

## Run MYSQL
mysql.server start
mysql.server stop


# AxonFramework CQRS

Ref:
- https://github.com/AxonIQ/hotel-demo/tree/master/inventory/src/main/java/io/axoniq/demo/hotel/inventory
- https://github.com/ivangfr/axon-springboot-websocket
- https://github.com/fraktalio/restaurant-demo
- https://github.com/fraktalio/restaurant-demo/blob/main/src/main/kotlin/com/fraktalio/restaurant/command/configuration/RestaurantCommandConfiguration.kt


@MessageMapping("users.{email}.get")
public Flux<UserAuthDto> user_subscribe(@DestinationVariable String email) {
	System.out.println(">[users.{email}.get] " +email);
	return reactorQueryGateway.subscriptionQueryMany(new FindUserAuth(email), UserAuthDto.class);
}

@MessageMapping("user.add")
public Mono<Void> add_subscribe(@Payload UserAuthDto userRequestData) {
	System.out.println(">[user.add] " );
	return reactorCommandGateway.send(new AddUserCommand(userRequestData.getEmail()));
}

@MessageMapping("user.add2")
public Mono<Void> add_subscribe2() {
	System.out.println(">[user.add] " );
	return reactorCommandGateway.send(new AddUserCommand(""));
}


## Cert pem to p12
brew install mkcert
brew install nss

mkcert -key-file melody-io-key.pem -cert-file melody-io.pem  melody.io "*.melody.io" 10.106.11.37 localhost 127.0.0.1 ::1 
openssl pkcs12 -export -out melody-io.p12 -inkey melody-io-key.pem -in melody-io.pem -passin pass:changeit -passout pass:changeit  


### Convert p12 to jks
keytool -importkeystore -srckeystore melody-io.p12 -srcstoretype pkcs12 -destkeystore melody-io.jks -deststoretype jks


## RSocket
rsc  --route=user.login --data="{\"email\":\"test@test.com\",\"password\":\"test\",\"deviceId\":\"device\"}" --debug ws://127.0.0.1:6565/ws
rsc  --route=user.login --data="{\"email\":\"test@test.com\",\"password\":\"test\",\"deviceId\":\"device\"}" --debug tcp://127.0.0.1:6565

Commaandline:
https://github.com/making/rsc

Ref for callback to user:
https://github.com/oli-broughton/livestream-chat
https://dev.to/olibroughton/building-a-scalable-live-stream-chat-service-with-spring-webflux-redis-pubsub-rsocket-and-auth0-22o9

Ref:
https://domenicosibilio.medium.com/rsocket-with-spring-boot-js-zero-to-hero-ef63128f973d
https://github.com/dsibilio/rsocket-demo
https://github.com/gregwhitaker/springboot-rsocketjwt-example
https://github.com/vinsguru/vinsguru-blog-code-samples/tree/master/rsocket
https://github.com/jhhong0509/rsocket-chatting-example/blob/main/chatting/src/main/java/jhhong/example/rsocketchatting/global/security/config/SecurityConfig.java


#NATS
brew install nats-streaming-server
nats-streaming-server
kill $(lsof -ti:4222)

# REDIS
brew install redis

"vmArgs":"--add-exports=java.base/sun.nio.ch=ALL-UNNAMED --add-exports=jdk.unsupported/sun.misc=ALL-UNNAMED --add-opens=java.base/sun.nio.ch=ALL-UNNAMED --add-opens=java.base/java.net=ALL-UNNAMED --add-opens=java.base/java.util.jar=ALL-UNNAMED --add-opens=java.base/java.util.zip=ALL-UNNAMED --add-opens=java.base/jdk.internal.module=ALL-UNNAMED --add-opens=java.base/java.nio=ALL-UNNAMED --add-opens=java.base/java.lang.module=ALL-UNNAMED --add-opens=java.base/java.io=ALL-UNNAMED --add-opens=java.base/sun.security.util=ALL-UNNAMED --add-opens=java.base/jdk.internal.loader=ALL-UNNAMED --add-opens=java.base/java.security=ALL-UNNAMED --add-opens=java.base/jdk.internal.ref=ALL-UNNAMED --add-opens=java.base/java.util.concurrent=ALL-UNNAMED --add-opens=java.base/java.lang.ref=ALL-UNNAMED --add-opens=java.base/java.util.concurrent.atomic=ALL-UNNAMED --add-opens=java.base/java.util.concurrent.locks=ALL-UNNAMED --add-opens=java.base/java.util=ALL-UNNAMED --add-opens=java.base/java.lang=ALL-UNNAMED"

--add-opens=java.base/java.nio=ALL-UNNAMED
--add-opens=java.base/sun.nio.ch=ALL-UNNAMED

## Testing Flow
rsc  --route=user.signup --data="{\"email\":\"test@test.com\",\"password\":\"test\",\"deviceId\":\"device\"}" --debug ws://127.0.0.1:6565/ws

rsc  --route=user.login --data="{\"email\":\"test@test.com\",\"password\":\"test\",\"deviceId\":\"device\"}" --debug ws://127.0.0.1:6565/ws