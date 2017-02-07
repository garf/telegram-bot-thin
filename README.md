# Telegram Chat Bot

Telegram chat bot. 

Thin client. Requires centralized service "Mother" to receive info and generate reply. 

## Build
- Install [http://maven.apache.org/install.html](Maven)
- Run in root folder `mvn -e package`
- Compiled <filename>-jar-with-dependencies.jar and `config.properties` file will be in `target` folder

## Run
- Edit file `config.properties`
- Run `java -jar <filename>-jar-with-dependencies.jar`
