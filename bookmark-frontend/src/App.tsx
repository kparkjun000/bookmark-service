import React, { useState, useEffect } from "react";

interface User {
  id: number;
  name: string;
  email: string;
  createdAt: string;
}

interface Bookmark {
  id: number;
  title: string;
  url: string;
  description?: string;
  siteName?: string;
  isFavorite: boolean;
  isPublic: boolean;
  createdAt: string;
  tags: Tag[];
}

interface Tag {
  id: number;
  name: string;
}

function App() {
  const [users, setUsers] = useState<User[]>([]);
  const [selectedUser, setSelectedUser] = useState<User | null>(null);
  const [bookmarks, setBookmarks] = useState<Bookmark[]>([]);
  const [tags, setTags] = useState<Tag[]>([]);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState<string | null>(null);
  const [activeTab, setActiveTab] = useState<"users" | "bookmarks" | "add">(
    "users"
  );

  // 새 북마크 추가 폼
  const [newBookmark, setNewBookmark] = useState({
    url: "",
    title: "",
    description: "",
    siteName: "",
    isFavorite: false,
    isPublic: false,
    tagIds: [] as number[],
  });

  useEffect(() => {
    loadUsers();
  }, []);

  const loadUsers = async () => {
    try {
      setLoading(true);
      setError(null);
      console.log("사용자 목록 로딩 시작...");

      const response = await fetch(
        "https://zerobase-bookmark-service-0aab4ffd66ec.herokuapp.com/api/users"
      );

      console.log("응답 상태:", response.status);

      if (!response.ok) {
        throw new Error(`HTTP ${response.status}: ${response.statusText}`);
      }

      const data = await response.json();
      console.log("사용자 데이터:", data);

      setUsers(data);
      if (data.length > 0) {
        setSelectedUser(data[0]);
        loadUserBookmarks(data[0].id);
        loadUserTags(data[0].id);
      }
    } catch (err) {
      console.error("사용자 로드 에러:", err);
      setError(`사용자 목록을 불러오는데 실패했습니다: ${err}`);
    } finally {
      setLoading(false);
    }
  };

  const loadUserBookmarks = async (userId: number) => {
    try {
      const response = await fetch(
        `https://zerobase-bookmark-service-0aab4ffd66ec.herokuapp.com/api/users/${userId}/bookmarks?page=0&size=20&sort=createdAt&direction=desc`
      );
      const data = await response.json();
      setBookmarks(data.content || []);
    } catch (err) {
      setError("북마크 목록을 불러오는데 실패했습니다.");
    }
  };

  const loadUserTags = async (userId: number) => {
    try {
      const response = await fetch(
        `https://zerobase-bookmark-service-0aab4ffd66ec.herokuapp.com/api/users/${userId}/tags`
      );
      const data = await response.json();
      setTags(data);
    } catch (err) {
      console.error("태그 로드 실패:", err);
    }
  };

  const handleUserSelect = (user: User) => {
    setSelectedUser(user);
    loadUserBookmarks(user.id);
    loadUserTags(user.id);
  };

  const extractMetadata = async (url: string) => {
    try {
      const response = await fetch(
        `https://zerobase-bookmark-service-0aab4ffd66ec.herokuapp.com/api/public/metadata?url=${encodeURIComponent(
          url
        )}`
      );
      const data = await response.json();
      setNewBookmark((prev) => ({
        ...prev,
        title: data.title || "",
        description: data.description || "",
        siteName: data.siteName || "",
      }));
    } catch (err) {
      console.error("메타데이터 추출 실패:", err);
    }
  };

  const addBookmark = async () => {
    if (!selectedUser || !newBookmark.url) return;

    try {
      setLoading(true);
      const response = await fetch(
        `https://zerobase-bookmark-service-0aab4ffd66ec.herokuapp.com/api/users/${selectedUser.id}/bookmarks`,
        {
          method: "POST",
          headers: {
            "Content-Type": "application/json",
          },
          body: JSON.stringify({
            url: newBookmark.url,
            title: newBookmark.title,
            description: newBookmark.description,
            siteName: newBookmark.siteName,
            isFavorite: newBookmark.isFavorite,
            isPublic: newBookmark.isPublic,
            tagIds: newBookmark.tagIds,
          }),
        }
      );

      if (response.ok) {
        setNewBookmark({
          url: "",
          title: "",
          description: "",
          siteName: "",
          isFavorite: false,
          isPublic: false,
          tagIds: [],
        });
        loadUserBookmarks(selectedUser.id);
        setActiveTab("bookmarks");
        alert("북마크가 성공적으로 추가되었습니다!");
      } else {
        throw new Error("북마크 추가 실패");
      }
    } catch (err) {
      setError("북마크 추가에 실패했습니다.");
    } finally {
      setLoading(false);
    }
  };

  const formatDate = (dateString: string) => {
    return new Date(dateString).toLocaleDateString("ko-KR", {
      year: "numeric",
      month: "short",
      day: "numeric",
      hour: "2-digit",
      minute: "2-digit",
    });
  };

  const openBookmark = (url: string) => {
    window.open(url, "_blank");
  };

  if (loading) {
    return (
      <div
        style={{
          minHeight: "100vh",
          background: "linear-gradient(135deg, #667eea 0%, #764ba2 100%)",
          display: "flex",
          alignItems: "center",
          justifyContent: "center",
          color: "white",
          fontSize: "1.2rem",
        }}
      >
        로딩 중...
      </div>
    );
  }

  return (
    <div
      style={{
        minHeight: "100vh",
        background: "linear-gradient(135deg, #667eea 0%, #764ba2 100%)",
        color: "white",
        fontFamily: "Arial, sans-serif",
      }}
    >
      <header
        style={{
          textAlign: "center",
          padding: "2rem",
          background: "rgba(255, 255, 255, 0.1)",
          backdropFilter: "blur(10px)",
        }}
      >
        <h1 style={{ fontSize: "2.5rem", margin: "0 0 0.5rem 0" }}>
          📚 Bookmark Service
        </h1>
        <p style={{ fontSize: "1.1rem", margin: 0, opacity: 0.9 }}>
          URL 북마크 관리 및 태그 서비스
        </p>
      </header>

      <nav
        style={{
          display: "flex",
          justifyContent: "center",
          gap: "1rem",
          padding: "1rem",
          background: "rgba(255, 255, 255, 0.1)",
        }}
      >
        <button
          onClick={() => setActiveTab("users")}
          style={{
            padding: "0.75rem 1.5rem",
            border: "none",
            borderRadius: "25px",
            background:
              activeTab === "users" ? "white" : "rgba(255, 255, 255, 0.2)",
            color: activeTab === "users" ? "#667eea" : "white",
            fontWeight: "600",
            cursor: "pointer",
          }}
        >
          👥 사용자 관리
        </button>
        <button
          onClick={() => setActiveTab("bookmarks")}
          style={{
            padding: "0.75rem 1.5rem",
            border: "none",
            borderRadius: "25px",
            background:
              activeTab === "bookmarks" ? "white" : "rgba(255, 255, 255, 0.2)",
            color: activeTab === "bookmarks" ? "#667eea" : "white",
            fontWeight: "600",
            cursor: "pointer",
          }}
        >
          🔖 북마크 관리
        </button>
        <button
          onClick={() => setActiveTab("add")}
          style={{
            padding: "0.75rem 1.5rem",
            border: "none",
            borderRadius: "25px",
            background:
              activeTab === "add" ? "white" : "rgba(255, 255, 255, 0.2)",
            color: activeTab === "add" ? "#667eea" : "white",
            fontWeight: "600",
            cursor: "pointer",
          }}
        >
          ➕ 북마크 추가
        </button>
      </nav>

      {error && (
        <div
          style={{
            background: "#ff6b6b",
            color: "white",
            padding: "1rem",
            margin: "1rem",
            borderRadius: "8px",
            textAlign: "center",
          }}
        >
          ❌ {error}
          <button
            onClick={() => setError(null)}
            style={{
              background: "rgba(255, 255, 255, 0.2)",
              border: "none",
              color: "white",
              padding: "0.5rem 1rem",
              borderRadius: "4px",
              cursor: "pointer",
              marginLeft: "1rem",
            }}
          >
            닫기
          </button>
        </div>
      )}

      <main
        style={{
          maxWidth: "1200px",
          margin: "0 auto",
          padding: "2rem",
        }}
      >
        {activeTab === "users" && (
          <div>
            <h2 style={{ marginBottom: "1.5rem", textAlign: "center" }}>
              사용자 목록 ({users.length}명)
            </h2>

            {users.length === 0 && !loading && (
              <div
                style={{
                  textAlign: "center",
                  padding: "2rem",
                  background: "rgba(255, 255, 255, 0.1)",
                  borderRadius: "12px",
                  marginBottom: "1rem",
                }}
              >
                <p>사용자 데이터를 불러오는 중입니다...</p>
                <button
                  onClick={loadUsers}
                  style={{
                    background: "rgba(255, 255, 255, 0.2)",
                    border: "none",
                    color: "white",
                    padding: "0.75rem 1.5rem",
                    borderRadius: "25px",
                    fontWeight: "600",
                    cursor: "pointer",
                    marginTop: "1rem",
                  }}
                >
                  🔄 다시 시도
                </button>
              </div>
            )}

            <div
              style={{
                display: "grid",
                gridTemplateColumns: "repeat(auto-fill, minmax(300px, 1fr))",
                gap: "1rem",
              }}
            >
              {users.map((user) => (
                <div
                  key={user.id}
                  onClick={() => handleUserSelect(user)}
                  style={{
                    background:
                      selectedUser?.id === user.id
                        ? "#667eea"
                        : "rgba(255, 255, 255, 0.95)",
                    color: selectedUser?.id === user.id ? "white" : "#333",
                    borderRadius: "12px",
                    padding: "1.5rem",
                    cursor: "pointer",
                    transition: "all 0.3s ease",
                    boxShadow: "0 4px 15px rgba(0, 0, 0, 0.1)",
                  }}
                >
                  <h3 style={{ margin: "0 0 0.5rem 0", fontSize: "1.3rem" }}>
                    {user.name}
                  </h3>
                  <p style={{ margin: "0 0 0.5rem 0", opacity: 0.8 }}>
                    {user.email}
                  </p>
                  <small style={{ opacity: 0.7 }}>
                    가입일: {formatDate(user.createdAt)}
                  </small>
                </div>
              ))}
            </div>
          </div>
        )}

        {activeTab === "bookmarks" && selectedUser && (
          <div>
            <h2 style={{ marginBottom: "1.5rem", textAlign: "center" }}>
              {selectedUser.name}의 북마크 ({bookmarks.length}개)
            </h2>
            <div
              style={{
                display: "grid",
                gridTemplateColumns: "repeat(auto-fill, minmax(400px, 1fr))",
                gap: "1.5rem",
              }}
            >
              {bookmarks.map((bookmark) => (
                <div
                  key={bookmark.id}
                  style={{
                    background: "rgba(255, 255, 255, 0.95)",
                    color: "#333",
                    borderRadius: "12px",
                    padding: "1.5rem",
                    boxShadow: "0 4px 15px rgba(0, 0, 0, 0.1)",
                    transition: "all 0.3s ease",
                  }}
                >
                  <div
                    style={{
                      display: "flex",
                      justifyContent: "space-between",
                      alignItems: "flex-start",
                      marginBottom: "1rem",
                    }}
                  >
                    <h3
                      onClick={() => openBookmark(bookmark.url)}
                      style={{
                        margin: 0,
                        fontSize: "1.2rem",
                        cursor: "pointer",
                        color: "#667eea",
                        flex: 1,
                        marginRight: "1rem",
                      }}
                    >
                      {bookmark.title || bookmark.url}
                    </h3>
                    <div
                      style={{
                        display: "flex",
                        gap: "0.5rem",
                        flexWrap: "wrap",
                      }}
                    >
                      {bookmark.isFavorite && (
                        <span
                          style={{
                            background: "#ffd93d",
                            color: "#333",
                            padding: "0.25rem 0.75rem",
                            borderRadius: "12px",
                            fontSize: "0.8rem",
                            fontWeight: "600",
                          }}
                        >
                          ⭐ 즐겨찾기
                        </span>
                      )}
                      {bookmark.isPublic && (
                        <span
                          style={{
                            background: "#4ecdc4",
                            color: "white",
                            padding: "0.25rem 0.75rem",
                            borderRadius: "12px",
                            fontSize: "0.8rem",
                            fontWeight: "600",
                          }}
                        >
                          🌐 공개
                        </span>
                      )}
                    </div>
                  </div>

                  {bookmark.description && (
                    <p
                      style={{
                        color: "#666",
                        margin: "0 0 1rem 0",
                        lineHeight: "1.5",
                      }}
                    >
                      {bookmark.description}
                    </p>
                  )}

                  <div style={{ marginBottom: "1rem" }}>
                    <p
                      onClick={() => openBookmark(bookmark.url)}
                      style={{
                        margin: "0.25rem 0",
                        fontSize: "0.9rem",
                        color: "#667eea",
                        cursor: "pointer",
                        textDecoration: "underline",
                      }}
                    >
                      🔗 {bookmark.url}
                    </p>
                    {bookmark.siteName && (
                      <p
                        style={{
                          margin: "0.25rem 0",
                          fontSize: "0.9rem",
                          color: "#888",
                        }}
                      >
                        📍 {bookmark.siteName}
                      </p>
                    )}
                    <p
                      style={{
                        margin: "0.25rem 0",
                        fontSize: "0.9rem",
                        color: "#888",
                      }}
                    >
                      📅 {formatDate(bookmark.createdAt)}
                    </p>
                  </div>

                  {bookmark.tags.length > 0 && (
                    <div
                      style={{
                        display: "flex",
                        flexWrap: "wrap",
                        gap: "0.5rem",
                      }}
                    >
                      {bookmark.tags.map((tag) => (
                        <span
                          key={tag.id}
                          style={{
                            background: "#e3f2fd",
                            color: "#1976d2",
                            padding: "0.25rem 0.75rem",
                            borderRadius: "12px",
                            fontSize: "0.8rem",
                            fontWeight: "500",
                          }}
                        >
                          {tag.name}
                        </span>
                      ))}
                    </div>
                  )}
                </div>
              ))}
            </div>
          </div>
        )}

        {activeTab === "add" && selectedUser && (
          <div
            style={{
              maxWidth: "600px",
              margin: "0 auto",
              background: "rgba(255, 255, 255, 0.95)",
              color: "#333",
              borderRadius: "12px",
              padding: "2rem",
            }}
          >
            <h2 style={{ marginBottom: "1.5rem", textAlign: "center" }}>
              새 북마크 추가
            </h2>

            <div style={{ marginBottom: "1rem" }}>
              <label
                style={{
                  display: "block",
                  marginBottom: "0.5rem",
                  fontWeight: "600",
                }}
              >
                URL *
              </label>
              <input
                type="url"
                value={newBookmark.url}
                onChange={(e) => {
                  setNewBookmark((prev) => ({ ...prev, url: e.target.value }));
                  if (e.target.value) {
                    extractMetadata(e.target.value);
                  }
                }}
                placeholder="https://example.com"
                style={{
                  width: "100%",
                  padding: "0.75rem",
                  border: "1px solid #ddd",
                  borderRadius: "8px",
                  fontSize: "1rem",
                }}
              />
            </div>

            <div style={{ marginBottom: "1rem" }}>
              <label
                style={{
                  display: "block",
                  marginBottom: "0.5rem",
                  fontWeight: "600",
                }}
              >
                제목
              </label>
              <input
                type="text"
                value={newBookmark.title}
                onChange={(e) =>
                  setNewBookmark((prev) => ({ ...prev, title: e.target.value }))
                }
                placeholder="북마크 제목 (자동 추출됨)"
                style={{
                  width: "100%",
                  padding: "0.75rem",
                  border: "1px solid #ddd",
                  borderRadius: "8px",
                  fontSize: "1rem",
                }}
              />
            </div>

            <div style={{ marginBottom: "1rem" }}>
              <label
                style={{
                  display: "block",
                  marginBottom: "0.5rem",
                  fontWeight: "600",
                }}
              >
                설명
              </label>
              <textarea
                value={newBookmark.description}
                onChange={(e) =>
                  setNewBookmark((prev) => ({
                    ...prev,
                    description: e.target.value,
                  }))
                }
                placeholder="북마크 설명 (자동 추출됨)"
                rows={3}
                style={{
                  width: "100%",
                  padding: "0.75rem",
                  border: "1px solid #ddd",
                  borderRadius: "8px",
                  fontSize: "1rem",
                  resize: "vertical",
                }}
              />
            </div>

            <div style={{ marginBottom: "1rem" }}>
              <label
                style={{ display: "flex", alignItems: "center", gap: "0.5rem" }}
              >
                <input
                  type="checkbox"
                  checked={newBookmark.isFavorite}
                  onChange={(e) =>
                    setNewBookmark((prev) => ({
                      ...prev,
                      isFavorite: e.target.checked,
                    }))
                  }
                />
                <span>⭐ 즐겨찾기</span>
              </label>
            </div>

            <div style={{ marginBottom: "1rem" }}>
              <label
                style={{ display: "flex", alignItems: "center", gap: "0.5rem" }}
              >
                <input
                  type="checkbox"
                  checked={newBookmark.isPublic}
                  onChange={(e) =>
                    setNewBookmark((prev) => ({
                      ...prev,
                      isPublic: e.target.checked,
                    }))
                  }
                />
                <span>🌐 공개</span>
              </label>
            </div>

            <button
              onClick={addBookmark}
              disabled={!newBookmark.url || loading}
              style={{
                width: "100%",
                background: "#667eea",
                color: "white",
                border: "none",
                padding: "1rem",
                borderRadius: "8px",
                fontSize: "1rem",
                fontWeight: "600",
                cursor: newBookmark.url && !loading ? "pointer" : "not-allowed",
                opacity: newBookmark.url && !loading ? 1 : 0.6,
              }}
            >
              {loading ? "추가 중..." : "북마크 추가"}
            </button>
          </div>
        )}
      </main>

      <footer
        style={{
          textAlign: "center",
          padding: "2rem",
          opacity: 0.8,
        }}
      >
        <p>
          🔗{" "}
          <a
            href="https://zerobase-bookmark-service-0aab4ffd66ec.herokuapp.com/swagger-ui.html"
            target="_blank"
            rel="noopener noreferrer"
            style={{
              color: "white",
              textDecoration: "none",
              fontWeight: "600",
            }}
          >
            Swagger UI
          </a>{" "}
          | 📊{" "}
          <a
            href="https://github.com/kparkjun000/bookmark-service"
            target="_blank"
            rel="noopener noreferrer"
            style={{
              color: "white",
              textDecoration: "none",
              fontWeight: "600",
            }}
          >
            GitHub
          </a>
        </p>
      </footer>
    </div>
  );
}

export default App;
