import { useState } from "react";
import TodoForm from "./TodoForm";

function TodoItem({ todo, categories, onToggleComplete, onUpdate, onDelete }) {
  const [isEditing, setIsEditing] = useState(false);

  const priorityColors = {
    HIGH: "high",
    MEDIUM: "medium",
    LOW: "low",
  };

  const priorityLabels = {
    HIGH: "높음",
    MEDIUM: "중간",
    LOW: "낮음",
  };

  const handleUpdate = async (todoData) => {
    await onUpdate(todo.id, todoData);
    setIsEditing(false);
  };

  const getCategoryName = (categoryId) => {
    const category = categories.find((c) => c.id === categoryId);
    return category ? category.name : "";
  };

  if (isEditing) {
    return (
      <div className="todo-item">
        <TodoForm
          todo={todo}
          categories={categories}
          onSubmit={handleUpdate}
          onCancel={() => setIsEditing(false)}
        />
      </div>
    );
  }

  return (
    <div className={`todo-item ${todo.completed ? "completed" : ""}`}>
      <div className="todo-checkbox">
        <input
          type="checkbox"
          checked={todo.completed}
          onChange={() => onToggleComplete(todo)}
        />
      </div>

      <div className="todo-content">
        <div className="todo-header-row">
          <h3 className="todo-title">{todo.title}</h3>
          <div className="todo-badges">
            <span className={`badge badge-${priorityColors[todo.priority]}`}>
              {priorityLabels[todo.priority]}
            </span>
            {todo.categoryId && (
              <span className="badge badge-category">
                {getCategoryName(todo.categoryId)}
              </span>
            )}
          </div>
        </div>

        {todo.description && (
          <p className="todo-description">{todo.description}</p>
        )}

        {todo.dueDate && (
          <div className="todo-due-date">
            📅 {new Date(todo.dueDate).toLocaleDateString("ko-KR")}
          </div>
        )}

        <div className="todo-actions">
          <button
            className="btn btn-sm btn-secondary"
            onClick={() => setIsEditing(true)}
          >
            수정
          </button>
          <button
            className="btn btn-sm btn-danger"
            onClick={() => onDelete(todo.id)}
          >
            삭제
          </button>
        </div>
      </div>
    </div>
  );
}

export default TodoItem;
