package com.wildsoft.shopping_mall.shop;

import java.util.List;

import lombok.Data;

@Data
public class ProductResponseVO {
  private List<ProductVO> product_list;
  private int total_count;

  public ProductResponseVO(List<ProductVO> product_list, int total_count) {
    this.product_list = product_list;
    this.total_count = total_count;
  }
}
