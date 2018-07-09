package com.pusilkom.hris.util;

import com.pusilkom.hris.dto.table.BreadcrumbItem;
import com.pusilkom.hris.dto.table.BreadcrumbItem;

import java.util.ArrayList;
import java.util.List;

public class BreadcrumbUtil {

    List<BreadcrumbItem> breadcrumbItems;

    public BreadcrumbUtil() {
        breadcrumbItems = new ArrayList<>();
    }
    public void addItem(String label, String url) {
        BreadcrumbItem item = new BreadcrumbItem(label, url);
        this.breadcrumbItems.add(item);
    }

    public List<BreadcrumbItem> getBreadcrumbItems() {
        return breadcrumbItems;
    }
}
