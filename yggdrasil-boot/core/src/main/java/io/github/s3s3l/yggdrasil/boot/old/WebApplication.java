package io.github.s3s3l.yggdrasil.boot.old;

import javax.servlet.Filter;
import javax.servlet.http.HttpServlet;

public interface WebApplication extends Application {

	void addServlet(HttpServlet servlet, String urlMapping);

	void addServlet(String name, HttpServlet servlet, String urlMapping);

	void addFilter(Filter filter, String pattern);

	void addFilter(String name, Filter filter, String pattern);
}
