FROM openjdk:22-slim-bookworm
LABEL mentainer="t.h.minh101002@gmail.com"
WORKDIR /app
COPY target/BirdFarmShop-0.0.1-SNAPSHOT.jar /app/bird_farm_shop_project.jar
ENTRYPOINT ["java", "-jar", "bird_farm_shop_project.jar"]