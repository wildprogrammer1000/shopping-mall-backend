package com.wildsoft.shopping_mall.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.wildsoft.shopping_mall.shop.ProductResponseVO;
import com.wildsoft.shopping_mall.shop.ProductVO;
import com.wildsoft.shopping_mall.shop.ShopDao;

import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;



@RestController
@CrossOrigin(origins = "${cors.allowed.origins}")
public class ShopController {

  @Autowired 
  private ShopDao dao;

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

  @GetMapping("/getProduct")
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
}
