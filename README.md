# RestAPI_Project

Explain how to run the tests:

1.Create a resources directory in the test package.

2.Make sure the directory is defined resources directory. 
file -> Project Structure -> Modules -> click on the directory src\test\resources and mark the directory as "Test Recources" then click "ok".

2.Create a "config.properties" file into recources directory.(This file should be in gitigonre)

3.Set two properties like this:

webhook_message_api=*INSERT_VALUE_1*
webhook_message_api_2=*INSERT_VALUE_2*
where INSERT_VALUE_1 is the webhook of "api-messages" channel that you can found it from our app slack,
and INSERT_VALUE_2 is the webhook of "api-messages2" channel.
for example your "config.properties" should look like this:
webhook_message_api=https://hooks.slack.com/services/XXXXXXXXXX/XXXXXXXXXXX/XXXXXXXXXXXXXXXXXXXX
webhook_message_api_2=https://hooks.slack.com/services/YYYYYYYYYY/YYYYYYYYYYY/YYYYYYYYYYYYYYYYYYYY

  you can get the webhook from this link:
  https://api.slack.com/apps
  a.get in to our App
  b.in the left side, under "Features" you can found "Incoming Webhooks" scroll down and you can copy the Webhook URL.
  
4.Thats it, now you can run EndToEndTest, IntegrationTest and SendMessageIntegrationTest.

