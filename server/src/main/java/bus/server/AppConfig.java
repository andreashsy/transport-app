package bus.server;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.client.builder.AwsClientBuilder.EndpointConfiguration;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import bus.server.services.JwtFilter;
import static bus.server.Constants.*;

@Configuration
public class AppConfig {
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

	/* @Bean
    AmazonS3 createAmazonS3() {
        final String accessKey = KEY_DO_ACCESS;
        final String secretKey = KEY_DO_PRIVATE;

        final BasicAWSCredentials basicCred = new BasicAWSCredentials(accessKey, secretKey);
        final AWSStaticCredentialsProvider cred = new AWSStaticCredentialsProvider(basicCred);
        final EndpointConfiguration config = new EndpointConfiguration(endpoint, region);

        return AmazonS3ClientBuilder
            .standard()
            .withCredentials(cred)
            .withEndpointConfiguration(config)
            .build();
    } */
    
}
