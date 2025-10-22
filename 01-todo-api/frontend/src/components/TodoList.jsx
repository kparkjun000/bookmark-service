import { useState, useEffect } from "react";
import { todoAPI, categoryAPI } from "../services/api";
import TodoItem from "./TodoItem";
import TodoForm from "./TodoForm";
import "./Todo.css";

function TodoList() {
  const [todos, setTodos] = useState([]);
  const [categories, setCategories] = useState([]);
  const [loading, setLoading] = useState(true);
  const [showForm, setShowForm] = useState(false);
  const [filter, setFilter] = useState("all"); // all, completed, pending
  const [searchTerm, setSearchTerm] = useState("");

  useEffect(() => {
    fetchTodos();
    fetchCategories();
  }, []);

  const fetchTodos = async () => {
    try {
      setLoading(true);
      const response = await todoAPI.getAllTodos();
      setTodos(response.data.content || response.data);
    } catch (error) {
      console.error("할일 목록 조회 실패:", error);
      alert("할일 목록을 불러오는데 실패했습니다.");
    } finally {
      setLoading(false);
    }
  };

  const fetchCategories = async () => {
    try {
      const response = await categoryAPI.getAllCategories();
      setCategories(response.data);
    } catch (error) {
      console.error("카테고리 목록 조회 실패:", error);
    }
  };

  const handleAddTodo = async (todoData) => {
    try {
      await todoAPI.createTodo(todoData);
      await fetchTodos();
      setShowForm(false);
    } catch (error) {
      console.error("할일 생성 실패:", error);
      alert("할일 생성에 실패했습니다.");
    }
  };

  const handleUpdateTodo = async (id, todoData) => {
    try {
      await todoAPI.updateTodo(id, todoData);
      await fetchTodos();
    } catch (error) {
      console.error("할일 수정 실패:", error);
      alert("할일 수정에 실패했습니다.");
    }
  };

  const handleDeleteTodo = async (id) => {
    if (!window.confirm("정말 삭제하시겠습니까?")) {
      return;
    }

    try {
      await todoAPI.deleteTodo(id);
      await fetchTodos();
    } catch (error) {
      console.error("할일 삭제 실패:", error);
      alert("할일 삭제에 실패했습니다.");
    }
  };

  const handleToggleComplete = async (todo) => {
    await handleUpdateTodo(todo.id, {
      ...todo,
      completed: !todo.completed,
    });
  };

  const filteredTodos = todos.filter((todo) => {
    // Filter by status
    if (filter === "completed" && !todo.completed) return false;
    if (filter === "pending" && todo.completed) return false;

    // Filter by search term
    if (searchTerm) {
      const searchLower = searchTerm.toLowerCase();
      return (
        todo.title.toLowerCase().includes(searchLower) ||
        (todo.description &&
          todo.description.toLowerCase().includes(searchLower))
      );
    }

    return true;
  });

  const stats = {
    total: todos.length,
    completed: todos.filter((t) => t.completed).length,
    pending: todos.filter((t) => !t.completed).length,
  };

  if (loading) {
    return <div className="loading">로딩 중...</div>;
  }

  return (
    <div className="todo-list-container">
      <div className="todo-header">
        <h1>할 일 관리</h1>
        <button
          className="btn btn-primary"
          onClick={() => setShowForm(!showForm)}
        >
          {showForm ? "취소" : "+ 새 할일"}
        </button>
      </div>

      <div className="todo-stats">
        <div className="stat-card">
          <div className="stat-value">{stats.total}</div>
          <div className="stat-label">전체</div>
        </div>
        <div className="stat-card">
          <div className="stat-value">{stats.pending}</div>
          <div className="stat-label">진행중</div>
        </div>
        <div className="stat-card">
          <div className="stat-value">{stats.completed}</div>
          <div className="stat-label">완료</div>
        </div>
      </div>

      {showForm && (
        <div className="todo-form-wrapper">
          <TodoForm
            categories={categories}
            onSubmit={handleAddTodo}
            onCancel={() => setShowForm(false)}
          />
        </div>
      )}

      <div className="todo-filters">
        <div className="filter-buttons">
          <button
            className={`filter-btn ${filter === "all" ? "active" : ""}`}
            onClick={() => setFilter("all")}
          >
            전체
          </button>
          <button
            className={`filter-btn ${filter === "pending" ? "active" : ""}`}
            onClick={() => setFilter("pending")}
          >
            진행중
          </button>
          <button
            className={`filter-btn ${filter === "completed" ? "active" : ""}`}
            onClick={() => setFilter("completed")}
          >
            완료
          </button>
        </div>

        <div className="search-box">
          <input
            type="text"
            className="form-control"
            placeholder="검색..."
            value={searchTerm}
            onChange={(e) => setSearchTerm(e.target.value)}
          />
        </div>
      </div>

      <div className="todo-items">
        {filteredTodos.length === 0 ? (
          <div className="empty-state">
            <h3>할일이 없습니다</h3>
            <p>새로운 할일을 추가해보세요!</p>
          </div>
        ) : (
          filteredTodos.map((todo) => (
            <TodoItem
              key={todo.id}
              todo={todo}
              categories={categories}
              onToggleComplete={handleToggleComplete}
              onUpdate={handleUpdateTodo}
              onDelete={handleDeleteTodo}
            />
          ))
        )}
      </div>
    </div>
  );
}

export default TodoList;
