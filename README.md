# GreenBank bootcamp demo APP

This project contains a demo application to be used at green coding bootcamp.

The application uses Quarkus framework as it's base. To learn more about Quarkus, please visit its website here: https://quarkus.io/.

## Quick info

The application uses an in-memory DB to temporary save content. Unless differently specified, each application restart clears the database.

The application is divided into three layers: resources (AKA controllers), services and repositories. Each layer has its own Java package.

The application uses entities objects as DTO just for simplicity. Usually, entities and DTO should be separated.

The application uses JSR-380 validation, with some standard annotations added at entity/DTO level.

The application doesn't use getters/setters opting for public variables. This both for semplicity and to avoid to add other libraries (like Lombok).

We introduced some basic checks, but this application is far from being 'safe' and 'bug-free'.

We intentionally introduced some bugs (the purpose is to find them). If something is not working as expected, maybe there is a reason!

We intentionally added some unwanted/unnecessary computation (for measurement purposes), be aware that those pieces of code can (SHOULD) be safely deleted!

We left also some hints that should point you to the most awful things, but there are also some tricky ones!

> **_WARN:_**  Useless to say, nonetheless... do not use this application in a production environment :smile:!

## How to code the application

You can use GitHub Codespaces to edit and try this application. From the repository, create a new codespace that will create a new remote environment that you can use to modify the code.

At first launch, codespace could ask you to install some suggested extensions. Just allow/install whatever it asks, since nothing will be persisted on your PC outside your local browser storage.

Through the left panel you will be able to browse the project files and open them to edit the code.

Through the terminal (in the bottom side of the window) you will be able to issue commands to start the application, execute Maven tasks, monitor the application usage and (in general) whatever you could need in this bootcamp.

Once you have finished to apply your changes, Codespaces will let you save your result as a new repository in your GitHub account. Once the bootcamp will be over, you are free to delete the codespace, the repository and your GitHub account if you don't need / don't want to use it.

If you want, you can also clone this repository and work locally on your PC using the IDE that you prefer. To continue with the bootcamp however, you must be able to push your local changes to your repository, since a codespace will be needed anyway.

## Configuring the application

Before starting the application (as described below) please, take a moment to configure the application opening the `application.properties` file that can be found under the `src/main/resources` folder and setting everything is needed.

## Running the application in dev mode

You can run your application in dev mode that enables live coding using:
```shell script
./mvnw compile quarkus:dev
```

> **_NOTE:_**  Quarkus now ships with a Dev UI, which is available in dev mode only at <your_github_codespace_url:8080>/q/dev/.

> **_NOTE:_**  The default port is 8080. If you need to use another port, head to `application.properties` file. The rest of the file readme assume that you are using this port. You can replace '8080' with whatever port you are going to use in the following instructions.

## Enabling port-forwarding

To be able to access the application (if not already done) enable the port-forwarding in your codespace for the 8080 port. There should be a tab in your codespace window named "Ports", in the bottom side of your screen. Follow the instructions to open the 8080 port.

Codespaces will provide you a URL that is your `<your_github_codespace_url>`. Append to this URL the application path you want to access (like Swagger UI)

## Accessing the exposed APIs

The project includes Swagger UI that is enabled by default when using the application in dev mode. You can reach Swagger UI pointing your browser to: `<your_github_codespace_url>/q/swagger-ui/`.

## Packaging and running the application

The application can be packaged using:
```shell script
./mvnw package
```

The application, packaged as an _über-jar_, is now runnable using the command:
``` shell script
java -jar target/*-runner.jar
```
> **_NOTE:_** Use the command `nohup java -jar <jar_file> &` if you want to avoid the process being interrupted after you close the connection with the remote machine and to be able to re-use the same shell.

## Running SonarQube

Unzip SonarQube (if not done already) in any folder of your drive. No setup is required in order to continue.

Configure (if needed) SonarQube editing the `application.properties` file under the `<sonarqubeHome>/conf` folder.

Unzip the SonarQube plugin (if not done already) in any folder of your drive.

Copy the .jar file of the plugin under the `<sonarqubeHome>/extensions/plugins` folder.

Start the web server, launching the script under the `<sonarqubeHome>/bin/<OS>` folder, where OS is your operating system.

Open a new browser window and browse to: http://localhost:9000.

> **_NOTE:_** If the 9000 port is not available to you, change it from the `application.properties` file.

> **_NOTE:_** The default system administrator credentials are admin / admin.

## Identify the running Java process

You can take the Java process ID with the command:
``` sheell script
ps aux | grep java
```

## How to measure the CPU usage

You first need to install the tool we are going to use into your codespace instance. Issue the following commands in your terminal:
```shell script
sudo apt update
sudo apt install -y sysstat
```

You can measure the usage of your execution with the following command:
```shell script
timeout --signal=SIGINT <execution_time> pidstat -u -p <process_id> 1
```
where:
- `execution_time`: is the duration time, after which the process will be interrupted
- `process_id`: is the process ID (PID) of the running Java application

The `pidstat` command will provide you the average of the CPU usage for this `process_id`, after `timeout` will interrupt it.

Since Codespaces is using several CPU cores to run your remote instance, to obtain the best possible results with the test measurement you can use process CPU affinity to constrain the Java process to use only one CPU.

The command to be used in this case is `taskset`. When starting the application for measurement purposes, issue the following command:
``` sheell script
taskset -c 0 java -jar target/*-runner.jar &
```

> **_WARN:_**  To be able to launch the packaged application, you must package it first! Follow the 'Packaging and running the application' section of this readme file.

## Automate the measurement process

To help you to automate these tasks, we added a bash script (under the `src/main/resources` folder) that will automatically run the (async) measurement API and start the measurement. The application must be running in order to use the script, you have to do it manually.

To start the script, move to the `src/main/resources` in a terminal (using the `cd` commnand) and start it using the following command:
``` sheell script
sh bash_script.sh
```

> **_WARN:_** Please keep in mind that the script cannot cover all possible cases of your local environment. If the script doesn't work for you, adjust it according to your setup or procede to manully measure the application.

The script will ask you the followings:
- the port used by the application, it defaults to 8080 if nothing is provided
- the measurement time, it defaults to 60 if nothing is provided
- how many runs to be used, it defaults to 10 if nothing is provided
- the running Java application PID, it tries to figure out what is it if nothing is provided

Once the setup is completed, the measurement process with `pidstat` will start automatically and you should receive the average result after the measurement time has passed.
