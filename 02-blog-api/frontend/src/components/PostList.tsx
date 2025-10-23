import React, { useState, useEffect } from "react";
import {
  postService,
  categoryService,
  type Post,
  type CreatePostRequest,
  type Category,
} from "../services/api";

const PostList: React.FC = () => {
  const [posts, setPosts] = useState<Post[]>([]);
  const [categories, setCategories] = useState<Category[]>([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);
  const [showForm, setShowForm] = useState(false);
  const [formData, setFormData] = useState<CreatePostRequest>({
    title: "",
    content: "",
    summary: "",
    author: "",
    status: "DRAFT",
  });

  // 게시글 목록 로드
  const loadPosts = async () => {
    try {
      setLoading(true);
      const data = await postService.getAll();
      setPosts(data);
      setError(null);
    } catch (err) {
      setError("게시글을 불러오는데 실패했습니다.");
      console.error("Error loading posts:", err);
    } finally {
      setLoading(false);
    }
  };

  // 카테고리 목록 로드
  const loadCategories = async () => {
    try {
      const data = await categoryService.getAll();
      setCategories(data);
    } catch (err) {
      console.error("Error loading categories:", err);
    }
  };

  // 게시글 생성
  const handleCreatePost = async (e: React.FormEvent) => {
    e.preventDefault();
    try {
      await postService.create(formData);
      setFormData({
        title: "",
        content: "",
        summary: "",
        author: "",
        status: "DRAFT",
      });
      setShowForm(false);
      loadPosts(); // 목록 새로고침
    } catch (err) {
      setError("게시글 생성에 실패했습니다.");
      console.error("Error creating post:", err);
    }
  };

  // 게시글 삭제
  const handleDeletePost = async (id: number) => {
    if (window.confirm("정말로 이 게시글을 삭제하시겠습니까?")) {
      try {
        await postService.delete(id);
        loadPosts(); // 목록 새로고침
      } catch (err) {
        setError("게시글 삭제에 실패했습니다.");
        console.error("Error deleting post:", err);
      }
    }
  };

  useEffect(() => {
    loadPosts();
    loadCategories();
  }, []);

  if (loading) {
    return <div className="loading">게시글을 불러오는 중...</div>;
  }

  return (
    <div className="post-list">
      <div className="header">
        <h2>게시글 관리</h2>
        <button
          className="btn btn-primary"
          onClick={() => setShowForm(!showForm)}
        >
          {showForm ? "취소" : "새 게시글"}
        </button>
      </div>

      {error && <div className="error">{error}</div>}

      {showForm && (
        <form onSubmit={handleCreatePost} className="form">
          <div className="form-group">
            <label htmlFor="title">제목</label>
            <input
              type="text"
              id="title"
              value={formData.title}
              onChange={(e) =>
                setFormData({ ...formData, title: e.target.value })
              }
              required
            />
          </div>
          <div className="form-group">
            <label htmlFor="author">작성자</label>
            <input
              type="text"
              id="author"
              value={formData.author}
              onChange={(e) =>
                setFormData({ ...formData, author: e.target.value })
              }
              required
            />
          </div>
          <div className="form-group">
            <label htmlFor="summary">요약</label>
            <textarea
              id="summary"
              value={formData.summary}
              onChange={(e) =>
                setFormData({ ...formData, summary: e.target.value })
              }
              rows={2}
            />
          </div>
          <div className="form-group">
            <label htmlFor="content">내용</label>
            <textarea
              id="content"
              value={formData.content}
              onChange={(e) =>
                setFormData({ ...formData, content: e.target.value })
              }
              rows={5}
              required
            />
          </div>
          <div className="form-group">
            <label htmlFor="status">상태</label>
            <select
              id="status"
              value={formData.status}
              onChange={(e) =>
                setFormData({
                  ...formData,
                  status: e.target.value as "DRAFT" | "PUBLISHED",
                })
              }
            >
              <option value="DRAFT">초안</option>
              <option value="PUBLISHED">게시됨</option>
            </select>
          </div>
          <div className="form-group">
            <label htmlFor="category">카테고리 (선택사항)</label>
            <select
              id="category"
              value={formData.categoryId || ""}
              onChange={(e) =>
                setFormData({
                  ...formData,
                  categoryId: e.target.value
                    ? Number(e.target.value)
                    : undefined,
                })
              }
            >
              <option value="">카테고리 선택</option>
              {categories.map((category) => (
                <option key={category.id} value={category.id}>
                  {category.name}
                </option>
              ))}
            </select>
          </div>
          <button type="submit" className="btn btn-primary">
            생성
          </button>
        </form>
      )}

      <div className="posts">
        {posts.length === 0 ? (
          <p>등록된 게시글이 없습니다.</p>
        ) : (
          posts.map((post) => (
            <div key={post.id} className="post-item">
              <div className="post-info">
                <h3>{post.title}</h3>
                <p className="author">작성자: {post.author}</p>
                {post.summary && <p className="summary">{post.summary}</p>}
                <div className="content">{post.content}</div>
                <div className="meta">
                  <span
                    className={`status status-${post.status.toLowerCase()}`}
                  >
                    {post.status === "DRAFT" ? "초안" : "게시됨"}
                  </span>
                  {post.category && (
                    <span className="category">
                      카테고리: {post.category.name}
                    </span>
                  )}
                  <small>
                    생성일: {new Date(post.createdAt).toLocaleDateString()}
                  </small>
                </div>
              </div>
              <button
                className="btn btn-danger btn-sm"
                onClick={() => handleDeletePost(post.id)}
              >
                삭제
              </button>
            </div>
          ))
        )}
      </div>
    </div>
  );
};

export default PostList;
