package com.michael.core.spring;

import javax.servlet.ServletContext;

/**
 * Spring unload
 *
 * @author Michael
 */
public interface SpringUnloadListener {

    void execute(ServletContext servletContext);

}
