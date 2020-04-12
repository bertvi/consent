package com.gofore.consent.service_declaration;

import org.apache.catalina.Context;
import org.apache.catalina.connector.Connector;
import org.apache.tomcat.util.descriptor.web.SecurityCollection;
import org.apache.tomcat.util.descriptor.web.SecurityConstraint;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.boot.web.servlet.server.ServletWebServerFactory;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class Application {

	private static final Logger log = LoggerFactory.getLogger(Application.class);

	@Value("${local.server.port}")
	private static int localPort;

	@Value("${server.port}")
	private static int redirectedPort;


	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
		log.info("Service Declaration API started on port {}}", localPort);
	}

	@Bean
	public ServletWebServerFactory servletContainer() {
		TomcatServletWebServerFactory tomcat = new TomcatServletWebServerFactory() {@Override
		protected void postProcessContext(Context context) {
			SecurityConstraint securityConstraint = new SecurityConstraint();
			securityConstraint.setUserConstraint("CONFIDENTIAL");
			SecurityCollection collection = new SecurityCollection();
			collection.addPattern("/*");
			securityConstraint.addCollection(collection);
			context.addConstraint(securityConstraint);
		}
		};
		tomcat.addAdditionalTomcatConnectors(redirectConnector());

		log.info("Service Declaration API redirected to port {}}", redirectedPort);

		return tomcat;
	}

	private Connector redirectConnector() {
		Connector connector = new Connector("org.apache.coyote.http11.Http11NioProtocol");
		connector.setScheme("http");
		connector.setPort(localPort);
		connector.setSecure(false);
		connector.setRedirectPort(redirectedPort);

		return connector;
	}

}
