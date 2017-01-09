### LTI Proxy

This service will return a JSON feed to be used in a HTML form.  The parameters
configured will be used in an LTI signature to provide authentication.  
[IMS Global](https://www.imsglobal.org/) has [documentation]
(https://www.imsglobal.org/activity/learning-tools-interoperability) on LTI.

## To Build
This project uses maven.  `mvn package` will build a warfile for deployment.

## To Configure
Create a `application.properties` file under `src/main/resources`. 
You may wish to copy the existing `application.properties.example` file.

The application supports a property file for configuration.  Replace `app` with
a unique identifier in your deployment.  The key you choose will be the endpoint
that your configuration is accessible through.  For example, the example
properties file allows /lti-proxy/lti-launch/app to return the JSON feed needed.
`key`, `secret`, `actionURL`, `launchParams`, and `headerReplacement`
are necessary.  `headerReplacement` can be blank.  `headerReplacement`
is both additive and can replace any parameters already specified in `launchParams`
useful if you would like default values (specified in launch params).

