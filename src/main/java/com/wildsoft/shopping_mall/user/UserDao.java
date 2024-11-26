package com.wildsoft.shopping_mall.user;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserDao {
  List<UserVO> getUserList(UserVO vo);
  UserVO getUser(UserVO vo);
  void insertUserEmail(UserVO vo);
  void updateUserNickname(UserVO vo);

  // 배송지
  int getShippingInfosIsDefault(ShippingInfoVO vo);
  void updateAllShippingInfosIsDefault(ShippingInfoVO vo);
  void updateShippingInfoIsDefault(ShippingInfoVO vo);
  void insertShippingInfo(ShippingInfoVO vo);
  List<ShippingInfoVO> getShippingInfosList(ShippingInfoVO vo);
  void deleteShippingInfo(ShippingInfoVO vo);
}
