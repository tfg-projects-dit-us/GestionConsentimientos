@echo off

set mavenInput="%*"

if "%*" == "" (
	echo No Maven arguments skipping maven build
) else (
	echo Running with user input: %mavenInput%
	echo Running maven build on available project

	call mvn -v >con

	cd ..

	for %%s in ("-model" "human-tasks-management-kjar" "signals-samples-kjar" "consentimientos-kjar" "consentimientos-service") do (

			cd *%%s
			echo ===============================================================================
            for %%I in (.) do echo %%~nxI
            echo ===============================================================================

			if exist "%M3_HOME%\bin\mvn.bat" (
				call %M3_HOME%\bin\mvn.bat %* >con
			) else (
				call mvn %* >con
			)

			cd ..

	)
)

goto :startapp

:startapp
	if not x%mavenInput:docker=%==x%mavenInput% (
		echo Launching the application as docker container...
		call docker run -d -p 8090:8090 --name consentimientos-service apps/consentimientos-service:1.0-SNAPSHOT
	) else if not x%mavenInput:openshift=%==x%mavenInput% (
		echo Launching the application on OpenShift...
		call oc new-app consentimientos-service:1.0-SNAPSHOT
		call oc expose svc/consentimientos-service
	) else (
		echo "Launching the application locally..."
		setlocal EnableDelayedExpansion
		cd consentimientos-service
		cd target
		for /f "delims=" %%x in ('dir /od /b *.jar') do set latestjar=%%x
		cd ..
		call java -jar target\!latestjar!
REM		call java -Dorg.kie.server.bypass.auth.user=true -Dorg.kie.server.pwd=consentimientos -Dorg.kie.server.user=consentimientos -jar target\!latestjar!
	)


:end
