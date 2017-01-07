package edu.wisc.my.ltiproxy.service;

import edu.wisc.my.ltiproxy.dao.LTILaunchPropertyFileDao;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;
import java.util.Map.Entry;

import javax.servlet.http.HttpServletRequest;

import org.imsglobal.lti.launch.LtiOauthSigner;
import org.imsglobal.lti.launch.LtiSigner;
import org.imsglobal.lti.launch.LtiSigningException;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;

@Service
public class LTILaunchServiceImpl implements LTILaunchService{
    
    private LTILaunchPropertyFileDao LTILaunchPropertyFileDao;
    
    @Autowired
    void setLTILaunchProperityFileDao(LTILaunchPropertyFileDao LTILaunchPropertyFileDao){
        this.LTILaunchPropertyFileDao = LTILaunchPropertyFileDao;
    }

    @Override
    public JSONObject getFormData(String key, HttpServletRequest request) throws
        JsonParseException, JsonMappingException, IOException, LtiSigningException, JSONException {
        
        Map<String, String> launchParameters = LTILaunchPropertyFileDao.getLaunchParameters(key);
        
        TreeMap<String, String> paramsToSign = new TreeMap<String, String>();
        paramsToSign.putAll(launchParameters);
        paramsToSign.putAll(getHeaders(key, request));
        
        LtiSigner ltiSigner = new LtiOauthSigner();
        String ltiKey = LTILaunchPropertyFileDao.getProperty(key, "key");
        String ltiSecret = LTILaunchPropertyFileDao.getProperty(key, "secret");
        String ltiActionUrl = LTILaunchPropertyFileDao.getProperty(key, "actionURL");
        Map<String, String> signedParameters = ltiSigner.signParameters(
                paramsToSign, ltiKey, ltiSecret, ltiActionUrl, "POST");

        JSONObject jsonToReturn = new JSONObject();
        jsonToReturn.put("action", ltiActionUrl);
        JSONArray formInputs = new JSONArray();
        for(Entry<String, String> entry : signedParameters.entrySet()){
            JSONObject entryObject = new JSONObject();
            entryObject.put("name", entry.getKey());
            entryObject.put("value", entry.getValue());
            formInputs.put(entryObject);
        }
        
        jsonToReturn.put("formInputs", formInputs);
        return jsonToReturn;
    }
    
    private Map<String, String> getHeaders (String key, HttpServletRequest request) throws JsonParseException, JsonMappingException, IOException{
        Map<String, String> headers = new HashMap<String, String>();
        Map<String, String[]> headersToReplace = LTILaunchPropertyFileDao.getHeadersToReplace(key);
        for( String headerToReplace : headersToReplace.keySet()){
          String[] headerAttributes = headersToReplace.get(headerToReplace);
          for(String header : headerAttributes){
            if(request.getHeader(header)!=null){
                headers.put(headerToReplace, request.getHeader(header));
              break;
            }
          }
        }
        return headers;
    }
    
}