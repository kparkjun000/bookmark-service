import { useState } from "react";

function TodoForm({ todo, categories, onSubmit, onCancel }) {
  const [formData, setFormData] = useState({
    title: todo?.title || "",
    description: todo?.description || "",
    priority: todo?.priority || "MEDIUM",
    categoryId: todo?.categoryId || "",
    dueDate: todo?.dueDate || "",
    completed: todo?.completed || false,
  });

  const handleChange = (e) => {
    const { name, value, type, checked } = e.target;
    setFormData({
      ...formData,
      [name]: type === "checkbox" ? checked : value,
    });
  };

  const handleSubmit = (e) => {
    e.preventDefault();

    const submitData = {
      title: formData.title,
      description: formData.description || null,
      priority: formData.priority,
      categoryId: formData.categoryId ? parseInt(formData.categoryId) : null,
      dueDate: formData.dueDate ? `${formData.dueDate}T00:00:00` : null,
    };

    // 수정 모드일 때만 completed 필드 추가
    if (todo) {
      submitData.completed = formData.completed;
    }

    onSubmit(submitData);
  };

  return (
    <form onSubmit={handleSubmit} className="todo-form card">
      <h3>{todo ? "할일 수정" : "새 할일 추가"}</h3>

      <div className="form-group">
        <label className="form-label">제목 *</label>
        <input
          type="text"
          name="title"
          className="form-control"
          placeholder="할일 제목을 입력하세요"
          value={formData.title}
          onChange={handleChange}
          required
        />
      </div>

      <div className="form-group">
        <label className="form-label">설명</label>
        <textarea
          name="description"
          className="form-control"
          placeholder="할일에 대한 설명을 입력하세요"
          value={formData.description}
          onChange={handleChange}
          rows="3"
        />
      </div>

      <div className="form-row">
        <div className="form-group">
          <label className="form-label">우선순위</label>
          <select
            name="priority"
            className="form-control"
            value={formData.priority}
            onChange={handleChange}
          >
            <option value="LOW">낮음</option>
            <option value="MEDIUM">중간</option>
            <option value="HIGH">높음</option>
          </select>
        </div>

        <div className="form-group">
          <label className="form-label">카테고리</label>
          <select
            name="categoryId"
            className="form-control"
            value={formData.categoryId}
            onChange={handleChange}
          >
            <option value="">카테고리 선택</option>
            {categories.map((category) => (
              <option key={category.id} value={category.id}>
                {category.name}
              </option>
            ))}
          </select>
        </div>
      </div>

      <div className="form-group">
        <label className="form-label">마감일</label>
        <input
          type="date"
          name="dueDate"
          className="form-control"
          value={formData.dueDate}
          onChange={handleChange}
        />
      </div>

      {todo && (
        <div className="form-group">
          <label className="checkbox-label">
            <input
              type="checkbox"
              name="completed"
              checked={formData.completed}
              onChange={handleChange}
            />
            <span>완료</span>
          </label>
        </div>
      )}

      <div className="form-actions">
        <button type="submit" className="btn btn-primary">
          {todo ? "수정" : "추가"}
        </button>
        <button type="button" className="btn btn-secondary" onClick={onCancel}>
          취소
        </button>
      </div>
    </form>
  );
}

export default TodoForm;
