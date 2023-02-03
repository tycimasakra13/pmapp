package com.project.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "spring.data.web.pageable")
public class PeagableConfig {
    private Integer defaultPageSize;

    public Integer getDefaultPageSize() {
        return defaultPageSize;
    }

    public void setDefaultPageSize(Integer defaultPageSize) {
        this.defaultPageSize = defaultPageSize;
    }

}

/*
spring.data.web.pageable.default-page-size=5         # Default page size.
spring.data.web.pageable.max-page-size=2000           # Maximum page size to be accepted.
spring.data.web.pageable.size-parameter=size          # Page size parameter name.

*/