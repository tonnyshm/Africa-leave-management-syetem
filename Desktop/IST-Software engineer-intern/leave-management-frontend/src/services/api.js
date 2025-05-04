// src/services/api.js
import axios from 'axios';

const api = axios.create({
  baseURL: import.meta.env.VITE_LEAVE_API_BASE_URL, // .env should define this
  withCredentials: false, // we are using token auth not cookies
});

const authApi = axios.create({
  baseURL: import.meta.env.VITE_AUTH_API_BASE_URL,
  withCredentials: false,
});

// Automatically attach the token to every request
[api, authApi].forEach(instance => {
  instance.interceptors.request.use(config => {
    const token = localStorage.getItem('id_token');
    if (token) {
      config.headers.Authorization = `Bearer ${token}`;
    }
    return config;
  }, error => Promise.reject(error));
});

export { api, authApi };
