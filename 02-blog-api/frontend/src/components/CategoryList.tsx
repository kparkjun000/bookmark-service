import React, { useState, useEffect } from "react";
import {
  categoryService,
  type Category,
  type CreateCategoryRequest,
} from "../services/api";

const CategoryList: React.FC = () => {
  const [categories, setCategories] = useState<Category[]>([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);
  const [showForm, setShowForm] = useState(false);
  const [formData, setFormData] = useState<CreateCategoryRequest>({
    name: "",
    description: "",
  });

  // 카테고리 목록 로드
  const loadCategories = async () => {
    try {
      setLoading(true);
      const data = await categoryService.getAll();
      setCategories(data);
      setError(null);
    } catch (err) {
      setError("카테고리를 불러오는데 실패했습니다.");
      console.error("Error loading categories:", err);
    } finally {
      setLoading(false);
    }
  };

  // 카테고리 생성
  const handleCreateCategory = async (e: React.FormEvent) => {
    e.preventDefault();
    try {
      await categoryService.create(formData);
      setFormData({ name: "", description: "" });
      setShowForm(false);
      loadCategories(); // 목록 새로고침
    } catch (err) {
      setError("카테고리 생성에 실패했습니다.");
      console.error("Error creating category:", err);
    }
  };

  // 카테고리 삭제
  const handleDeleteCategory = async (id: number) => {
    if (window.confirm("정말로 이 카테고리를 삭제하시겠습니까?")) {
      try {
        await categoryService.delete(id);
        loadCategories(); // 목록 새로고침
      } catch (err) {
        setError("카테고리 삭제에 실패했습니다.");
        console.error("Error deleting category:", err);
      }
    }
  };

  useEffect(() => {
    loadCategories();
  }, []);

  if (loading) {
    return <div className="loading">카테고리를 불러오는 중...</div>;
  }

  return (
    <div className="category-list">
      <div className="header">
        <h2>카테고리 관리</h2>
        <button
          className="btn btn-primary"
          onClick={() => setShowForm(!showForm)}
        >
          {showForm ? "취소" : "새 카테고리"}
        </button>
      </div>

      {error && <div className="error">{error}</div>}

      {showForm && (
        <form onSubmit={handleCreateCategory} className="form">
          <div className="form-group">
            <label htmlFor="name">카테고리 이름 *</label>
            <input
              type="text"
              id="name"
              value={formData.name}
              onChange={(e) =>
                setFormData({ ...formData, name: e.target.value })
              }
              placeholder="카테고리 이름을 입력하세요"
              required
            />
          </div>
          <div className="form-group">
            <label htmlFor="description">설명</label>
            <textarea
              id="description"
              value={formData.description}
              onChange={(e) =>
                setFormData({ ...formData, description: e.target.value })
              }
              rows={3}
              placeholder="카테고리에 대한 설명을 입력하세요"
            />
          </div>
          <button type="submit" className="btn btn-primary">
            생성
          </button>
        </form>
      )}

      <div className="categories">
        {categories.length === 0 ? (
          <p>등록된 카테고리가 없습니다.</p>
        ) : (
          categories.map((category) => (
            <div key={category.id} className="category-item">
              <div className="category-info">
                <h3>{category.name}</h3>
                {category.description && <p>{category.description}</p>}
                <small>
                  생성일: {new Date(category.createdAt).toLocaleDateString()}
                </small>
              </div>
              <button
                className="btn btn-danger btn-sm"
                onClick={() => handleDeleteCategory(category.id)}
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

export default CategoryList;
