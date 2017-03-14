package edu.wisc.my.ltiproxy.service;

import edu.wisc.my.ltiproxy.dao.LTILaunchPropertyFileDao;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;
import java.util.Map.Entry;

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
import edu.wisc.my.ltiproxy.LTIParameters;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

@Service
public class LTILaunchServiceImpl implements LTILaunchService{
    
    private RestTemplate rest = new RestTemplate();
    private LTILaunchPropertyFileDao LTILaunchPropertyFileDao;
    
    private static final String UTF8 = StandardCharsets.UTF_8.name();
    
    @Autowired
    void setLTILaunchProperityFileDao(LTILaunchPropertyFileDao LTILaunchPropertyFileDao){
        this.LTILaunchPropertyFileDao = LTILaunchPropertyFileDao;
    }

    @Override
    public URI getRedirectUri (String key, Map<String, String> headers) throws
        JsonParseException, JsonMappingException, IOException, LtiSigningException, JSONException {
        URI result;
        
        Map<String, String> prepParams = prepareParameters(key, headers);
        LTIParameters ltiParams = signParameters(key, prepParams);
        
        String actionUrl = ltiParams.getActionURL();
        String formBody = buildFormBody(ltiParams.getSignedParameters());
        HttpHeaders ltiHeaders = new HttpHeaders();
        ltiHeaders.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        HttpEntity<String> ltiReq = new HttpEntity<>(formBody);
        
        ResponseEntity<String> ltiResp = rest.exchange(actionUrl, HttpMethod.POST, ltiReq, String.class);
        result = ltiResp.getHeaders().getLocation();
        
        return result;
    }
    
    @Override
    public JSONObject getFormData(String key, Map<String, String> headers) throws
        JsonParseException, JsonMappingException, IOException, LtiSigningException, JSONException {
        
        Map<String, String> prepParams = prepareParameters(key, headers);
        LTIParameters ltiParams = signParameters(key, prepParams);

        JSONObject jsonToReturn = new JSONObject();
        jsonToReturn.put("action", ltiParams.getActionURL());
        JSONArray formInputs = new JSONArray();
        for(Entry<String, String> entry : ltiParams.getSignedParameters().entrySet()){
            JSONObject entryObject = new JSONObject();
            entryObject.put("name", entry.getKey());
            entryObject.put("value", entry.getValue());
            formInputs.put(entryObject);
        }
        
        jsonToReturn.put("formInputs", formInputs);
        return jsonToReturn;
    }
    
    private Map<String, String> prepareParameters(String key, Map<String, String> requestHeaders) throws 
            JsonParseException, JsonMappingException, IOException, LtiSigningException, JSONException {
        Map<String, String> launchParameters = LTILaunchPropertyFileDao.getLaunchParameters(key);
        
        Map<String, String> paramsToSign = new TreeMap<>();
        paramsToSign.putAll(launchParameters);
        paramsToSign.putAll(replaceHeaders(key, requestHeaders));
        
        return paramsToSign;
    }
    
    private LTIParameters signParameters(String key, Map<String, String> paramsToSign) throws LtiSigningException {
        LTIParameters result;
        
        LtiSigner ltiSigner = new LtiOauthSigner();
        String ltiKey = LTILaunchPropertyFileDao.getProperty(key, "key");
        String ltiSecret = LTILaunchPropertyFileDao.getProperty(key, "secret");
        String ltiActionUrl = LTILaunchPropertyFileDao.getProperty(key, "actionURL");
        Map<String, String> signedParameters = ltiSigner.signParameters(
                paramsToSign, ltiKey, ltiSecret, ltiActionUrl, "POST");
        result = new LTIParameters(ltiActionUrl, signedParameters);
        
        return result;
    }
    
    private Map<String, String> replaceHeaders (String key, Map<String, String> requestHeaders) throws JsonParseException, JsonMappingException, IOException{
        Map<String, String> headers = new HashMap<>();
        Map<String, String[]> headersToReplace = LTILaunchPropertyFileDao.getHeadersToReplace(key);
        for( String headerToReplace : headersToReplace.keySet()){
          String[] headerAttributes = headersToReplace.get(headerToReplace);
          for(String header : headerAttributes){
            if(requestHeaders.get(header)!=null){
                headers.put(headerToReplace, requestHeaders.get(header));
              break;
            }
          }
        }
        return headers;
    }
    
    private String buildFormBody(Map<String, String> parameters) throws UnsupportedEncodingException {
        StringBuilder result = new StringBuilder();
        
        for (Entry<String, String> param : parameters.entrySet()) {
            result.append("&")
                    .append(param.getKey()) //TODO urlencode?
                    .append("=")
                    .append(param.getValue());
        }
        result.deleteCharAt(0);
        
        return result.toString();
    }
}