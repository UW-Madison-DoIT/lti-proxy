package edu.wisc.my.ltiproxy.dao;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Repository;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import java.util.Arrays;

@Repository
public class LTILaunchPropertyFileDaoImpl implements LTILaunchPropertyFileDao{
    
    private Environment env;
    private ObjectMapper mapper = new ObjectMapper();

    /**
     * @param env the env to set
     */
    @Autowired
    void setEnv(Environment env) {
      this.env = env;
    }

    @Override
    public Map<String, String> getLaunchParameters(String key) throws JsonParseException, JsonMappingException, IOException {
        String loadedSignedParams = env.getProperty(key+".launchParams");
        HashMap<String, String> loadedSignedParamMap = mapper.readValue(loadedSignedParams, new TypeReference<Map<String, String>>(){});
        return loadedSignedParamMap;
    }

    @Override
    public Multimap<String, String> getHeadersToReplace(String launchKey) throws JsonParseException, JsonMappingException, IOException {
        String headerReplacementVariables = env.getProperty(launchKey+".headerReplacement");
        HashMap<String, String> rawHeaderReplacementVariablesMap = mapper.readValue(headerReplacementVariables, new TypeReference<Map<String, String>>(){});
        HashMultimap<String, String> headerReplacementVariableMap = HashMultimap.create();
        for( String key : rawHeaderReplacementVariablesMap.keySet()){
            String headersString = rawHeaderReplacementVariablesMap.get(key);
            String [] headers = headersString.split(",");
            headerReplacementVariableMap.putAll(key, Arrays.asList(headers));
          }
        return headerReplacementVariableMap;
    }

    @Override
    public String getProperty(String key, String propertyName) {
        return env.getProperty(key+"."+propertyName);
    }
    
}