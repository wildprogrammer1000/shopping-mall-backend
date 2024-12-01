package com.wildsoft.shopping_mall.shop;

import lombok.Data;

@Data
public class PaymentResponseVO {
  private int order_id;
  private int id;
  private String order_name;
  private int shipping_fee;
  private int total_amount;
  private String order_status;
  private String name;
  private String phone;
  private String shipping_address;
  private String payment_method;
  
  private int order_item_id;
  private int product_id;
  private String product_name;
  private String product_description;
  private int product_price;
  private int quantity;
  private int total_price;
}
