import { useState } from "react";
import { categoryAPI } from "../services/api";
import "./CategoryManager.css";

function CategoryManager({ categories, onCategoryAdded }) {
  const [showForm, setShowForm] = useState(false);
  const [formData, setFormData] = useState({
    name: "",
    description: "",
    color: "#4F46E5",
  });
  const [loading, setLoading] = useState(false);

  const handleChange = (e) => {
    setFormData({
      ...formData,
      [e.target.name]: e.target.value,
    });
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    setLoading(true);

    try {
      await categoryAPI.createCategory(formData);
      setFormData({ name: "", description: "", color: "#4F46E5" });
      setShowForm(false);
      onCategoryAdded();
      alert("카테고리가 추가되었습니다!");
    } catch (error) {
      console.error("카테고리 생성 실패:", error);
      alert("카테고리 생성에 실패했습니다.");
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="category-manager">
      <div className="category-header">
        <h4>카테고리 ({categories.length}개)</h4>
        <button
          className="btn btn-sm btn-secondary"
          onClick={() => setShowForm(!showForm)}
        >
          {showForm ? "취소" : "+ 카테고리 추가"}
        </button>
      </div>

      {showForm && (
        <form onSubmit={handleSubmit} className="category-form">
          <div className="form-row">
            <div className="form-group">
              <input
                type="text"
                name="name"
                className="form-control"
                placeholder="카테고리 이름 *"
                value={formData.name}
                onChange={handleChange}
                required
                maxLength={20}
              />
            </div>
            <div className="form-group">
              <input
                type="text"
                name="description"
                className="form-control"
                placeholder="설명 (선택)"
                value={formData.description}
                onChange={handleChange}
              />
            </div>
            <div className="form-group color-group">
              <input
                type="color"
                name="color"
                className="form-control-color"
                value={formData.color}
                onChange={handleChange}
                title="색상 선택"
              />
            </div>
            <button
              type="submit"
              className="btn btn-primary btn-sm"
              disabled={loading}
            >
              {loading ? "추가 중..." : "추가"}
            </button>
          </div>
        </form>
      )}

      <div className="category-list">
        {categories.map((category) => (
          <div key={category.id} className="category-badge">
            <span
              className="category-color"
              style={{ backgroundColor: category.color }}
            ></span>
            <span className="category-name">{category.name}</span>
            <span className="category-count">{category.todoCount || 0}</span>
          </div>
        ))}
      </div>
    </div>
  );
}

export default CategoryManager;

