package com.wildsoft.shopping_mall.shop;

import lombok.Data;

@Data
public class OrderVO {
  private int order_id;
  private int id;
  private int shipping_fee;
  private int total_amount;
  private String order_status;
  private String name;
  private String phone;
  private String shipping_address;
  private String payment_method;
  private String order_date;
  private String updated_at;
}
