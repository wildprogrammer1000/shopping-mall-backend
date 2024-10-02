package com.wildsoft.shopping_mall.shop;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ShopDao {
  List<ProductVO> getProductList(ProductVO vo);
  int getProductsCount();
}
