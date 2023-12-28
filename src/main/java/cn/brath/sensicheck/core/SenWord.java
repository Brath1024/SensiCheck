package cn.brath.sensicheck.core;

import java.io.Serializable;

public class SenWord implements Serializable {
    private static final long serialVersionUID = -7918430275428907853L;
    private final String fragment;
    private final SenKey senKey;

    public SenWord(String fragment, SenKey senKey) {
        this.fragment = fragment;
        this.senKey = senKey;
    }

    public String getFragment() {
        return fragment;
    }

    public SenKey getSenKey() {
        return senKey;
    }

    @Override
    public String toString() {
        if (senKey == null) {
            return fragment;
        } else {
            return fragment + "(" + senKey + ")";
        }
    }
}