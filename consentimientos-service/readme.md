# Servicio Consentimientos
El componente "consentimientos-service" contiene la aplicación de negocio del proyecto para la gestión de consentimientos
## Tecnologías
Utiliza Spring boot 2 y jBPM (kie server 7.74.1.Final)
## Generación
Se ha generado con el arquetipo mvn con las opciones:
* ` mvn archetype:generate -B "-DarchetypeGroupId=org.kie" "-DarchetypeArtifactId=kie-service-spring-boot-archetype" "-DarchetypeVersion=7.74.1.Final" "-DgroupId=us.dit" "-DartifactId=consentimientos-service" "-Dversion=1.0-SNAPSHOT" "-Dpackage=us.dit.consentimientos.service" "-DappType=bpm"`

Pero posteriormente se han hecho los siguientes cambios a la configuración por defecto:
1. En pom.xml se ha añadido, para que utilice Spring boot 2.6.15
2. Se ha cambiado la configuración de seguridad para que sea conforme a los nuevos mecanismos de Spring
3. Se ha cambiado el banner por defecto. Se ha usado la web: https://manytools.org/hacker-tools/ascii-banner/
4. Se ha añadido el fichero consentimientos-service.xml para incluir la configuración del servidor kie
5. En el script de arranque para windows _launch.bat_ se ha cambiado la sentencia de arranque local, incluyendo opciones _call java -Dorg.kie.server.bypass.auth.user=true -Dorg.kie.server.pwd=consentimientos -Dorg.kie.server.user=consentimientos -jar target\!latestjar!_

## Ejecución
* application.properties está preparado para usar postgres como bbdd por defecto (se ha manenido el original como BU)
* Debe estar creada la BBDD y el usuario en el servidor postgres y después ejecutar (base de datos consentimientos, usuario jbpm). Pero se puede cambiar la configuración de base de datos en el fichero de propiedades
* En windows ejecutar .\launch.bat clean install -Ppostgres
* Se ha añadido una clase para la configuración de las variables del sistema que configuran el servidor kie (o cualquier otra). Es necesario añadir en application.properties la variable con el prefijo system.properties
