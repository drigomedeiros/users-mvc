package com.fromscratch.users.infrastructure.jakartaee;

import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.server.mvc.mustache.MustacheMvcFeature;


public class JakartaApp extends ResourceConfig {

    public JakartaApp() {
        super();
        packages(true, "com.fromscratch.users");
        property(MustacheMvcFeature.TEMPLATE_BASE_PATH, "templates");
        register(MustacheMvcFeature.class);
    }

}
