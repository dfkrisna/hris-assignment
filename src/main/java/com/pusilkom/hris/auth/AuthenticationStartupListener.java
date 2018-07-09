package com.pusilkom.hris.auth;

import com.pusilkom.hris.model.AuthItem;
import com.pusilkom.hris.service.AuthItemService;
import com.pusilkom.hris.model.AuthItem;
import com.pusilkom.hris.service.AuthItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

@Component
public class AuthenticationStartupListener implements ApplicationListener {
    @Autowired
    AuthItemService authItemService;

    @Override
    public void onApplicationEvent(ApplicationEvent event) {
        if (event instanceof ContextRefreshedEvent) {
            //clear auth item
            //authItemService.clearItem();
            ApplicationContext applicationContext = ((ContextRefreshedEvent) event).getApplicationContext();
            applicationContext.getBean(RequestMappingHandlerMapping.class).getHandlerMethods().forEach(
                (k,v)->{
                    String authItemName = createAuthItem(k);
                    AuthItem item = authItemService.getAuthItemByName(authItemName);
                    if(item == null)
                        item = new AuthItem();
                    item.setName(authItemName);
                    authItemService.createItem(item);
                }
            );
        }
    }

    private String createAuthItem(RequestMappingInfo k) {
        String method = k.getMethodsCondition().toString();
        String uri = method + k.getPatternsCondition().toString();
        uri = uri.replace("[","");
        uri = uri.replace("{","");
        uri = uri.replace("]","");
        uri = uri.replace("}","");
        uri = uri.replace("/", "_");
        uri = uri.replace("-", "_");
        return uri.toUpperCase();
    }

}
