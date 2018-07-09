package com.pusilkom.hris.dto.table;

public class BreadcrumbItem {
    private String url;
    private String label;

    public BreadcrumbItem(String label, String url) {
        this.label = label;
        this.url = url;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }
}
