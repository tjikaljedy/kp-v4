# RSOCKET Client

rsc --stream --route=user.login  --debug tcp://localhost:6565
gradlew thinResolve
java -Dthin.root=. -jar /Users/tjikaljedy/Workspace/kelaspintar/workspace/backend/melody-io-broker/build/thin/deploy/melody-io-broker-10.0.0.jar --thin.debug=true