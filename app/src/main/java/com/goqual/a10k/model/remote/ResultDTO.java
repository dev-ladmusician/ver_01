package com.goqual.a10k.model.remote;

import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * Created by ladmusician on 2016. 12. 9..
 */

public class ResultDTO<T> {
    private String error;
    private T result;

    public String getError() {
        return error;
    }
    public void setError(String error) {
        this.error = error;
    }
    public T getResult() {
        return result;
    }
    public void setResult(T result) {
        this.result = result;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }
}
