import React from "react";

function App() {
  return (
    <div
      style={{
        minHeight: "100vh",
        background: "linear-gradient(135deg, #667eea 0%, #764ba2 100%)",
        display: "flex",
        flexDirection: "column",
        alignItems: "center",
        justifyContent: "center",
        color: "white",
        fontFamily: "Arial, sans-serif",
        padding: "20px",
      }}
    >
      <h1 style={{ fontSize: "3rem", marginBottom: "20px" }}>
        📚 Bookmark Service
      </h1>

      <p
        style={{
          fontSize: "1.2rem",
          marginBottom: "30px",
          textAlign: "center",
        }}
      >
        URL 북마크 관리 및 태그 서비스
      </p>

      <div
        style={{
          background: "rgba(255, 255, 255, 0.1)",
          padding: "20px",
          borderRadius: "10px",
          marginBottom: "20px",
          textAlign: "center",
        }}
      >
        <h2>✅ 프론트엔드 정상 작동!</h2>
        <p>React 앱이 성공적으로 로드되었습니다.</p>
      </div>

      <div
        style={{
          background: "rgba(255, 255, 255, 0.1)",
          padding: "20px",
          borderRadius: "10px",
          marginBottom: "20px",
          textAlign: "center",
        }}
      >
        <h3>🔗 관련 링크</h3>
        <p>
          <a
            href="https://zerobase-bookmark-service-0aab4ffd66ec.herokuapp.com/swagger-ui.html"
            target="_blank"
            rel="noopener noreferrer"
            style={{
              color: "white",
              textDecoration: "underline",
              marginRight: "20px",
            }}
          >
            Swagger UI
          </a>
          <a
            href="https://github.com/kparkjun000/bookmark-service"
            target="_blank"
            rel="noopener noreferrer"
            style={{ color: "white", textDecoration: "underline" }}
          >
            GitHub
          </a>
        </p>
      </div>

      <div
        style={{
          background: "rgba(255, 255, 255, 0.1)",
          padding: "20px",
          borderRadius: "10px",
          textAlign: "center",
        }}
      >
        <h3>🧪 API 테스트</h3>
        <button
          onClick={async () => {
            try {
              const response = await fetch(
                "https://zerobase-bookmark-service-0aab4ffd66ec.herokuapp.com/api/users"
              );
              const data = await response.json();
              alert(
                `API 연결 성공! 사용자 ${data.length}명: ${data
                  .map((u) => u.name)
                  .join(", ")}`
              );
            } catch (error) {
              alert(`API 연결 실패: ${error}`);
            }
          }}
          style={{
            background: "rgba(255, 255, 255, 0.2)",
            border: "none",
            color: "white",
            padding: "10px 20px",
            borderRadius: "5px",
            cursor: "pointer",
            fontSize: "16px",
          }}
        >
          🔄 API 연결 테스트
        </button>
      </div>
    </div>
  );
}

export default App;
