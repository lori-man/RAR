package com.mro.RAR.model;


public class QueryObjectRequest extends WebServiceRequest {

    private int page;
    private int size;


    public QueryObjectRequest() {
    }


    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

}
