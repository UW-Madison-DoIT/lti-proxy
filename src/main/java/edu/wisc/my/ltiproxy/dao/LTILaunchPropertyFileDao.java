package edu.wisc.my.ltiproxy.dao;

import java.io.IOException;
import java.util.Map;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.google.common.collect.Multimap;

public interface LTILaunchPropertyFileDao{
    
    public Map<String, String> getLaunchParameters(String key) throws JsonParseException, JsonMappingException, IOException;
    
    public Multimap<String, String> getHeadersToReplace(String key) throws JsonParseException, JsonMappingException, IOException;
    
    public String getProperty(String key, String propertyName);
    
}