package com.epam.esm.config;

import org.springframework.web.servlet.support.AbstractAnnotationConfigDispatcherServletInitializer;

import javax.servlet.ServletRegistration;
import java.util.ResourceBundle;

/**
 * @author Aliaksei Halkin
 */
public class DispatcherInitializer extends AbstractAnnotationConfigDispatcherServletInitializer {
    private static final String APPLICATION_FILE_NAME = "application";
    private static final String SPRING_PROFILES_ACTIVE = "spring.profiles.active";
    private static final String SPRING_NO_HANDLER = "throwExceptionIfNoHandlerFound";
    private static final String SPRING_METHOD_NOT_ALLOWED = "throwExceptionIfMethodNotAllowed";

    @Override
    protected Class<?>[] getRootConfigClasses() {
        return new Class[]{};
    }

    @Override
    protected Class<?>[] getServletConfigClasses() {
        return new Class[]{WebConfig.class};
    }

    @Override
    protected String[] getServletMappings() {
        return new String[]{"/"};
    }
    @Override
    protected void customizeRegistration(ServletRegistration.Dynamic registration) {
        ResourceBundle bundle = ResourceBundle.getBundle(APPLICATION_FILE_NAME);
        registration.setInitParameter(SPRING_PROFILES_ACTIVE, bundle.getString(SPRING_PROFILES_ACTIVE));
        registration.setInitParameter(SPRING_NO_HANDLER, bundle.getString(SPRING_NO_HANDLER));
        registration.setInitParameter(SPRING_METHOD_NOT_ALLOWED, bundle.getString(SPRING_METHOD_NOT_ALLOWED));
    }
}
