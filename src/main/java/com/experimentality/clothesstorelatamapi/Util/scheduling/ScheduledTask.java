package com.experimentality.clothesstorelatamapi.Util.scheduling;

import com.experimentality.clothesstorelatamapi.visit.service.VisitService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class ScheduledTask {

    private VisitService visitService;
    private static final long interval_milliSeconds = 15;

    @Autowired
    public ScheduledTask(VisitService visitService) {
        this.visitService = visitService;
    }

    @Scheduled(fixedRate = interval_milliSeconds)
    public void reportCurrentTime() {
        visitService.resetVisits();
        System.out.println("se están reiniciando las visitas" + interval_milliSeconds);

        int a  = 8;
        System.out.println(a + " => a");
    }
}
