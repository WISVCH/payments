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
### Tools
-   [PostgresQL](https://www.postgresql.org/)
-   [mailcatcher](https://mailcatcher.me/), mailcatcher creates a mailserver locally on your pc. All mail sent from the API is cought here, you end up with a mailbox with every outgoing mailaddress. Unix-like systems: `gem install mailcatcher`. Windows users can try mailcatcher as well, but [Papercut](https://github.com/changemakerstudios/papercut) has an easier installation. 

### Usage
1.  Import the project into IntelliJ IDEA, we really recommend using [IntelliJ IDEA Ultimate Edition](https://www.jetbrains.com/idea/), since it includes all the support for Spring. You could use another IDE, but we do not recommend this
2.  Make sure you have installed the `Lombok Plugin`
3.  Enable annotation processing, this can be enabled in `Settings > Build, Execution, Deployment > Compiler > Annotation Processors`. Here you have to check the checkmark that says `Enable Annotation Processors`
4.  Copy `config/application.properties.sample` to `config/application.properties`. The sample properties assume a working PostgreSQL installation running in the background.

  You should change:
  - `spring.datasource.[…]` (`url`, `username`, `password`) to your database url and credentials
  - `payments.molliekey` and `payments.returnUrl` to their respective keys if you have those

5.  Right click the `Application` class (`src -> main -> java -> ch.wisv.payments`) and choose `Run`. Terminate the process (you don't have to wait for it to finish starting). Now go to the Run/Debug Configuration window `Run -> Edit Configurations` choose the `Spring Boot` configuration called `Application`. Enable the dev profile for this configuration by entering `dev` in the `Active Profiles` box.

### Build
To generate a runnable JAR file, make sure that you have followed all the instructions under the **run** section. When you have done so, run `./gradlew build`. This command will run all tests, and create a runnable JAR file in the `./build` folder. You could also run `Build` from the gradle view in IntelliJ IDEA.
