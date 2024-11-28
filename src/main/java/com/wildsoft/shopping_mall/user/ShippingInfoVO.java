package com.wildsoft.shopping_mall.user;

import lombok.Data;

@Data
public class ShippingInfoVO {
  private int shipping_id;
  private int id;
  private String name;
  private String phone;
  private String shipping_address;
  private int is_default;
  private String nickname;
  private String created_at;
  private String updated_at;
}
