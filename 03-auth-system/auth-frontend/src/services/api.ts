import axios from "axios";
import {
  LoginRequest,
  SignupRequest,
  AuthResponse,
  ApiResponse,
  User,
} from "../types/auth";

const API_BASE_URL = "https://warm-gorge-17774-fa28e4fd90c3.herokuapp.com/api";

const api = axios.create({
  baseURL: API_BASE_URL,
  headers: {
    "Content-Type": "application/json",
  },
});

// Request interceptor to add auth token
api.interceptors.request.use(
  (config) => {
    const token = localStorage.getItem("token");
    if (token) {
      config.headers.Authorization = `Bearer ${token}`;
    }
    return config;
  },
  (error) => {
    return Promise.reject(error);
  }
);

// Response interceptor to handle token refresh
api.interceptors.response.use(
  (response) => response,
  async (error) => {
    const originalRequest = error.config;

    if (error.response?.status === 401 && !originalRequest._retry) {
      originalRequest._retry = true;

      try {
        const refreshToken = localStorage.getItem("refreshToken");
        if (refreshToken) {
          const response = await axios.post(
            `${API_BASE_URL}/auth/refreshtoken`,
            {
              refreshToken: refreshToken,
            }
          );

          const { token } = response.data;
          localStorage.setItem("token", token);

          // Retry original request
          originalRequest.headers.Authorization = `Bearer ${token}`;
          return api(originalRequest);
        }
      } catch (refreshError) {
        // Refresh failed, redirect to login
        localStorage.removeItem("token");
        localStorage.removeItem("refreshToken");
        window.location.href = "/login";
      }
    }

    return Promise.reject(error);
  }
);

export const authAPI = {
  login: (data: LoginRequest): Promise<ApiResponse<AuthResponse>> =>
    api.post("/auth/login", data).then((res) => res.data),

  signup: (data: SignupRequest): Promise<ApiResponse<AuthResponse>> =>
    api.post("/auth/signup", data).then((res) => res.data),

  refreshToken: (
    refreshToken: string
  ): Promise<ApiResponse<{ token: string }>> =>
    api.post("/auth/refreshtoken", { refreshToken }).then((res) => res.data),

  getCurrentUser: (): Promise<ApiResponse<User>> =>
    api.get("/users/me").then((res) => res.data),

  getAllUsers: (): Promise<ApiResponse<User[]>> =>
    api.get("/admin/users").then((res) => res.data),
};

export default api;
