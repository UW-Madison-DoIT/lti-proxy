package edu.wisc.my.ltiproxy.service;

import java.io.IOException;

import org.imsglobal.lti.launch.LtiSigningException;
import org.json.JSONException;
import org.json.JSONObject;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import java.net.URI;
import java.util.Map;

public interface LTILaunchService {
    public URI getRedirectUri(String launchKey, Map<String, String> headers) throws JsonParseException, JsonMappingException, IOException, LtiSigningException, JSONException;
    public JSONObject getFormData(String launchKey, Map<String, String> headers) throws JsonParseException, JsonMappingException, IOException, LtiSigningException, JSONException;
}
