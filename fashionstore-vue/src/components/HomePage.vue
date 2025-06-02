<template>
    <header>
        <p>Chào mừng đến với Haravan shop</p>   
      <hr />
      <nav class="navbar navbar-light bg-light">
        <div class="container">
          <!-- Logo -->
          <a class="navbar-brand" href="#">
            <img
              src="@/assets/img/LogoChinh.png"
              alt="Logo"
              width="300"
                height="50"
            />
          </a>

          <!-- Form tìm kiếm (nằm bên trái) -->
          <form class="d-flex">
            <input
              class="form-control me-2"
              type="search"
              placeholder="Search"
              aria-label="Search"
            />
            <button class="btn btn-outline-success" type="submit">
              Search
            </button>
          </form>

          <!-- Nút giỏ hàng và đăng nhập/đăng ký (nằm bên phải) -->
          <div class="d-flex ms-auto">
            <button class="btn btn-outline-primary me-2" type="button">
              🛒 Giỏ hàng
            </button>
            <button class="btn btn-outline-secondary" type="button">
              Đăng nhập
            </button>
            <span class="mx-1"></span>
            <button class="btn btn-outline-secondary" type="button">
              Đăng ký
            </button>
          </div>
        </div>
      </nav>
    </header>
    <main>
      <!-- place main content here -->
      <div class="container mt-4">
        <div class="row">
          <div class="col-4">
            <ul class="list-group">
              <button>
                <li class="list-group-item">Deal sốc hội viên</li>
              </button>
              <button><li class="list-group-item">Áo</li></button
              ><button><li class="list-group-item">Set bộ</li></button
              ><button><li class="list-group-item">Quần</li></button
              ><button><li class="list-group-item">Phụ kiện</li></button
              ><button><li class="list-group-item">Sản phẩm khác</li></button>
            </ul>
          </div>
          <div class="col-8">
            <img
              src="@/assets/img/banner.jpg"
              width="100%"
              class="img-fluid rounded shadow"
              alt="Banner"
            />
          </div>
        </div>
        <div class="section-title">SẢN PHẨM BÁN CHẠY</div>

        <!-- Danh sách sản phẩm -->
        <div class="row g-3">
          <!-- Sản phẩm 1 -->
          <div class="col-md-3">
            <div class="position-relative product-card">
              <img src="@/assets/img/img1.jpg" alt="Mũ" class="img-fluid" />
              <div class="mt-2">
                <div class="product-name">
                  Mũ Lưỡi Trai Chống Tia UV | Keith Haring
                </div>
                <div class="product-price">252,250 VNĐ</div>
              </div>
            </div>
          </div>

          <!-- Sản phẩm 2 -->
          <div class="col-md-3">
            <div class="position-relative product-card">
              <img src="@/assets/img/img2.jpg" alt="Áo Polo" class="img-fluid" />
              <div class="mt-2">
                <div class="product-name">
                  Áo polo nam premium tay ngắn sọc gân form fitted
                </div>
                <div class="product-price">399,000 VNĐ</div>
              </div>
            </div>
          </div>

          <!-- Sản phẩm 3 -->
          <div class="col-md-3">
            <div class="position-relative product-card">
              <img src="@/assets/img/img3.jpg" alt="Mũ" class="img-fluid" />
              <div class="mt-2">
                <div class="product-name">Mũ Lubby</div>
                <div class="product-price">252,250 VNĐ</div>
              </div>
            </div>
          </div>
          <!-- Sản phẩm 4 -->
          <div class="col-md-3">
            <div class="position-relative product-card">
              <img src="@/assets/img/img4.jpg" alt="Áo Polo" class="img-fluid" />
              <div class="mt-2">
                <div class="product-name">
                  Áo polo nam premium tay ngắn sọc gân form fitted
                </div>
                <div class="product-price">399,000 VNĐ</div>
              </div>
            </div>
          </div>
        </div>
        <div class="section-title">SẢN PHẨM SALE</div>
        <!-- Danh sách sản phẩm -->
        <div class="row g-3">
          <!-- Sản phẩm 1 -->
          <div class="col-md-3">
            <div class="position-relative product-card">
              <img src="@/assets/img/img4.jpg" alt="Mũ" class="img-fluid" />
              <div class="mt-2">
                <div class="product-name">Mũ Lubby</div>
                <div class="product-price">252,250 VNĐ</div>
              </div>
            </div>
          </div>
          <!-- Sản phẩm 2 -->
          <div class="col-md-3">
            <div class="position-relative product-card">
              <img src="@/assets/img/img3.jpg" alt="Áo Polo" class="img-fluid" />
              <div class="mt-2">
                <div class="product-name">
                  Áo polo nam premium tay ngắn sọc gân form fitted
                </div>
                <div class="product-price">399,000 VNĐ</div>
              </div>
            </div>
          </div>
          <!-- Sản phẩm 3 -->
          <div class="col-md-3">
            <div class="position-relative product-card">
              <img src="@/assets/img/img2.jpg" alt="Mũ" class="img-fluid" />
              <div class="mt-2">
                <div class="product-name">Mũ Lubby</div>
                <div class="product-price">252,250 VNĐ</div>
              </div>
            </div>
          </div>
          <!-- Sản phẩm 4 -->
          <div class="col-md-3">
            <div class="position-relative product-card">
              <img src="@/assets/img/img1.jpg" alt="Áo Polo" class="img-fluid" />
              <div class="mt-2">
                <div class="product-name">
                  Áo polo nam premium tay ngắn sọc gân form fitted
                </div>
                <div class="product-price">399,000 VNĐ</div>
              </div>
            </div>
          </div>
        </div>
      </div>
    </main>
    <footer>
      <!-- place footer here -->
      <div class="footer">
        <div class="container">
          <div class="row justify-content-center">
            <!-- Cột 1: Hỗ trợ khách hàng -->
            <div class="col-md-4 mb-3">
              <img
                src="@/assets/img/LogoChinh.png"
                alt="Haravan logo"
              />
              <p>
                HOTLINE: 093.133.5634 - MR.HÙNG<br />
                Email:
                <a href="mailto:dodoctuanhung@gmail.com"
                  >dodoctuanhung@gmail.com</a
                ><br />
                DNTN Dịch vụ và Thương mại Hùng Giang<br />
                GPKD số 0318210496 do Sở KH và ĐT TP. Hồ Chí Minh cấp ngày
                12/12/2023<br />
                Địa chỉ văn phòng: 116 Nguyễn Huy Tưởng, Liên Chiểu, Đà Nẵng
              </p>
            </div>

            <!-- Cột 2: Chính sách -->
            <div class="col-md-4 mb-3">
              <h6><strong>CHÍNH SÁCH WEBSITE</strong></h6>
              <ul>
                <li><a href="#">Thông tin về điều kiện giao dịch chung</a></li>
                <li><a href="#">Chính sách đổi trả</a></li>
                <li><a href="#">Chính sách bảo mật thông tin</a></li>
                <li><a href="#">Chính sách vận chuyển</a></li>
                <li><a href="#">Chính sách thanh toán</a></li>
                <li><a href="#">Chính sách bảo hành</a></li>
              </ul>
            </div>

            <!-- Cột 3: Kinh nghiệm bán hàng -->
            <div class="col-md-4 mb-3">
              <h6><strong>KINH NGHIỆM BÁN HÀNG</strong></h6>
              <ul>
                <li><a href="#">Kiến thức marketing online</a></li>
                <li><a href="#">Kinh nghiệm xử lý đơn hàng online</a></li>
                <li><a href="#">Kinh nghiệm quản lý kho</a></li>
                <li><a href="#">Kinh nghiệm quản lý website</a></li>
              </ul>
            </div>
          </div>
        </div>
      </div>
    </footer>
</template>
<style>
      .product-card img {
        width: 300px;
        height: 400px;
      }
      .sold-out {
        position: absolute;
        top: 0;
        right: 0;
        background-color: red;
        color: white;
        padding: 2px 6px;
        font-size: 0.8rem;
        font-weight: bold;
      }
      .product-name {
        font-weight: bold;
      }
      .product-price {
        color: red;
        font-weight: bold;
      }
      .section-title {
        background-color: #00b5e2;
        color: white;
        margin-top: 20px;
        padding: 8px 16px;
        font-size: 1.2rem;
        font-weight: bold;
        display: inline-block;
        margin-bottom: 20px;
      }
      .footer {
        background-color: #00c0f1; /* Nền xanh dương giống hình */
        color: white;
        padding: 30px 20px;
        font-size: 14px;
      }

      .footer a {
        color: white;
        text-decoration: underline;
      }

      .footer a:hover {
        text-decoration: none;
      }

      .footer ul {
        padding-left: 1rem;
      }

      .footer ul li {
        margin-bottom: 6px;
      }

      .footer img {
        max-width: 120px;
        margin-bottom: 10px;
      }
    </style>
