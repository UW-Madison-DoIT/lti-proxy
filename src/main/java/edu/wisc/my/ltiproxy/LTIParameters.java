/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.wisc.my.ltiproxy;

import java.util.Collections;
import java.util.Map;

/**
 *
 * @author sibley
 */
public class LTIParameters {
    private final String actionURL;
    private final Map<String, String> signedParameters;

    public LTIParameters(String actionURL, Map<String, String> signedParameters) {
        this.actionURL = actionURL;
        this.signedParameters = Collections.unmodifiableMap(signedParameters);
    }

    public String getActionURL() {
        return actionURL;
    }

    public Map<String, String> getSignedParameters() {
        return signedParameters;
    }
}
