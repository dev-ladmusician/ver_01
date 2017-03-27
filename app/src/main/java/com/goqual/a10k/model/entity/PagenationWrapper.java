package com.goqual.a10k.model.entity;

import org.apache.commons.lang3.builder.ToStringBuilder;

import java.util.List;

/**
 * Created by hanwool on 2017. 3. 17..
 */

public class PagenationWrapper<T> {
    private int page;
    private int lastPage;
    private List<T> result;

    public PagenationWrapper() {
    }

    public PagenationWrapper(int page, int lastPage, List<T> result) {
        this.page = page;
        this.lastPage = lastPage;
        this.result = result;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getLastPage() {
        return lastPage;
    }

    public void setLastPage(int lastPage) {
        this.lastPage = lastPage;
    }

    public List<T> getResult() {
        return result;
    }

    public void setResult(List<T> result) {
        this.result = result;
    }

    public boolean isEmpty() {
        return result == null || result.isEmpty();
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }
}
