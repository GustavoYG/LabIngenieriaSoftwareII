package guru.springframework.configuration;

import org.h2.server.web.WebServlet;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class WebConfiguration {
    @Bean
    public ServletRegistrationBean<WebServlet> h2servletRegistration() {
        ServletRegistrationBean<WebServlet> registrationBean =
                new ServletRegistrationBean<>(new WebServlet(), "/console/*");
        registrationBean.setName("H2Console");
        registrationBean.setLoadOnStartup(1);
        return registrationBean;
    }
}
