# Proyecto Consentimientos
Este proyecto es una aplicación de negocios jBPM con Spring Bootç
Está basado en el TFG disponible en https://github.com/josgarlin/GestorConsentimientos
## Módulos
1. kjar: base de conocimiento kie, procesos, reglas de negocio, etc...
2. service: la aplicación
3. model: POJOs compartidos entre kjar y service
## Dependencias
**En la versión actual utilizamos:**
* _spring boot starter_ [_2.6.15_](https://mvnrepository.com/artifact/org.springframework.boot/spring-boot-starter/2.6.15)
* _kie server_: [_7.74.1.Final_](https://mvnrepository.com/artifact/org.kie/kie-server-spring-boot-starter/7.74.1.Final)
## Generación
Se han utilizado los arquetipos maven para la generación de los tres componentes (modelo, kjar y aplicación)
mvn archetype:generate -B "-DarchetypeGroupId=org.kie" "-DarchetypeArtifactId=kie-model-archetype" "-DarchetypeVersion=7.74.1.Final" "-DgroupId=us.dit" "-DartifactId=consentimientos-model" "-Dversion=1.0-SNAPSHOT" "-Dpackage=us.dit.consentimientos.model"
mvn archetype:generate -B "-DarchetypeGroupId=org.kie" "-DarchetypeArtifactId=kie-kjar-archetype" "-DarchetypeVersion=7.74.1.Final" "-DgroupId=us.dit" "-DartifactId=consentimientos-kjar" "-Dversion=1.0-SNAPSHOT" "-Dpackage=us.dit.consentimientos"
mvn archetype:generate -B "-DarchetypeGroupId=org.kie" "-DarchetypeArtifactId=kie-service-spring-boot-archetype" "-DarchetypeVersion=7.74.1.Final" "-DgroupId=us.dit" "-DartifactId=consentimientos-service" "-Dversion=1.0-SNAPSHOT" "-Dpackage=us.dit.consentimientos.service" "-DappType=bpm"
