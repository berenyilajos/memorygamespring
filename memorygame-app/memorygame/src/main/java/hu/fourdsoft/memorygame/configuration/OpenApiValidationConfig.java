package hu.fourdsoft.memorygame.configuration;

import com.atlassian.oai.validator.OpenApiInteractionValidator;
import com.atlassian.oai.validator.springmvc.OpenApiValidationFilter;
import com.atlassian.oai.validator.springmvc.OpenApiValidationInterceptor;
import hu.fourdsoft.memorygame.jwt.JwtFilterHandlerInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.servlet.Filter;
import java.io.IOException;

/**
 * Created by jt on 2018-12-10.
 */
@Configuration
public class OpenApiValidationConfig implements WebMvcConfigurer {

    private final OpenApiValidationInterceptor validationInterceptor;

    @Autowired
    private JwtFilterHandlerInterceptor jwtFilterHandlerInterceptor;

    @Autowired
    public OpenApiValidationConfig() throws IOException {

        this.validationInterceptor = new OpenApiValidationInterceptor(OpenApiInteractionValidator
                .createFor("/api/memorygame-api.yaml").build());
    }

    @Bean
    public Filter validationFilter() {
        return new OpenApiValidationFilter(
                true, // enable request validation
                true  // enable response validation
        );
    }

    @Override
    public void addInterceptors(final InterceptorRegistry registry) {
        registry.addInterceptor(validationInterceptor);
        registry.addInterceptor(jwtFilterHandlerInterceptor);
    }
}