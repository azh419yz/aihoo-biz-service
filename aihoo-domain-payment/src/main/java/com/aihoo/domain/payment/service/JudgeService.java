package com.aihoo.domain.payment.service;

import org.springframework.scheduling.annotation.Async;

public interface JudgeService {

    @Async
    void judge(String msg);
}
