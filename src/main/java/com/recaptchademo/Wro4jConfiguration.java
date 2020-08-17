package com.recaptchademo;

import com.google.common.collect.ImmutableList;
import lombok.NonNull;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import ro.isdc.wro.config.jmx.ConfigConstants;
import ro.isdc.wro.http.ConfigurableWroFilter;
import ro.isdc.wro.model.resource.processor.factory.ConfigurableProcessorsFactory;

import javax.servlet.Filter;
import java.util.List;
import java.util.Properties;
import java.util.stream.Stream;

@Configuration
public class Wro4jConfiguration {
    private static final List<String> OTHER_WRO_PROPS = ImmutableList.of(
            ConfigurableProcessorsFactory.PARAM_POST_PROCESSORS,
            ConfigurableProcessorsFactory.PARAM_PRE_PROCESSORS
    );

    @Bean
    public FilterRegistrationBean webResourceOptimizer(Environment environment) {
        FilterRegistrationBean registrationBean = new FilterRegistrationBean();
        registrationBean.setFilter(newWroFilter(environment));
        registrationBean.addUrlPatterns("/wro/*");
        registrationBean.setName("WebResourceOptimizer");
        registrationBean.setOrder(1);
        return registrationBean;
    }

    private Filter newWroFilter(@NonNull Environment environment) {
        ConfigurableWroFilter filter = new ConfigurableWroFilter();
        Properties props = buildWroProps(environment);
        filter.setProperties(props);
        filter.setWroManagerFactory(new Wro4jCustomXmlModelManagerFactory(props));
        return filter;
    }

    private Properties buildWroProps(@NonNull Environment environment) {
        Properties props = new Properties();
        Stream.of(ConfigConstants.values())
                .map(it -> it.name())
                .forEach(name -> addProperty(environment, props, name));
        OTHER_WRO_PROPS.forEach(name -> addProperty(environment, props, name));

        return props;
    }

    private void addProperty(@NonNull Environment environment, @NonNull Properties properties, @NonNull String name) {
        String value = environment.getProperty("wro." + name);
        if (value != null) {
            properties.put(name, value);
        }
    }
}
