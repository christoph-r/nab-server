package at.vienna.glg;

import java.util.List;

import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.ByteArrayHttpMessageConverter;
import org.springframework.http.converter.FormHttpMessageConverter;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.ResourceHttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.ContentNegotiationConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;

@Configuration
class WebConfig extends WebMvcConfigurationSupport {

	@Override
	public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
		converters.add(new StringHttpMessageConverter());
		converters.add(new FormHttpMessageConverter());
		converters.add(new ByteArrayHttpMessageConverter());
		converters.add(new ResourceHttpMessageConverter());
		converters.add(new MappingJackson2HttpMessageConverter());
	}

	@Override
	protected void configureContentNegotiation(final ContentNegotiationConfigurer configurer) {
		// Turn off suffix-based content negotiation
		configurer.favorPathExtension(false);
	}
}