package com.wildsoft.shopping_mall.shop;

import lombok.Data;

@Data
public class CartVO {
  private int cart_id;
  private int id;
  private int product_id;
  private String product_price;
  private int quantity;
  private int total_price;
  private String created_at;
  private String updated_at;

  private String product_name; // 상품명
  private String product_description; // 상품 설명
  private String category; // 카테고리
}
