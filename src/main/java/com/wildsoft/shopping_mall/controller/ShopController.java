package com.wildsoft.shopping_mall.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RestController;

import com.wildsoft.shopping_mall.shop.CartVO;
import com.wildsoft.shopping_mall.shop.OrderItemVO;
import com.wildsoft.shopping_mall.shop.OrderVO;
import com.wildsoft.shopping_mall.shop.PaymentResponseVO;
import com.wildsoft.shopping_mall.shop.ProductResponseVO;
import com.wildsoft.shopping_mall.shop.ProductVO;
import com.wildsoft.shopping_mall.shop.ShopDao;

import org.springframework.web.bind.annotation.GetMapping;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.transaction.annotation.Transactional;

@RestController
@CrossOrigin(origins = "${cors.allowed.origins}", allowCredentials = "true", // 세션/쿠키 사용
    methods = { RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE, RequestMethod.OPTIONS })
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
  @PostMapping("/getCartList")
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

  @PostMapping("/deleteCart")
  public void deleteCart(@RequestBody List<CartVO> list) {
    for (CartVO vo : list) {
      dao.deleteCart(vo);
    }
  }

  // 주문하기
  @PostMapping("/order/complete")
  @Transactional
  public ResponseEntity<Map<String, Object>> completeOrder(@RequestBody Map<String, Object> orderData) {
    try {
      // 결제 상태 확인
      String paymentStatus = (String) orderData.get("paymentStatus");
      if (!"SUCCESS".equals(paymentStatus)) {
        throw new RuntimeException("결제가 성공적으로 완료되지 않았습니다.");
      }
      // 주문 정보 저장
      OrderVO orderVO = new OrderVO();

      Map<String, Object> shippingInfo = (Map<String, Object>) orderData.get("shippingInfo");
      orderVO.setId((Integer) shippingInfo.get("id"));
      orderVO.setName((String) shippingInfo.get("name"));
      orderVO.setPhone((String) shippingInfo.get("phone"));
      orderVO.setShipping_address((String) shippingInfo.get("shipping_address"));

      orderVO.setOrder_status("결제완료");
      orderVO.setOrder_name((String) orderData.get("order_name"));
      orderVO.setShipping_fee((Integer) orderData.get("shippingFee"));
      orderVO.setTotal_amount((Integer) orderData.get("totalAmount"));
      orderVO.setPayment_method("Card");
      dao.insertOrder(orderVO);

      int orderId = orderVO.getOrder_id();
      // 주문 상품 상세 정보 저장
      List<Map<String, Object>> productList = (List<Map<String, Object>>) orderData.get("products");
      for (Map<String, Object> product : productList) {
        OrderItemVO orderItemVO = new OrderItemVO();
        orderItemVO.setOrder_id(orderId);
        orderItemVO.setProduct_id((Integer) product.get("product_id"));
        orderItemVO.setQuantity((Integer) product.get("quantity"));
        orderItemVO.setTotal_price((Integer) product.get("total_price"));
        dao.insertOrderItem(orderItemVO);
        // 장바구니에서 제품 삭제
        CartVO cartVO = new CartVO();
        cartVO.setId((Integer) product.get("id"));
        cartVO.setProduct_id((Integer) product.get("product_id"));
        dao.deleteCart(cartVO);
      }

      Map<String, Object> response = new HashMap<>();
      response.put("message", "주문이 성공적으로 완료되었습니다.");
      response.put("orderData", orderData);

      return ResponseEntity.ok(response);
    } catch (Exception e) {
      e.printStackTrace();
      Map<String, Object> errorResponse = new HashMap<>();
      errorResponse.put("error", e.getMessage());
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
    }
  }

  @PostMapping("/getPaymentAllList")
  public List<OrderVO> getPaymentAllList(@RequestBody OrderVO vo) {

    return dao.getPaymentAllList(vo);
  }

  @PostMapping("/getPaymentOneList")
  public List<PaymentResponseVO> getPaymentOneList(@RequestBody PaymentResponseVO vo) {

    return dao.getPaymentOneList(vo);
  }
}
