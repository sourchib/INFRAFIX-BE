package com.infrafix.citizen_reporting.service;

import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
public class BPService {
    @Scheduled(fixedRate = 80000)
    public void healthChecking() {
        System.out.println("MASIH AMAN");
    }

    @Async
    public void asyncTest() {
        for (int i = 0; i < 10; i++) {
            try {
                Thread.sleep(1000);
                System.out.println("Loop ke " + (i + 1));
            } catch (InterruptedException e) {
                System.out.println("Gagal Boz!!");
            }
        }
    }
}