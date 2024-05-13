package com.trungnguyen.synchornizedataservice.controller;


import com.trungnguyen.synchornizedataservice.cronjobs.SynchronizationJobs;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {


    private SynchronizationJobs jobs;

    public TestController(SynchronizationJobs jobs) {
        this.jobs = jobs;
    }

    @GetMapping("/test")
    public void test(){
        jobs.synchronizeData();
    }

}
