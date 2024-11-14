package com.wildsoft.shopping_mall.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RestController;

import com.wildsoft.shopping_mall.shop.CartVO;
import com.wildsoft.shopping_mall.shop.OrderVO;
import com.wildsoft.shopping_mall.shop.ProductResponseVO;
import com.wildsoft.shopping_mall.shop.ProductVO;
import com.wildsoft.shopping_mall.shop.ShopDao;

import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
@CrossOrigin(origins = "${cors.allowed.origins}")
public class ShopController {

  @Autowired 
  private ShopDao dao;

  // 상품
  @GetMapping("/getProductList")
  public ProductResponseVO getProductList(ProductVO vo) {
    List<ProductVO> productList = dao.getProductList(vo);
    int totalCount = dao.getProductsCount();

    return new ProductResponseVO(productList, totalCount);
  }

  @PostMapping("/insertProduct")
  public void insertProduct(@RequestBody ProductVO vo) {
    dao.insertProduct(vo);
  }

  @PostMapping("/getProduct")
  public ProductVO getProduct(@RequestBody ProductVO vo) {

    return dao.getProduct(vo);
  }

  @PostMapping("/updateProduct")
  public void updateProduct(@RequestBody ProductVO vo) {
    
    dao.updateProduct(vo);
  }
  
  @PostMapping("/deleteProduct")
  public void deleteProduct(@RequestBody ProductVO vo) {
    
    dao.deleteProduct(vo);
  }

  // 장바구니
  @GetMapping("/getCartList")
  public List<CartVO> getCartList(@RequestBody CartVO vo) {

    return dao.getCartList(vo);
  }

  @PostMapping("/addCart")
  public String addCart(@RequestBody CartVO vo) {
    CartVO cart = dao.findCartItem(vo);

    if (cart == null) {
      dao.insertCart(vo);
      
      return "added to cart";
    } else {
      cart.setQuantity(cart.getQuantity() + vo.getQuantity());
      dao.updateCart(cart);

      return "updated in cart";
    }
  }

  @PostMapping("/deleteCart")
  public void deleteCart(@RequestBody CartVO vo) {
    dao.deleteCart(vo);
  }

  @PostMapping("/updateCart")
  public void updateCart(@RequestBody CartVO vo) {
    dao.updateCart(vo);
  }

  @PostMapping("/updateCartAll")
  public void updateCartAll(@RequestBody List<CartVO> list) {
    for (CartVO vo : list) {
      dao.updateCart(vo);
    }
  }

  @PostMapping("/deleteCartAll")
  public void deleteCartAll(@RequestBody CartVO vo) {
    dao.deleteCartAll(vo);
  }

  // 주문하기
  @PostMapping("/addOrder")
  public void addOrder(@RequestBody List<OrderVO> list) {
    for (OrderVO vo : list) {
      CartVO cart = new CartVO();
      cart.setProduct_id(vo.getProduct_id());
      dao.deleteCart(cart);
      dao.insertOrder(vo);
    }
  }
  
}
