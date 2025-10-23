import axios from "axios";

// API 기본 URL
const API_BASE_URL = "https://blog-api-zerobase-6e822c3f7763.herokuapp.com";

// Axios 인스턴스 생성
export const api = axios.create({
  baseURL: API_BASE_URL,
  headers: {
    "Content-Type": "application/json",
  },
});

// API 응답 인터페이스
export interface ApiResponse<T> {
  success: boolean;
  message: string;
  data: T;
}

// 카테고리 인터페이스
export interface Category {
  id: number;
  name: string;
  description: string;
  createdAt: string;
  updatedAt: string;
}

// 게시글 인터페이스
export interface Post {
  id: number;
  title: string;
  content: string;
  summary?: string;
  author: string;
  status: "DRAFT" | "PUBLISHED";
  createdAt: string;
  updatedAt: string;
  category?: Category;
}

// 카테고리 생성 요청 인터페이스
export interface CreateCategoryRequest {
  name: string;
  description?: string;
}

// 게시글 생성 요청 인터페이스
export interface CreatePostRequest {
  title: string;
  content: string;
  summary?: string;
  author: string;
  status?: "DRAFT" | "PUBLISHED";
  categoryId?: number;
}

// API 서비스 함수들
export const categoryService = {
  // 모든 카테고리 조회
  getAll: async (): Promise<Category[]> => {
    const response = await api.get<Category[]>("/api/categories");
    return response.data;
  },

  // 카테고리 생성
  create: async (data: CreateCategoryRequest): Promise<Category> => {
    const response = await api.post<Category>("/api/categories", data);
    return response.data;
  },

  // 카테고리 삭제
  delete: async (id: number): Promise<void> => {
    await api.delete(`/api/categories/${id}`);
  },
};

export const postService = {
  // 모든 게시글 조회
  getAll: async (): Promise<Post[]> => {
    const response = await api.get<Post[]>("/api/posts");
    return response.data;
  },

  // 게시글 생성
  create: async (data: CreatePostRequest): Promise<Post> => {
    const response = await api.post<Post>("/api/posts", data);
    return response.data;
  },

  // 게시글 삭제
  delete: async (id: number): Promise<void> => {
    await api.delete(`/api/posts/${id}`);
  },

  // 게시글 업데이트
  update: async (
    id: number,
    data: Partial<CreatePostRequest>
  ): Promise<Post> => {
    const response = await api.put<Post>(`/api/posts/${id}`, data);
    return response.data;
  },
};

// 헬스체크
export const healthService = {
  check: async () => {
    const response = await api.get("/api/health");
    return response.data;
  },
};
