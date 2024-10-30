package com.wildsoft.shopping_mall.controller;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.ModelAndView;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.wildsoft.shopping_mall.user.UserDao;
import com.wildsoft.shopping_mall.user.UserVO;

import io.github.cdimascio.dotenv.Dotenv;
import jakarta.servlet.http.HttpSession;

@RestController
@CrossOrigin(origins = "${cors.allowed.origins}")
public class UserController {
  @Autowired
  private UserDao dao;
  Dotenv dotenv = Dotenv.load();

  private final String googleClientId = dotenv.get("GOOGLE_CLIENT_ID");
  private final String googleClientSecret = dotenv.get("GOOGLE_CLIENT_SECRET");
  private final String googleRedirectUri = dotenv.get("GOOGLE_REDIRECT_URI");
  private final String GOOGLE_AUTH_URL = "https://accounts.google.com/o/oauth2/v2/auth";
  private final String GOOGLE_TOKEN_URL = "https://oauth2.googleapis.com/token";
  private final String GOOGLE_USER_INFO_URL = "https://www.googleapis.com/oauth2/v3/userinfo";

  static public class CodeRequest {
    private String code;

    public String getCode() {
      return code;
    }

    public void setCode(String code) {
      this.code = code;
    }
  }

  @GetMapping("/userList")
  public List<UserVO> userList(UserVO vo) {
    return dao.getUserList(vo);
  }

  @GetMapping("/mypage")
  public UserVO mypage(UserVO vo) {
    return dao.getUser(vo);
  }

  // 구글 로그인
  @GetMapping({ "/signup", "/signin" })
  public ModelAndView googleSignup() {
    String redirectUrl = GOOGLE_AUTH_URL + "?client_id=" + googleClientId +
        "&redirect_uri=" + URLEncoder.encode(googleRedirectUri, StandardCharsets.UTF_8) +
        "&response_type=code&scope=email%20profile";

    System.out.println("Redirecting to: " + redirectUrl);
    return new ModelAndView("redirect:" + redirectUrl);
  }

  @PostMapping("/auth/google")
  public UserVO googleCallback(@RequestBody CodeRequest codeReq, HttpSession session) {
    try {
      String code = codeReq.getCode();

      RestTemplate restTemplate = new RestTemplate();
      HttpHeaders headers = new HttpHeaders();
      headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
      headers.setAccept(List.of(MediaType.APPLICATION_JSON));

      MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
      body.add("code", code);
      body.add("client_id", googleClientId);
      body.add("client_secret", googleClientSecret);
      body.add("redirect_uri", googleRedirectUri);
      body.add("grant_type", "authorization_code");

      HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(body, headers);

      ResponseEntity<Map> tokenResponse = restTemplate.postForEntity(GOOGLE_TOKEN_URL, request, Map.class);
      Map<String, Object> tokenData = tokenResponse.getBody();

      if (tokenData == null || !tokenData.containsKey("access_token")) {
        throw new RuntimeException("Failed to retrieve access token");
      }

      String accessToken = (String) tokenData.get("access_token");

      // 2. 사용자 정보 요청
      HttpHeaders userInfoHeaders = new HttpHeaders();
      userInfoHeaders.setBearerAuth(accessToken); // Authorization 헤더에 토큰 추가

      HttpEntity<String> userInfoRequest = new HttpEntity<>(userInfoHeaders);

      ResponseEntity<String> userInfoResponse = restTemplate.exchange(
          GOOGLE_USER_INFO_URL, HttpMethod.GET, userInfoRequest, String.class);

      ObjectMapper mapper = new ObjectMapper();
      Map<String, Object> userInfo = mapper.readValue(userInfoResponse.getBody(), Map.class);

      // 사용자 정보 파싱
      String email = (String) userInfo.get("email");
      String name = (String) userInfo.get("name");

      // - 진입 시도 시 users 테이블에 해당 이메일 주소가 있는지 확인
      UserVO vo = new UserVO();
      vo.setEmail(email);
      UserVO user = dao.getUser(vo);

      if (user == null) {
        System.out.println("User Info: " + name + " (" + email + ")");
        session.setAttribute("email", email);

        dao.insertUserEmail(email);

        return vo;
      } else {
        session.setAttribute("user", user);

        return user;
      }

    } catch (Exception e) {
      e.printStackTrace();
      throw new RuntimeException("Google login failed");
    }
  }
}
