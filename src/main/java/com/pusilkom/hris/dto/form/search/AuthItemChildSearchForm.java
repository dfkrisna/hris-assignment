package com.pusilkom.hris.dto.form.search;

public class AuthItemChildSearchForm extends BaseTableSearchForm {

    private String parent;
    private String child;

    public String getParent() {
        return parent;
    }

    public void setParent(String parent) {
        this.parent = parent;
    }

    public String getChild() {
        return child;
    }

    public void setChild(String child) {
        this.child = child;
    }
}
