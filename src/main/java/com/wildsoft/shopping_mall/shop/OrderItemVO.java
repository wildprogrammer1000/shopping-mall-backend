package com.wildsoft.shopping_mall.shop;

import lombok.Data;

@Data
public class OrderItemVO {
  private int order_item_id;
  private int order_id;
  private int product_id;
  private int quantity;
  private int total_price;
  private String order_date;
  private String updated_at;
}

