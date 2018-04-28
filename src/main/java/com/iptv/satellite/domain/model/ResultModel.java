package com.iptv.satellite.domain.model;

/**
 * ResultModel
 */
public class ResultModel {

    private int code;
    private Object result;

    /**
     * @param code the code to set
     */
    public void setCode(int code) {
        this.code = code;
    }

    /**
     * @param result the result to set
     */
    public void setResult(Object result) {
        this.result = result;
    }

    /**
     * @return the code
     */
    public int getCode() {
        return code;
    }

    /**
     * @return the result
     */
    public Object getResult() {
        return result;
    }
}