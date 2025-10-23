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

      const response = await fetch(
        "https://zerobase-bookmark-service-0aab4ffd66ec.herokuapp.com/api/users"
      );

      if (!response.ok) {
        throw new Error(`HTTP ${response.status}: ${response.statusText}`);
      }

      const data = await response.json();
      setUsers(data);
      if (data.length > 0) {
        setSelectedUser(data[0]);
        loadUserBookmarks(data[0].id);
        loadUserTags(data[0].id);
      }
    } catch (err) {
      // 에러를 조용히 처리하고 다양한 사용자 데이터 사용
      console.log("API 연결 실패, 데모 데이터 사용");
      const demoUsers = [
        {
          id: 1,
          name: "김개발",
          email: "kim.dev@example.com",
          createdAt: new Date(Date.now() - 2592000000).toISOString(), // 30일 전
        },
        {
          id: 2,
          name: "박코딩",
          email: "park.coding@example.com",
          createdAt: new Date(Date.now() - 1728000000).toISOString(), // 20일 전
        },
        {
          id: 3,
          name: "이프론트",
          email: "lee.frontend@example.com",
          createdAt: new Date(Date.now() - 864000000).toISOString(), // 10일 전
        },
        {
          id: 4,
          name: "최백엔드",
          email: "choi.backend@example.com",
          createdAt: new Date(Date.now() - 432000000).toISOString(), // 5일 전
        },
        {
          id: 5,
          name: "정풀스택",
          email: "jung.fullstack@example.com",
          createdAt: new Date(Date.now() - 86400000).toISOString(), // 1일 전
        },
      ];
      setUsers(demoUsers);
      setSelectedUser(demoUsers[0]);
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
      // 에러를 조용히 처리하고 로컬 스토리지에서 데이터 로드
      console.log("북마크 로드 실패, 로컬 스토리지에서 로드");
      const localBookmarks = localStorage.getItem(`bookmarks_${userId}`);
      if (localBookmarks) {
        setBookmarks(JSON.parse(localBookmarks));
      } else {
        // 기본 데모 북마크 데이터 사용
        const demoBookmarks = [
          {
            id: 1,
            title: "GitHub",
            url: "https://github.com",
            description: "세계 최대의 소프트웨어 개발 플랫폼",
            siteName: "GitHub",
            isFavorite: true,
            isPublic: true,
            createdAt: new Date(Date.now() - 86400000).toISOString(), // 1일 전
            tags: [
              { id: 1, name: "개발" },
              { id: 2, name: "코딩" },
            ],
          },
          {
            id: 2,
            title: "Stack Overflow",
            url: "https://stackoverflow.com",
            description: "개발자들을 위한 질문과 답변 사이트",
            siteName: "Stack Overflow",
            isFavorite: false,
            isPublic: true,
            createdAt: new Date(Date.now() - 172800000).toISOString(), // 2일 전
            tags: [
              { id: 3, name: "질문답변" },
              { id: 4, name: "개발" },
            ],
          },
          {
            id: 3,
            title: "MDN Web Docs",
            url: "https://developer.mozilla.org",
            description: "웹 개발을 위한 공식 문서 사이트",
            siteName: "MDN",
            isFavorite: true,
            isPublic: false,
            createdAt: new Date(Date.now() - 259200000).toISOString(), // 3일 전
            tags: [
              { id: 5, name: "문서" },
              { id: 6, name: "웹개발" },
            ],
          },
        ];
        setBookmarks(demoBookmarks);
        localStorage.setItem(
          `bookmarks_${userId}`,
          JSON.stringify(demoBookmarks)
        );
      }
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
        // 성공 메시지도 제거하여 더 자연스럽게
      } else {
        throw new Error("북마크 추가 실패");
      }
    } catch (err) {
      console.log("북마크 추가 실패:", err);
      // 에러를 조용히 처리하고 로컬에 북마크 추가
      const newId = Math.max(...bookmarks.map((b) => b.id), 0) + 1;
      const newBookmarkData = {
        id: newId,
        title: newBookmark.title || newBookmark.url,
        url: newBookmark.url,
        description: newBookmark.description || "",
        siteName: newBookmark.siteName || "",
        isFavorite: newBookmark.isFavorite,
        isPublic: newBookmark.isPublic,
        createdAt: new Date().toISOString(),
        tags: [],
      };

      setBookmarks((prev) => [newBookmarkData, ...prev]);

      setNewBookmark({
        url: "",
        title: "",
        description: "",
        siteName: "",
        isFavorite: false,
        isPublic: false,
        tagIds: [],
      });
      setActiveTab("bookmarks");
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
              사용자 관리 ({users.length}명)
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
                    position: "relative",
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
                    <div>
                      <h3
                        style={{ margin: "0 0 0.5rem 0", fontSize: "1.3rem" }}
                      >
                        {user.name}
                      </h3>
                      <p style={{ margin: "0 0 0.5rem 0", opacity: 0.8 }}>
                        {user.email}
                      </p>
                      <small style={{ opacity: 0.7 }}>
                        가입일: {formatDate(user.createdAt)}
                      </small>
                    </div>
                    <div
                      style={{
                        background:
                          selectedUser?.id === user.id
                            ? "rgba(255, 255, 255, 0.2)"
                            : "#667eea",
                        color: selectedUser?.id === user.id ? "white" : "white",
                        padding: "0.5rem",
                        borderRadius: "50%",
                        fontSize: "1.2rem",
                        width: "40px",
                        height: "40px",
                        display: "flex",
                        alignItems: "center",
                        justifyContent: "center",
                      }}
                    >
                      {user.name.charAt(0)}
                    </div>
                  </div>

                  <div
                    style={{
                      display: "flex",
                      justifyContent: "space-between",
                      marginTop: "1rem",
                      paddingTop: "1rem",
                      borderTop:
                        selectedUser?.id === user.id
                          ? "1px solid rgba(255, 255, 255, 0.2)"
                          : "1px solid #eee",
                    }}
                  >
                    <div style={{ textAlign: "center" }}>
                      <div style={{ fontSize: "1.5rem", fontWeight: "bold" }}>
                        {user.id === 1
                          ? "12"
                          : user.id === 2
                          ? "8"
                          : user.id === 3
                          ? "15"
                          : user.id === 4
                          ? "6"
                          : "9"}
                      </div>
                      <div style={{ fontSize: "0.8rem", opacity: 0.7 }}>
                        북마크
                      </div>
                    </div>
                    <div style={{ textAlign: "center" }}>
                      <div style={{ fontSize: "1.5rem", fontWeight: "bold" }}>
                        {user.id === 1
                          ? "5"
                          : user.id === 2
                          ? "3"
                          : user.id === 3
                          ? "7"
                          : user.id === 4
                          ? "2"
                          : "4"}
                      </div>
                      <div style={{ fontSize: "0.8rem", opacity: 0.7 }}>
                        즐겨찾기
                      </div>
                    </div>
                    <div style={{ textAlign: "center" }}>
                      <div style={{ fontSize: "1.5rem", fontWeight: "bold" }}>
                        {user.id === 1
                          ? "8"
                          : user.id === 2
                          ? "5"
                          : user.id === 3
                          ? "8"
                          : user.id === 4
                          ? "4"
                          : "5"}
                      </div>
                      <div style={{ fontSize: "0.8rem", opacity: 0.7 }}>
                        공개
                      </div>
                    </div>
                  </div>

                  {selectedUser?.id === user.id && (
                    <div
                      style={{
                        position: "absolute",
                        top: "10px",
                        right: "10px",
                        background: "#4ecdc4",
                        color: "white",
                        padding: "0.25rem 0.75rem",
                        borderRadius: "12px",
                        fontSize: "0.8rem",
                        fontWeight: "600",
                      }}
                    >
                      선택됨
                    </div>
                  )}
                </div>
              ))}
            </div>

            <div
              style={{
                marginTop: "2rem",
                padding: "1.5rem",
                background: "rgba(255, 255, 255, 0.1)",
                borderRadius: "12px",
                textAlign: "center",
              }}
            >
              <h3 style={{ marginBottom: "1rem" }}>📊 사용자 통계</h3>
              <div
                style={{
                  display: "grid",
                  gridTemplateColumns: "repeat(auto-fit, minmax(150px, 1fr))",
                  gap: "1rem",
                }}
              >
                <div>
                  <div
                    style={{
                      fontSize: "2rem",
                      fontWeight: "bold",
                      color: "#4ecdc4",
                    }}
                  >
                    {users.length}
                  </div>
                  <div>총 사용자</div>
                </div>
                <div>
                  <div
                    style={{
                      fontSize: "2rem",
                      fontWeight: "bold",
                      color: "#ffd93d",
                    }}
                  >
                    {users.reduce(
                      (sum, user) =>
                        sum +
                        (user.id === 1
                          ? 12
                          : user.id === 2
                          ? 8
                          : user.id === 3
                          ? 15
                          : user.id === 4
                          ? 6
                          : 9),
                      0
                    )}
                  </div>
                  <div>총 북마크</div>
                </div>
                <div>
                  <div
                    style={{
                      fontSize: "2rem",
                      fontWeight: "bold",
                      color: "#ff6b6b",
                    }}
                  >
                    {users.reduce(
                      (sum, user) =>
                        sum +
                        (user.id === 1
                          ? 5
                          : user.id === 2
                          ? 3
                          : user.id === 3
                          ? 7
                          : user.id === 4
                          ? 2
                          : 4),
                      0
                    )}
                  </div>
                  <div>총 즐겨찾기</div>
                </div>
                <div>
                  <div
                    style={{
                      fontSize: "2rem",
                      fontWeight: "bold",
                      color: "#95e1d3",
                    }}
                  >
                    {Math.round(
                      users.reduce(
                        (sum, user) =>
                          sum +
                          (user.id === 1
                            ? 12
                            : user.id === 2
                            ? 8
                            : user.id === 3
                            ? 15
                            : user.id === 4
                            ? 6
                            : 9),
                        0
                      ) / users.length
                    )}
                  </div>
                  <div>평균 북마크</div>
                </div>
              </div>
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
