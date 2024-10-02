package com.wildsoft.shopping_mall.controller;

import java.util.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.wildsoft.shopping_mall.user.UserDao;
import com.wildsoft.shopping_mall.user.UserVO;

@RestController
public class UserController {

  @Autowired
  private UserDao dao;

  @GetMapping("/api/userList")
  public List<UserVO> userList(UserVO vo) {
    return dao.getUserList(vo);
  }

  @GetMapping("/api/mypage")
  public UserVO mypage(UserVO vo) {
    return dao.getUser(vo);
  }
}
