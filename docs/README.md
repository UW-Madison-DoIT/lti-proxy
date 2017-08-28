# LTI Proxy

This service will return a JSON feed to be used in an HTML form.  The parameters
configured will be used in a LTI signature to provide authentication.
[IMS Global](https://www.imsglobal.org/) has
[documentation](https://www.imsglobal.org/activity/learning-tools-interoperability)
on LTI.

## To Build
This project uses maven.  `mvn package` will build a warfile for deployment.

## To Configure
Create an `application.properties` file under `src/main/resources`.
You may wish to copy the existing `application.properties.example` file.

The application supports a [property file for configuration](launchParams.md).  Replace `app` with
a unique identifier in your deployment.  The key you choose will be the endpoint
that your configuration is accessible through.  For example, the example
properties file allows /lti-proxy/lti-launch/app to return the JSON feed needed.
`key`, `secret`, `actionURL`, `launchParams`, and `headerReplacement`
are necessary.  `headerReplacement` can be blank.  `headerReplacement`
is both additive and can replace any parameters already specified in `launchParams`
useful if you would like default values (specified in launch params).

## Usage
### /lti-proxy/lti-launch/`app`
The basic lti-launch path will read in and replace headers according to the
configuration provided in your `application.properties` file and return
a JSON object that you may use to populate your own request to the LTI consumer.
This is useful for debugging the information sent to the consumer, and sending
the request from the client-side.

### /lti-proxy/lti-launch/go/`app`
The `go` lti-launch path will read and replace headers the same as the basic path
then send a configured request to the LTI consumer. On success, the proxy will
relay the authenticated redirect location back to the client.
