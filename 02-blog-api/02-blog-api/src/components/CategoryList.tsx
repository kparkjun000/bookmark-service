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
  const [isFirstLoad, setIsFirstLoad] = useState(true);
  const [showForm, setShowForm] = useState(false);
  const [formData, setFormData] = useState<CreateCategoryRequest>({
    name: "",
    description: "",
  });

  // 미리 정의된 카테고리 목록
  const predefinedCategories = [
    { name: "기술", description: "프로그래밍, 개발, IT 관련 내용" },
    { name: "일상", description: "일상생활, 개인적인 이야기" },
    { name: "여행", description: "여행 경험, 추천 장소" },
    { name: "음식", description: "맛집, 요리, 음식 리뷰" },
    { name: "독서", description: "책 리뷰, 독서 경험" },
    { name: "영화", description: "영화 리뷰, 영화 추천" },
    { name: "운동", description: "운동, 건강, 피트니스" },
  ];

  // 카테고리 목록 로드
  const loadCategories = async () => {
    try {
      setLoading(true);
      if (isFirstLoad) {
        setError(null);
      }
      const data = await categoryService.getAll();
      setCategories(data);
      setIsFirstLoad(false);
    } catch (err) {
      if (!isFirstLoad) {
        setError("카테고리를 불러오는데 실패했습니다.");
      }
      console.error("Error loading categories:", err);
    } finally {
      setLoading(false);
    }
  };

  // 카테고리 생성
  const handleCreateCategory = async (e: React.FormEvent) => {
    e.preventDefault();

    // 폼 검증
    if (!formData.name.trim()) {
      setError("카테고리 이름을 입력해주세요.");
      return;
    }

    try {
      setError(null);
      console.log("카테고리 생성 시도:", formData);
      const newCategory = await categoryService.create(formData);
      console.log("카테고리 생성 성공:", newCategory);
      setFormData({ name: "", description: "" });
      setShowForm(false);
      await loadCategories(); // 목록 새로고침
    } catch (err: any) {
      const errorMessage =
        err?.response?.data?.message ||
        err?.message ||
        "카테고리 생성에 실패했습니다.";
      setError(`카테고리 생성 실패: ${errorMessage}`);
      console.error("Error creating category:", err);
      console.error("Error details:", err?.response?.data);
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
    return (
      <div className="category-list">
        <div className="header">
          <h2>카테고리 관리</h2>
        </div>
        <div className="loading">카테고리를 불러오는 중...</div>
      </div>
    );
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
            <label htmlFor="category-select">카테고리 선택 (선택사항)</label>
            <select
              id="category-select"
              value={formData.name}
              onChange={(e) => {
                const selectedCategory = predefinedCategories.find(
                  (cat) => cat.name === e.target.value
                );
                setFormData({
                  name: e.target.value,
                  description: selectedCategory?.description || "",
                });
              }}
            >
              <option value="">카테고리를 선택하세요</option>
              {predefinedCategories.map((category) => (
                <option key={category.name} value={category.name}>
                  {category.name}
                </option>
              ))}
            </select>
          </div>
          <div className="form-group">
            <label htmlFor="name">카테고리 이름 *</label>
            <input
              type="text"
              id="name"
              value={formData.name}
              onChange={(e) =>
                setFormData({ ...formData, name: e.target.value })
              }
              placeholder="직접 입력하거나 위에서 선택하세요"
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
