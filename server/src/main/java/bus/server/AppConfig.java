package bus.server;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import bus.server.services.JwtFilter;

@Configuration
public class AppConfig implements WebMvcConfigurer{
	@Value("${spaces.endpoint}")
    private String endpoint;

    @Value("${spaces.region}")
    private String region;

    @Autowired
	private JwtFilter myfilter;

	@Bean
	public FilterRegistrationBean<JwtFilter> registerJwtFilter(JwtFilter filter) {

		FilterRegistrationBean<JwtFilter> regFilterBean = new FilterRegistrationBean<>();
		regFilterBean.setFilter(filter);
		regFilterBean.addUrlPatterns("/secure/*");
		regFilterBean.setEnabled(true);

		return regFilterBean;
	}

    @Override
    public void addCorsMappings(CorsRegistry cors) {
        cors.addMapping("/api/**").allowedMethods("GET", "PUT", "POST", "PATCH", "DELETE");
    }

}
