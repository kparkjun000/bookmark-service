// API 서비스 타입 정의
export interface User {
  id: number;
  email: string;
  name: string;
  createdAt: string;
  updatedAt: string;
}

export interface Tag {
  id: number;
  name: string;
  bookmarkCount: number;
  createdAt: string;
  updatedAt: string;
}

export interface Bookmark {
  id: number;
  url: string;
  title: string;
  description: string;
  imageUrl?: string;
  siteName?: string;
  isPublic: boolean;
  isFavorite: boolean;
  tags: Tag[];
  createdAt: string;
  updatedAt: string;
}

export interface CreateUserRequest {
  email: string;
  name: string;
}

export interface CreateBookmarkRequest {
  url: string;
  title?: string;
  description?: string;
  isPublic?: boolean;
  isFavorite?: boolean;
  tagNames?: string[];
}

export interface MetadataResponse {
  url: string;
  title: string;
  description: string;
  imageUrl?: string;
  siteName?: string;
}

