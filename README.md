# CH-Payments
Welcome to CH Payments, a simple paymentprovider wrapper for stuff you want CH to get money for.
Payments makes it easy to create products, and implement it in your own website.
No hassle with keys, accounts and webhooks, just a single call to initiate a payment. 

Below you can find some ready-to-use code examples for your website.

The administration panel (for now) is only available for board members. 
They can create products to generate the keys required.

#Code examples
No idea how to code, but still want to sell tickets on your CH site? 
Use one of these code examples to get going. 

//Insert code templates here #6

#HootHub
This project currently is quite minimal, and offers lots of opportunities for great features built by you!
Check out the issue page for HootHub issues, and start earning Uilenballen. 

First, pick an issue and self-assign it. Make your changes in a new branch, with the following naming convention:

- Fixing a bug? > "fix-description_of_bug"
- Implementing a new feature? > "feature-description_of_feature"

Once you're satisfied with your changes, create a pull request and give it the label "Ready for review". 
You can assign someone in specific or wait for someone to pick it up. 
Make sure to include tests and documentation. 
If Travis isn't happy, we're not happy.

#Running
## From IntelliJ
1.  Import the project into IntelliJ IDEA, we really recommend using [IntelliJ IDEA Ultimate Edition](https://www.jetbrains.com/idea/), since it includes all the support for Spring. You could use another IDE, but we do not recommend this
2.  Make sure you have installed the `Lombok Plugin`
3.  Enable annotation processing, this can be enabled in `Settings > Build, Execution, Deployment > Compiler > Annotation Processors`. Here you have to check the checkmark that says `Enable Annotation Processors`
4.  Copy `config/application.properties.sample` to `config/application.properties`.

  You should change:
  - `payments.molliekey` and `payments.returnUrl` to their respective keys if you have those
  - Add the group that you are a part of to the `payments.admin.groups`

5.  Right click the `Application` class (`src -> main -> java -> ch.wisv.payments`) and choose `Run`. Terminate the process (you don't have to wait for it to finish starting). Now go to the Run/Debug Configuration window `Run -> Edit Configurations` choose the `Spring Boot` configuration called `Application`. Enable the dev profile for this configuration by entering `dev` in the `Active Profiles` box.

## Running without IntelliJ
It is also possible to run without IntelliJ. This can be done by performing all the previous instructions `1-4`, after this run the `./gradlew dev` command.

## Build
To generate a runnable JAR file, make sure that you have followed all the instructions under the **run** section. When you have done so, run `./gradlew build`. This command will run all tests, and create a runnable JAR file in the `./build` folder. You could also run `Build` from the gradle view in IntelliJ IDEA.

## Docker
To run a docker container with payments issue the following commands:
```bash
./gradlew build
docker build -t payments
docker run -e "SPRING_PROFILES_ACTIVE=production"
  -e "SPRING_MAIL_HOST=localhost" -e "SPRING_MAIL_PORT=1025"
  -e "PAYMENTS_MAIL_SENDER=${MAIL_SENDER}" -e "SPRING_DATASOURCE_URL=jdbc:hsqldb:mem:payments"
  -e "SPRING_DATASOURCE_USERNAME=sa" -e "SPRING_DATASOURCE_PASSWORD="
  -e "PAYMENTS_MOLLIEKEY=${FILL_MOLLIEKEY_HERE}"
  -e "PAYMENTS_ADMIN_GROUPS=chbeheer,bestuur,hoothub,${SOME_GROUP_YOU_ARE_IN}"
  -e "PAYMENTS_ADMIN_USERNAME=${SOME_USERNAME}" -e "PAYMENTS_ADMIN_PASSWORD=${SOME_PASSWORD}"
  -p 8080:8080 payments:latest
```

To run the container in a production environment, use the following configuration. If you are starting for the first time without an existing database present, please add `-e "SPRING_JPA_HIBERNATE_DDL_AUTO=create"` to generate the database.
```bash
docker run -e "SPRING_PROFILES_ACTIVE=production"
 -e "SPRING_MAIL_HOST=localhost" -e "SPRING_MAIL_PORT=1025"
 -e "PAYMENTS_MAIL_SENDER=${MAIL_SENDER}" -e "SPRING_DATASOURCE_URL=jdbc:postgresql://${DATABASE_LOCATION}"
 -e "SPRING_DATASOURCE_USERNAME=${DATABASE_USERNAME}" -e "SPRING_DATASOURCE_PASSWORD=${DATABASE_PASSWORD}"
 -e "PAYMENTS_MOLLIEKEY=${MOLLIE_KEY}" -e "PAYMENTS_ADMIN_GROUPS=chbeheer,bestuur,hoothub,${GROUP_YOU_ARE_IN}"
 -e "PAYMENTS_ADMIN_USERNAME=${ADMINISTRATOR_USERNAME}" -e "PAYMENTS_ADMIN_PASSWORD=${ADMINISTRATOR_PASSWORD}"
 -p 8080:8080 payments:latest
```
