package com.hyphenated.card.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;
import org.springframework.stereotype.Controller;

/**
 * Created by Nitin on 08-11-2015.
 */
@Configuration
@ComponentScan(value = "com.hyphenated.card", excludeFilters = {
        @ComponentScan.Filter(type = FilterType.ANNOTATION, value = Controller.class),
        @ComponentScan.Filter(type = FilterType.ANNOTATION, value = Configuration.class)
})
public class SpringConfig {

//    @Bean(name = "jsonpCallbackFilter")
//    public JsonpCallbackFilter getJsonpCallbackFilter() {
//        return new JsonpCallbackFilter();
//    }
}