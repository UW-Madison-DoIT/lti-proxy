package edu.wisc.my.ltiproxy.service;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;

import org.imsglobal.lti.launch.LtiSigningException;
import org.json.JSONException;
import org.json.JSONObject;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;

public interface LTILaunchService {
    public JSONObject getFormData(String launchKey, HttpServletRequest request) throws JsonParseException, JsonMappingException, IOException, LtiSigningException, JSONException;
}
