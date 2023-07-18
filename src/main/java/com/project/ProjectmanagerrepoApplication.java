package com.project;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableElasticsearchRepositories("com.project.repositories.elastic")
@EnableScheduling
public class ProjectmanagerrepoApplication {

    public static void main(String[] args) {
        SpringApplication.run(ProjectmanagerrepoApplication.class, args);
    }

}
