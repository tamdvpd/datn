import { createApp } from 'vue'
import App from './App.vue'
import axios from 'axios'
import 'bootstrap/dist/css/bootstrap.min.css';
import 'bootstrap/dist/js/bootstrap.bundle.min.js';


// import BootstrapVue3
import BootstrapVue3 from 'bootstrap-vue-3'

// import Bootstrap CSS và BootstrapVue CSS
import 'bootstrap/dist/css/bootstrap.css'
import 'bootstrap-vue-3/dist/bootstrap-vue-3.css'

// (Tuỳ chọn) import Bootstrap JS và icon
import 'bootstrap/dist/js/bootstrap.bundle.min.js'
import 'bootstrap-icons/font/bootstrap-icons.css'

// Import Vue Router
import router from './router'
const app = createApp(App)
app.config.globalProperties.$axios = axios
axios.defaults.baseURL = 'http://localhost:8080'
app.use(router)
app.use(BootstrapVue3)

app.mount('#app')