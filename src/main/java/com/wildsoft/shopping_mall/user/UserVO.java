package com.wildsoft.shopping_mall.user;

import lombok.Data;

@Data
public class UserVO {
  private int id;
  private String platform;
  private String role;
  private String email;
  private String nickname;
  private String created_at;
  private String updated_at;
}