import { createApp } from 'vue'
import App from './App.vue'
import axios from 'axios'

import 'bootstrap/dist/css/bootstrap.min.css'
import 'bootstrap/dist/js/bootstrap.bundle.min.js'
import 'bootstrap-icons/font/bootstrap-icons.css'
import 'bootstrap-vue-3/dist/bootstrap-vue-3.css'

import BootstrapVue3 from 'bootstrap-vue-3'
import router from './router'

// 👉 Đúng plugin
import GoogleLogin from 'vue3-google-login'

const app = createApp(App)
app.config.globalProperties.$axios = axios
axios.defaults.baseURL = 'http://localhost:8080'

app.use(router)
app.use(BootstrapVue3)

app.use(GoogleLogin, {
  clientId: '1084049649244-6ba4m8j19qo3dtuoihi5q6o2bojkrpbi.apps.googleusercontent.com'
})

app.mount('#app')
