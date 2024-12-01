CREATE TABLE IF NOT EXISTS users (
	id INT AUTO_INCREMENT PRIMARY KEY, 
	platform VARCHAR(20) NOT NULL,
	role VARCHAR(10) NOT NULL DEFAULT "user",
	email VARCHAR(50) NOT NULL,
	nickname VARCHAR(20) UNIQUE,
	created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

-- 상품 정보 테이블 (추후 이미지 추가 예정)
CREATE TABLE IF NOT EXISTS products (
  product_id INT AUTO_INCREMENT PRIMARY KEY, -- 상품 ID (기본키)
  product_name VARCHAR(255) NOT NULL, -- 상품 이름
  product_description TEXT, -- 설명 및 이미지 URL을 포함
  product_price DECIMAL(10, 2) NOT NULL, -- 가격
  -- product_image_name VARCHAR(255) DEFAULT 'space.png', -- 상품 이미지
  stock_quantity INT NOT NULL, -- 재고 수량
  category VARCHAR(100), -- 카테고리
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP, -- 생성 일시
  updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP -- 수정 일시
);

-- 장바구니 테이블
CREATE TABLE IF NOT EXISTS carts (
  cart_id INT AUTO_INCREMENT PRIMARY KEY, -- 장바구니 ID (기본키)
  id INT NOT NULL, -- 사용자 ID (외래키)
  product_id INT NOT NULL, -- 상품 ID (외래키)
  product_price DECIMAL(10, 2) NOT NULL, -- 가격
  -- product_image_name VARCHAR(255) DEFAULT 'space.png', -- 상품 이미지
  quantity INT NOT NULL, -- 수량
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP, -- 생성 일시
  updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP, -- 수정 일시
  FOREIGN KEY (id) REFERENCES users(id), -- 사용자 외래키
  FOREIGN KEY (product_id) REFERENCES products(product_id) -- 상품 외래키
);

-- 배송 정보 테이블
CREATE TABLE IF NOT EXISTS shipping_infos (
  shipping_id INT AUTO_INCREMENT PRIMARY KEY, -- 배송정보 ID (기본키)
  id INT NOT NULL, -- 사용자 ID
  name VARCHAR(255) NOT NULL, -- 고객명
  phone VARCHAR(20), -- 전화번호
  shipping_address TEXT, -- 주소
  is_default tinyint(1), -- 기본 배송지 1-true / 0-false
  nickname VARCHAR(50), -- 배송지명
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP, -- 생성 일시
  updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP, -- 수정 일시
  FOREIGN KEY (id) REFERENCES users(id) -- 사용자 외래키
);

-- 주문 정보 테이블
CREATE TABLE IF NOT EXISTS orders (
  order_id INT AUTO_INCREMENT PRIMARY KEY, -- 주문 ID (기본키)
  id INT NOT NULL, -- 사용자 ID (외래키)
  -- product_image_name VARCHAR(255) DEFAULT 'space.png', -- 상품 이미지
  order_name VARCHAR(255) NOT NULL, -- 결제 이름
  shipping_fee DECIMAL(10, 2) NOT NULL, -- 배송비
  total_amount DECIMAL(10, 2) NOT NULL, -- 전체 상품 총 주문 금액
  order_status ENUM('결제완료', '결제취소', '배송준비', '배송중', '배송완료', '교환', '환불') NOT NULL, -- 주문 상태
  name VARCHAR(255) NOT NULL, -- 고객명
  phone VARCHAR(20), -- 전화번호
  shipping_address TEXT, -- 배송 주소
  payment_method ENUM('Card', 'KakaoPay', 'Bank Transfer') NOT NULL, -- 결제 방법
  order_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP, -- 주문 일시
  updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP, -- 수정 일시
  FOREIGN KEY (id) REFERENCES users(id) -- 사용자 외래키
);

-- 주문 상품 상세 테이블
CREATE TABLE IF NOT EXISTS order_items (
  order_item_id INT AUTO_INCREMENT PRIMARY KEY, -- 주문 상품 ID (기본키)
  order_id INT NOT NULL, -- 주문 ID (외래키)
  product_id INT NOT NULL, -- 상품 ID (외래키)
  quantity INT NOT NULL, -- 주문 수량
  total_price DECIMAL(10, 2) NOT NULL, -- 상품별 총 가격
  order_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP, -- 주문 일시
  updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP, -- 수정 일시
  FOREIGN KEY (order_id) REFERENCES orders(order_id), -- 주문 외래키
  FOREIGN KEY (product_id) REFERENCES products(product_id) -- 상품 외래키
);