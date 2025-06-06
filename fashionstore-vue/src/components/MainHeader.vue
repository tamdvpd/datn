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
          <button class="btn btn-outline-success" type="submit">Search</button>
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
    <div id="app">
      <nav>
        <router-link to="/">Home</router-link> |
        <router-link to="/login">Login</router-link> |
        <router-link to="/register">Register</router-link> |
        <router-link to="/cart">Cart</router-link>
      </nav>
      <router-view></router-view>
    </div>
  </header>
</template>
