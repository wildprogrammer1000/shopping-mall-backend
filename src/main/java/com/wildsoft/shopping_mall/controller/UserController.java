package com.wildsoft.shopping_mall.controller;

import java.util.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.wildsoft.shopping_mall.user.UserDao;
import com.wildsoft.shopping_mall.user.UserVO;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
@CrossOrigin(origins = "${cors.allowed.origins}")
public class UserController {

  @Autowired
  private UserDao dao;

  @GetMapping("/userList")
  public List<UserVO> userList(UserVO vo) {
    return dao.getUserList(vo);
  }

  @GetMapping("/mypage")
  public UserVO mypage(UserVO vo) {
    return dao.getUser(vo);
  }

  @PostMapping("/signin/google")
  public String postMethodName(@RequestBody String code) {
      
      return code;
  }
  
}
