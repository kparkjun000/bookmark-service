import axios from 'axios';
import { User, Bookmark, Tag, CreateUserRequest, CreateBookmarkRequest, MetadataResponse } from '../types/api';

const API_BASE_URL = 'https://zerobase-bookmark-service-0aab4ffd66ec.herokuapp.com/api';

const api = axios.create({
  baseURL: API_BASE_URL,
  headers: {
    'Content-Type': 'application/json',
  },
});

// User API
export const userApi = {
  // 모든 사용자 조회
  getAllUsers: async (): Promise<User[]> => {
    const response = await api.get('/users');
    return response.data;
  },

  // 사용자 생성
  createUser: async (userData: CreateUserRequest): Promise<User> => {
    const response = await api.post('/users', userData);
    return response.data;
  },

  // 사용자 조회
  getUser: async (userId: number): Promise<User> => {
    const response = await api.get(`/users/${userId}`);
    return response.data;
  },

  // 이메일로 사용자 조회
  getUserByEmail: async (email: string): Promise<User> => {
    const response = await api.get(`/users/email/${email}`);
    return response.data;
  },
};

// Bookmark API
export const bookmarkApi = {
  // 사용자의 북마크 목록 조회
  getUserBookmarks: async (userId: number, page = 0, size = 20): Promise<{ content: Bookmark[], totalElements: number, totalPages: number }> => {
    const response = await api.get(`/users/${userId}/bookmarks?page=${page}&size=${size}&sort=createdAt&direction=desc`);
    return response.data;
  },

  // 북마크 생성
  createBookmark: async (userId: number, bookmarkData: CreateBookmarkRequest): Promise<Bookmark> => {
    const response = await api.post(`/users/${userId}/bookmarks`, bookmarkData);
    return response.data;
  },

  // 북마크 상세 조회
  getBookmark: async (userId: number, bookmarkId: number): Promise<Bookmark> => {
    const response = await api.get(`/users/${userId}/bookmarks/${bookmarkId}`);
    return response.data;
  },

  // 즐겨찾기 북마크 조회
  getFavoriteBookmarks: async (userId: number, page = 0, size = 20): Promise<{ content: Bookmark[], totalElements: number, totalPages: number }> => {
    const response = await api.get(`/users/${userId}/bookmarks/favorites?page=${page}&size=${size}`);
    return response.data;
  },

  // 북마크 검색
  searchBookmarks: async (userId: number, keyword: string, page = 0, size = 20): Promise<{ content: Bookmark[], totalElements: number, totalPages: number }> => {
    const response = await api.get(`/users/${userId}/bookmarks/search?keyword=${encodeURIComponent(keyword)}&page=${page}&size=${size}`);
    return response.data;
  },

  // 북마크 수정
  updateBookmark: async (userId: number, bookmarkId: number, bookmarkData: Partial<CreateBookmarkRequest>): Promise<Bookmark> => {
    const response = await api.put(`/users/${userId}/bookmarks/${bookmarkId}`, bookmarkData);
    return response.data;
  },

  // 북마크 삭제
  deleteBookmark: async (userId: number, bookmarkId: number): Promise<void> => {
    await api.delete(`/users/${userId}/bookmarks/${bookmarkId}`);
  },
};

// Tag API
export const tagApi = {
  // 사용자의 태그 목록 조회
  getUserTags: async (userId: number): Promise<Tag[]> => {
    const response = await api.get(`/users/${userId}/tags`);
    return response.data;
  },

  // 태그 생성
  createTag: async (userId: number, name: string): Promise<Tag> => {
    const response = await api.post(`/users/${userId}/tags`, { name });
    return response.data;
  },

  // 태그별 북마크 조회
  getBookmarksByTag: async (userId: number, tagId: number, page = 0, size = 20): Promise<{ content: Bookmark[], totalElements: number, totalPages: number }> => {
    const response = await api.get(`/users/${userId}/bookmarks/tags/${tagId}?page=${page}&size=${size}`);
    return response.data;
  },
};

// Public API
export const publicApi = {
  // 공개 북마크 조회
  getPublicBookmarks: async (page = 0, size = 20): Promise<{ content: Bookmark[], totalElements: number, totalPages: number }> => {
    const response = await api.get(`/public/bookmarks?page=${page}&size=${size}`);
    return response.data;
  },

  // URL 메타데이터 추출
  extractMetadata: async (url: string): Promise<MetadataResponse> => {
    const response = await api.get(`/public/metadata?url=${encodeURIComponent(url)}`);
    return response.data;
  },
};

export default api;

