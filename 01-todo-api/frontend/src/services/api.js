import axios from "axios";

const API_URL = import.meta.env.VITE_API_URL || "http://localhost:8080";

const api = axios.create({
  baseURL: API_URL,
  headers: {
    "Content-Type": "application/json",
  },
});

// Request interceptor to add token
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

// Response interceptor to handle errors
api.interceptors.response.use(
  (response) => response,
  (error) => {
    if (error.response?.status === 401) {
      localStorage.removeItem("token");
      localStorage.removeItem("user");
      window.location.href = "/login";
    }
    return Promise.reject(error);
  }
);

// Auth APIs
export const authAPI = {
  signup: (data) => api.post("/api/auth/signup", data),
  login: (data) => api.post("/api/auth/login", data),
};

// Todo APIs
export const todoAPI = {
  getAllTodos: (page = 0, size = 20) =>
    api.get("/api/todos", { params: { page, size } }),
  getTodoById: (id) => api.get(`/api/todos/${id}`),
  createTodo: (data) => api.post("/api/todos", data),
  updateTodo: (id, data) => api.put(`/api/todos/${id}`, data),
  deleteTodo: (id) => api.delete(`/api/todos/${id}`),
  searchTodos: (params) => api.get("/api/todos/search", { params }),
  getTodosByStatus: (completed) => api.get(`/api/todos/status/${completed}`),
  getTodosByPriority: (priority) => api.get(`/api/todos/priority/${priority}`),
};

// Category APIs
export const categoryAPI = {
  getAllCategories: () => api.get("/api/categories"),
  getCategoryById: (id) => api.get(`/api/categories/${id}`),
  createCategory: (data) => api.post("/api/categories", data),
  updateCategory: (id, data) => api.put(`/api/categories/${id}`, data),
  deleteCategory: (id) => api.delete(`/api/categories/${id}`),
};

// Statistics API
export const statisticsAPI = {
  getStatistics: () => api.get("/api/statistics"),
};

// Export APIs
export const exportAPI = {
  exportJSON: () => api.get("/api/export/json"),
  exportCSV: () => api.get("/api/export/csv", { responseType: "blob" }),
};

export default api;
