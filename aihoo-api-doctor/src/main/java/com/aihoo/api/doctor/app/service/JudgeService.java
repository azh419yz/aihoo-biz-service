package com.aihoo.api.doctor.app.service;

import org.springframework.scheduling.annotation.Async;

public interface JudgeService {
    
    @Async
    void judge(String msg);
}
