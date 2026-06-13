package com.aihoo.domain.im.service.impl;

import com.aihoo.domain.im.model.entity.LoginQuestion;
import com.aihoo.domain.im.model.mapper.LoginQuestionMapper;
import com.aihoo.domain.im.service.LoginQuestionService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

@Service
public class LoginQuestionServiceImpl extends ServiceImpl<LoginQuestionMapper, LoginQuestion> implements LoginQuestionService {
}
