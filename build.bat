@start cmd /c "call mvn clean package -DskipTests -f eshop/pom.xml && call mvn clean package -DskipTests -f catalogservice/pom.xml && call mvn clean package -DskipTests -f categoryservice/pom.xml && call mvn clean package -DskipTests -f eureka/pom.xml"

call mvn clean package -DskipTests -f userservice/pom.xml
call mvn clean package -DskipTests -f zuul/pom.xml
call mvn clean package -DskipTests -f dashboard/pom.xml
call mvn clean package -DskipTests -f oauth/pom.xml
call mvn clean package -DskipTests -f productservice/pom.xml