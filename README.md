# CH-Payments
Welcome to CH Payments, a simple paymentprovider wrapper for stuff you want CH to get money for.
Payments makes it easy to create products, and implement it in your own website.
No hassle with keys, accounts and webhooks, just a single call to initiate a payment.

Below you can find some ready-to-use code examples for your website.

The administration panel (for now) is only available for board members.
They can create products to generate the keys required.

# API Endpoints

CH Payments has an API containing the following endpoints.

### `POST` Request payment `/api/v1/orders`

Requesting a payment url.

#### Parameters

|   |   |   |
|---|---|---|
| `name` <br> (required)  | string |   The name of the customer  |
| `email`<br> (required)  | string | The email of the customer for the order confirmation. |
| `productKeys` <br> (required) | array  | Array of CH Payments products keys of the products the customer wants to order | 
| `returnUrl`  | string | The URL the customer will be redirected to after the payment process. <br> Default: `https://ch.tudelft.nl/`  |
|  `method`<br> | string  |  Payment method selection. Possible values: `IDEAL`, `SOFORT`. <br>Default: `IDEAL` |
| `mailConfirmation` | boolean | Flag if CH Payments should send a Order confirmation email. <br>Default: `true` |
| `webhookURl`  | string | Set the webhook URL, where CH Payments will send order status updates to. See section **Webhook call**. |

#### Responses

`201 CREATED` - Order has been successfully created

```json
{
    "url": "<mollie-url>",
    "publicReference": "1073dc09-1b53-427f-ad3f-21b1057dd254",
    "status": "WAITING"
}
```

`500 INTERNAL_SERVER_ERROR` - Something went wrong

```json
{
    "timestamp": 1537533514873,
    "status": 500,
    "error": "Internal Server Error",
    "exception": "<exception>",
    "message": "<error-message>",
    "path": "/payments/api/orders"
}
```

### `GET` Get order status `/api/v1/orders/{public-reference}`

Get the status of an order.

#### Responses

`200 OK` - Successful

```json
{
    "url": "<mollie-url>",
    "publicReference": "1073dc09-1b53-427f-ad3f-21b1057dd254",
    "status": "WAITING"
}
```

`500` - Something went wrong

```json
{
    "timestamp": 1537533514873,
    "status": 500,
    "error": "Internal Server Error",
    "exception": "<exception>",
    "message": "<error-message>",
    "path": "/payments/api/orders"
}
```

### Webhook call

The call CH Payments makes about an order status update.

#### Body

```json
{
  "message": "OrderStatus update!",
  "publicReference": "1073dc09-1b53-427f-ad3f-21b1057dd254"
}
```

# Code examples
No idea how to code, but still want to sell tickets on your CH site?
Use one of these code examples to get going.

#### Python 3.5
```python
import requests

url = "http://localhost:9000/api/orders"

payload = {
        'name': 'Thomas Ticket',
        'email': 'thomasticket@example.com',
        'returnUrl': 'https://www.ch.tudelft.nl/payments/ordercompleted',
        'productKeys': ['e44685c5-a360-467c-960d-29843a101bb1', '879941f4-43d1-4ff2-ad66-cbdf3b141cab']
    }

response = requests.post(url, json=payload)

print(response.json())
```

#### JavaScript

The full workflow is as follows.

```js
async function main() {
  const productsRequest = await fetch('http://localhost:9000/api/products/SYMPOSIUM/2016');
  const products = await productsRequest.json();

  const orderRequest = await fetch('http://localhost:9000/api/orders', {
    method: 'POST',
    headers: {'Content-Type':'application/json'},
    body: JSON.stringify({
      name: 'Thomas Ticket',
      email: 'thomasticket@example.com',
      returnUrl: 'https://www.ch.tudelft.nl/payments/ordercompleted',
      productKeys: products.map(p => p.key)
    })
  });
  const order = await orderRequest.json();
  console.log(`You should redirect the user to url ${order.url}`);

  // After that you can obtain the order status with:
  const statusRequest = await fetch(`http://localhost:9000/api/orders/${order.publicReference}`);
  const status = await statusRequest.json();
  console.log(`The order status is right now ${status.status}`);
}

main();
```
(You can copy-paste this snippet into the Dev console of your browser to try it out. It requires Chrome 55+.

#### PHP (you filthy sjaarsCH)
```php
$url  = 'http://localhost:9000/api/orders';
$data = [
  'name'        => "Thomas Ticket",
  'email'       => "thomasticket@example.com",
  'returnUrl'   => "<return-url>",
  'productKeys' => [ "<product-key>", "<product-key>" ],
  'method'      => 'CREDIT_CARD'  // options: IDEAL, CREDIT_CARD, SOFORT, PAYPAL
];

$options = [
  'http' => [
    'header'  => [
      "Content-Type:application/json",
    ],
    'method'  => "POST",
    'content' => json_encode( $data ),
  ],
];

$response = json_decode( file_get_contents( $url, false, stream_context_create( $options ) ) );
```

# HootHub
This project currently is quite minimal, and offers lots of opportunities for great features built by you!
Check out the issue page for HootHub issues, and start earning Uilenballen.

First, pick an issue and self-assign it. Make your changes in a new branch, with the following naming convention:

- Fixing a bug? > "fix-description_of_bug"
- Implementing a new feature? > "feature-description_of_feature"

Once you're satisfied with your changes, create a pull request and give it the label "Ready for review".
You can assign someone in specific or wait for someone to pick it up.
Make sure to include tests and documentation.
If Travis isn't happy, we're not happy.

# Running
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
