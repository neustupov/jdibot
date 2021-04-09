#### jdibot

Used:

- Spring Boot 2.4.2
  - Spring Web
  - Spring Security
  - Spring Data MongoDB

- Telegram API - org.telegram.telegrambots 5.0.1.1

- JUnit 5
- Mockito
- Embedded MongoDB 

Used for containerization:
- Docker
- Docker Compose

-----

 #### Build Docker Image
 
- in app directory: docker build -t jdibot .
  
  "-t" this is a "TAG", in this case it is "jdibot"
  
  "." this is the directory from which the image is being built

#### Run temp MongoDB image

- docker run --rm -d -p 27017:27017 mongo

  "--rm" remove image after stop
  "-d" running in the background
  "-p" ports from:to

#### Run app docker image

- docker run --rm --name jdibot -p 5000:5000 jdibot

  "--rm" remove image after stop
  "--name" name container 
  "-p" ports from:to 
  "jdibot" name of image for start container

#### With Docker Compose

For run docker with docker-compose:
$ docker-compose --env-file "./.env" up

 - ".env" - config for docker-compose.yaml
 - "mongo-init.js" - for init database at first start

-----

- @BotFather - for register and config bot
- NGRok https://ngrok.com/download - for proxy requests
- https://api.telegram.org/bot{my_bot_token}/setWebhook?url={url_to_send_updates_to} - for set bot webHook
