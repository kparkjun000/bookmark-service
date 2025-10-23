import React, { useState, useEffect } from "react";
import { useAuth } from "../contexts/AuthContext";
import { authAPI } from "../services/api";
import { User } from "../types/auth";

const Dashboard: React.FC = () => {
  const { user, logout } = useAuth();
  const [allUsers, setAllUsers] = useState<User[]>([]);
  const [isLoading, setIsLoading] = useState(false);
  const [error, setError] = useState("");

  const fetchAllUsers = async () => {
    if (!user?.roles.includes("ADMIN")) return;

    setIsLoading(true);
    setError("");

    try {
      const response = await authAPI.getAllUsers();
      if (response.success) {
        setAllUsers(response.data);
      } else {
        setError(response.message);
      }
    } catch (err: any) {
      setError(
        err.response?.data?.message || "사용자 목록을 가져오는데 실패했습니다."
      );
    } finally {
      setIsLoading(false);
    }
  };

  useEffect(() => {
    fetchAllUsers();
  }, [user]);

  const handleLogout = () => {
    logout();
  };

  return (
    <div style={{ maxWidth: "800px", margin: "0 auto", padding: "20px" }}>
      <div
        style={{
          display: "flex",
          justifyContent: "space-between",
          alignItems: "center",
          marginBottom: "30px",
        }}
      >
        <h1>대시보드</h1>
        <button
          onClick={handleLogout}
          style={{
            padding: "8px 16px",
            backgroundColor: "#dc3545",
            color: "white",
            border: "none",
            borderRadius: "4px",
            cursor: "pointer",
          }}
        >
          로그아웃
        </button>
      </div>

      <div
        style={{
          marginBottom: "30px",
          padding: "20px",
          backgroundColor: "#f8f9fa",
          borderRadius: "8px",
        }}
      >
        <h2>내 정보</h2>
        <p>
          <strong>사용자명:</strong> {user?.username}
        </p>
        <p>
          <strong>이메일:</strong> {user?.email}
        </p>
        <p>
          <strong>역할:</strong> {user?.roles.join(", ")}
        </p>
      </div>

      {user?.roles.includes("ADMIN") && (
        <div>
          <div
            style={{
              display: "flex",
              justifyContent: "space-between",
              alignItems: "center",
              marginBottom: "20px",
            }}
          >
            <h2>전체 사용자 목록</h2>
            <button
              onClick={fetchAllUsers}
              disabled={isLoading}
              style={{
                padding: "8px 16px",
                backgroundColor: "#007bff",
                color: "white",
                border: "none",
                borderRadius: "4px",
                cursor: isLoading ? "not-allowed" : "pointer",
                opacity: isLoading ? 0.6 : 1,
              }}
            >
              {isLoading ? "새로고침 중..." : "새로고침"}
            </button>
          </div>

          {error && (
            <div style={{ color: "red", marginBottom: "15px" }}>{error}</div>
          )}

          {allUsers.length > 0 ? (
            <div style={{ overflowX: "auto" }}>
              <table
                style={{
                  width: "100%",
                  borderCollapse: "collapse",
                  border: "1px solid #ddd",
                }}
              >
                <thead>
                  <tr style={{ backgroundColor: "#f8f9fa" }}>
                    <th
                      style={{
                        padding: "12px",
                        textAlign: "left",
                        border: "1px solid #ddd",
                      }}
                    >
                      ID
                    </th>
                    <th
                      style={{
                        padding: "12px",
                        textAlign: "left",
                        border: "1px solid #ddd",
                      }}
                    >
                      사용자명
                    </th>
                    <th
                      style={{
                        padding: "12px",
                        textAlign: "left",
                        border: "1px solid #ddd",
                      }}
                    >
                      이메일
                    </th>
                    <th
                      style={{
                        padding: "12px",
                        textAlign: "left",
                        border: "1px solid #ddd",
                      }}
                    >
                      역할
                    </th>
                  </tr>
                </thead>
                <tbody>
                  {allUsers.map((user) => (
                    <tr key={user.id}>
                      <td style={{ padding: "12px", border: "1px solid #ddd" }}>
                        {user.id}
                      </td>
                      <td style={{ padding: "12px", border: "1px solid #ddd" }}>
                        {user.username}
                      </td>
                      <td style={{ padding: "12px", border: "1px solid #ddd" }}>
                        {user.email}
                      </td>
                      <td style={{ padding: "12px", border: "1px solid #ddd" }}>
                        {user.roles.join(", ")}
                      </td>
                    </tr>
                  ))}
                </tbody>
              </table>
            </div>
          ) : (
            <p>사용자가 없습니다.</p>
          )}
        </div>
      )}

      <div
        style={{
          marginTop: "30px",
          padding: "20px",
          backgroundColor: "#e9ecef",
          borderRadius: "8px",
        }}
      >
        <h3>API 테스트</h3>
        <p>Swagger UI에서 API를 테스트해보세요:</p>
        <a
          href="https://warm-gorge-17774-fa28e4fd90c3.herokuapp.com/swagger-ui.html"
          target="_blank"
          rel="noopener noreferrer"
          style={{ color: "#007bff", textDecoration: "none" }}
        >
          🔗 Swagger UI 열기
        </a>
      </div>
    </div>
  );
};

export default Dashboard;
