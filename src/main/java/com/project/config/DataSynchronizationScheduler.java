package com.project.config;

import com.project.repositories.ProjectRepository;
import com.project.services.ProjectSynchronizerES;
import com.project.services.StudentSynchronizerES;
import com.project.services.ZadanieSynchronizerES;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "sync")
public class DataSynchronizationScheduler {
    @Autowired
    private ProjectSynchronizerES projectSynchronizerES;
    
    @Autowired
    private StudentSynchronizerES studentSynchronizerES;
    
    @Autowired
    private ZadanieSynchronizerES zadanieSynchronizerES;
    
    @Autowired
    private ProjectRepository pr;
    
    @Scheduled(fixedRate = 30000)
    public void synchronizeData() {
        projectSynchronizerES.synchronizeData();
        studentSynchronizerES.synchronizeData();
        zadanieSynchronizerES.synchronizeData();
    }
}
