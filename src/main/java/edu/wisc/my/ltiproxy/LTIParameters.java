package edu.wisc.my.ltiproxy;

import com.google.common.base.MoreObjects;
import java.util.Collections;
import java.util.Map;
import java.util.Objects;

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

    @Override
    public boolean equals(Object obj) {
        boolean result = (null != obj) && (obj instanceof LTIParameters);
        if (result) {
            LTIParameters rhs = (LTIParameters) obj;
            result = result
                    && Objects.equals(rhs.getActionURL(), this.getActionURL())
                    && Objects.equals(rhs.getSignedParameters(), this.getSignedParameters());
        }
        return result;
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.getActionURL(), this.getSignedParameters());
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("actionUrl", actionURL)
                .add("signedParameters", signedParameters)
                .toString();
    }
}
