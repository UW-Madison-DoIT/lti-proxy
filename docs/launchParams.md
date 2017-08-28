# Application Properties
Here's an example `application.properties` for reference when satisfying LTI v1.0. We don't claim this to be complete nor correct, but it can be handy when stubbing out a new endpoint.

```properties
endpoints.enabled=false
endpoints.health.enabled=true
server.port = 8090
logging.level.org.springframework.web=DEBUG
example.launchParams=\
{"context_id" : ""\
,"context_label" : "SUPPORT"\
,"context_title" : "Application Support"\
,"launch_presentation_document_target" : "frame"\
,"launch_presentation_locale" : "EN-US__"\
,"lis_outcome_service_url" : "https://www.example.com/d2l/le/lti/Outcome"\
,"lis_person_contact_email_primary" : ""\
,"lis_person_name_family" : ""\
,"lis_person_name_full" : ""\
,"lis_person_name_given" : ""\
,"lti_message_type" : "basic-lti-launch-request"\
,"lti_version" : "LTI-1p0"\
,"resource_link_description" : "Application LTI Launcher"\
,"resource_link_title" : "LTI Launcher"\
,"roles" : "urn:lti:instrole:ims/lis/Staff,Staff"\
,"tool_consumer_info_product_family_code" : "desire2learn"\
,"tool_consumer_info_version" : "10.6.0"\
,"user_id" : ""\
}
example.key=username
example.secret=usersecret
example.actionURL=https://www.example.com/lti
example.headerReplacement = \
{"lis_person_contact_email_primary" : "mail"\
,"lis_person_name_family" : "tryFirstHeaderSurname,fallthroughHeaderLastName"\
,"lis_person_name_full" : "displayName"\
,"lis_person_name_given" : "givenName"\
,"user_id" : "username"\
,"context_id" : "username"\
}
```
