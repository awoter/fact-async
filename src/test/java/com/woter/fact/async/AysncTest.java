package com.woter.fact.async;

import java.util.Map;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.woter.fact.async.core.AsyncFutureCallback;
import com.woter.fact.async.core.AsyncTaskFuture;
import com.woter.fact.async.template.AsyncTemplate;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:/config/spring.xml" })
public class AysncTest {
	
	private final static Logger logger = LoggerFactory.getLogger(AysncTest.class);
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private TeacherService teacherService;
	
	@Test
	public void testAsyncAnnotation(){
		User user1 = userService.addUser(new User(34,"李一"));
		User user2 = userService.addUser(new User(35,"李二"));
		logger.info("异步任务已执行");
		logger.info("执行结果  任务1：{}  任务2：{}",user1.getName(),user2.getName());
	}
	
	
	@Test
	public void testAsyncTemplate(){
		
		AsyncTemplate.execute(new AsyncTaskFuture<User>() {

			@Override
			public User doAsync(Map<String, Object> dataMap) {
				
				return teacherService.addTeacher(new User(12,"李三"));
			}
	
		},new AsyncFutureCallback<User>(){

			@Override
			public void onSuccess(User user) {
				logger.info("添加用户成功：{}",user.getName());
			}

			@Override
			public void onFailure(Throwable t) {
				logger.info("添加用户失败：{}",t);
			}
			
		});
		logger.info("调用结束");
	}
	
}
