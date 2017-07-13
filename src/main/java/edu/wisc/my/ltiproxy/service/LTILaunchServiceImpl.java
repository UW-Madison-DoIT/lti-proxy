package edu.wisc.my.ltiproxy.service;

import edu.wisc.my.ltiproxy.dao.LTILaunchPropertyFileDao;

import java.io.IOException;
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
import com.google.common.collect.Multimap;
import edu.wisc.my.ltiproxy.LTIParameters;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.HttpHeaders;
import org.apache.http.HttpStatus;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class LTILaunchServiceImpl implements LTILaunchService {

    protected final Logger logger = LoggerFactory.getLogger(getClass());
    private LTILaunchPropertyFileDao LTILaunchPropertyFileDao;

    @Autowired
    void setLTILaunchProperityFileDao(LTILaunchPropertyFileDao LTILaunchPropertyFileDao) {
        this.LTILaunchPropertyFileDao = LTILaunchPropertyFileDao;
    }

    @Override
    public URI getRedirectUri(String key, Map<String, String> headers) throws
            JsonParseException, JsonMappingException, IOException, LtiSigningException, URISyntaxException {
        URI result = null;

        Map<String, String> prepParams = prepareParameters(key, headers);
        LTIParameters ltiParams = signParameters(key, prepParams);

        String actionUrl = ltiParams.getActionURL();
        List<NameValuePair> formBody = buildFormBody(ltiParams.getSignedParameters());

        CloseableHttpClient httpClient = null;
        CloseableHttpResponse httpResp = null;

        try {
            httpClient = HttpClientBuilder.create().disableRedirectHandling().build();
            HttpPost httpPost = new HttpPost(actionUrl);
            UrlEncodedFormEntity ent = new UrlEncodedFormEntity(formBody);
            httpPost.setEntity(ent);

            httpResp = httpClient.execute(httpPost);

            int status = httpResp.getStatusLine().getStatusCode();
            logger.debug("status " + status);
            for (Header header : httpResp.getAllHeaders()) {
                logger.trace(header.getName() + " : " + header.getValue());
            }

            HttpEntity respEntity = httpResp.getEntity();
            byte[] b = EntityUtils.toByteArray(respEntity);
            EntityUtils.consumeQuietly(respEntity);

            logger.trace(new String(b));

            if (HttpStatus.SC_SEE_OTHER == status) {
                String loc = httpResp.getLastHeader(HttpHeaders.LOCATION).getValue();
                logger.trace(loc);
                result = new URI(loc);
            }

        } finally {
            if (null != httpClient) {
                httpClient.close();
            }
            if (null != httpResp) {
                httpResp.close();
            }
        }

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
        for (Entry<String, String> entry : ltiParams.getSignedParameters().entrySet()) {
            JSONObject entryObject = new JSONObject();
            entryObject.put("name", entry.getKey());
            entryObject.put("value", entry.getValue());
            formInputs.put(entryObject);
        }

        jsonToReturn.put("formInputs", formInputs);
        return jsonToReturn;
    }

    protected Map<String, String> prepareParameters(String key, Map<String, String> requestHeaders) throws
            JsonParseException, JsonMappingException, IOException, LtiSigningException, JSONException {
        Map<String, String> launchParameters = LTILaunchPropertyFileDao.getLaunchParameters(key);

        Map<String, String> paramsToSign = new TreeMap<>();
        paramsToSign.putAll(launchParameters);
        paramsToSign.putAll(replaceHeaders(key, requestHeaders));

        return paramsToSign;
    }

    protected LTIParameters signParameters(String key, Map<String, String> paramsToSign) throws LtiSigningException {
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

    protected Map<String, String> replaceHeaders(String key, Map<String, String> requestHeaders) throws JsonParseException, JsonMappingException, IOException {
        Map<String, String> result = new LinkedHashMap<>();
        Multimap<String, String> paramToHeaders = LTILaunchPropertyFileDao.getHeadersToReplace(key);
        for (String param : paramToHeaders.keySet()) {
            Iterable<String> headers = paramToHeaders.get(param);
            for (String headerKey : headers) {
                String headerValue = requestHeaders.get(headerKey.toLowerCase());
                if (StringUtils.isNotBlank(headerValue)) {
                    result.put(param, headerValue);
                    break;
                }
            }
        }
        return result;
    }

    protected List<NameValuePair> buildFormBody(Map<String, String> parameters) throws UnsupportedEncodingException {
        List<NameValuePair> result = new ArrayList<>();

        for (Entry<String, String> entry : parameters.entrySet()) {
            result.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
        }

        return result;
    }
}
