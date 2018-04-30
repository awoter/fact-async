package com.woter.fact.async;

import com.woter.fact.async.annotation.Async;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final static Logger logger = LoggerFactory.getLogger(TeacherService.class);

    @Async
    public User addUser(User user) {

        logger.info("正在添加用户{}", user.getName());

        return user;
    }
}
