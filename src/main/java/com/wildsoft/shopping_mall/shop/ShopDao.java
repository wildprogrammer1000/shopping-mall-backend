package com.wildsoft.shopping_mall.shop;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ShopDao {
  // 상품
  List<ProductVO> getProductList(ProductVO vo);
  int getProductsCount();
  ProductVO getProduct(ProductVO vo);
  void insertProduct(ProductVO vo);
  void updateProduct(ProductVO vo);
  void deleteProduct(ProductVO vo);

  // 장바구니
  List<CartVO> getCartList(CartVO vo);
  CartVO findCartItem(CartVO vo);
  void insertCart(CartVO vo);
  void updateCart(CartVO vo);
  void deleteCart(CartVO vo);
  void deleteCartAll(CartVO vo);
}
