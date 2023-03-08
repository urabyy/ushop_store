package com.ttudecor.interceptor;

import java.util.Optional;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.ttudecor.entity.User;
import com.ttudecor.service.UserService;
import com.ttudecor.utils.EncryptionUtils;

@Component
public class RememberInterceptor implements HandlerInterceptor{
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private HttpSession session;
	
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		
		if(session.getAttribute("fullname") != null) 
			return true;
		else {
			Cookie[] cookies = request.getCookies();
			
			if(cookies != null)
			for(Cookie c : cookies) {
				if(c.getName().equals("uid")) {
					String uid = EncryptionUtils.dencrypt("PASSWORD_ABCD123", c.getValue());
					
					try {
						int userId = Integer.parseInt(uid);
						User user = userService.findUserById(userId);

						session.setAttribute("fullname", user.getFullname());
						session.setAttribute("userId", user.getId());
						if(user.isIsadmin()) session.setAttribute("admin", true);
						
						return true;
					} catch (Exception e) {
					}
				}
			}
		}
		
		return true;

	}

}
