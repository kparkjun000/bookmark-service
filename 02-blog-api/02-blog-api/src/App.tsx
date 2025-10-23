import { useState } from "react";
import CategoryList from "./components/CategoryList";
import PostList from "./components/PostList";
import { healthService } from "./services/api";
import "./App.css";

function App() {
  const [activeTab, setActiveTab] = useState<"categories" | "posts" | "health">(
    "categories"
  );
  const [healthStatus, setHealthStatus] = useState<any>(null);

  const checkHealth = async () => {
    try {
      const status = await healthService.check();
      setHealthStatus(status);
    } catch (error) {
      setHealthStatus({ error: "Health check failed" });
    }
  };

  return (
    <div className="app">
      <header className="app-header">
        <h1>📝 Blog Management System</h1>
        <p>블로그 게시글 및 카테고리 관리 시스템</p>
      </header>

      <nav className="nav-tabs">
        <button
          className={activeTab === "categories" ? "active" : ""}
          onClick={() => setActiveTab("categories")}
        >
          📂 카테고리
        </button>
        <button
          className={activeTab === "posts" ? "active" : ""}
          onClick={() => setActiveTab("posts")}
        >
          📄 게시글
        </button>
        <button
          className={activeTab === "health" ? "active" : ""}
          onClick={() => {
            setActiveTab("health");
            checkHealth();
          }}
        >
          🔍 시스템 상태
        </button>
      </nav>

      <main className="main-content">
        {activeTab === "categories" && <CategoryList />}
        {activeTab === "posts" && <PostList />}
        {activeTab === "health" && (
          <div className="health-check">
            <h2>시스템 상태 확인</h2>
            <button className="btn btn-primary" onClick={checkHealth}>
              상태 확인
            </button>
            {healthStatus && (
              <div className="health-status">
                <pre>{JSON.stringify(healthStatus, null, 2)}</pre>
              </div>
            )}
          </div>
        )}
      </main>

      <footer className="app-footer">
        <p>Powered by React + TypeScript + Vite | API: Heroku</p>
      </footer>
    </div>
  );
}

export default App;
