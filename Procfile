web: java -javaagent:newrelic/newrelic.jar -agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=$DEBUG_PORT -Dserver.port=$PORT -Denvironment=prod -XX:+UseConcMarkSweepGC -jar $PATH_TO_JAR
