import React, { useState, useEffect } from 'react';
import { User, Bookmark, Tag } from '../types/api';
import { userApi, bookmarkApi, tagApi, publicApi } from '../services/api';
import './App.css';

const App: React.FC = () => {
  const [users, setUsers] = useState<User[]>([]);
  const [selectedUser, setSelectedUser] = useState<User | null>(null);
  const [bookmarks, setBookmarks] = useState<Bookmark[]>([]);
  const [tags, setTags] = useState<Tag[]>([]);
  const [publicBookmarks, setPublicBookmarks] = useState<Bookmark[]>([]);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState<string | null>(null);
  const [activeTab, setActiveTab] = useState<'users' | 'bookmarks' | 'public'>('users');

  // 사용자 목록 로드
  useEffect(() => {
    loadUsers();
    loadPublicBookmarks();
  }, []);

  const loadUsers = async () => {
    try {
      setLoading(true);
      const userList = await userApi.getAllUsers();
      setUsers(userList);
      if (userList.length > 0 && !selectedUser) {
        setSelectedUser(userList[0]);
      }
    } catch (err) {
      setError('사용자 목록을 불러오는데 실패했습니다.');
      console.error(err);
    } finally {
      setLoading(false);
    }
  };

  const loadPublicBookmarks = async () => {
    try {
      const response = await publicApi.getPublicBookmarks(0, 10);
      setPublicBookmarks(response.content);
    } catch (err) {
      console.error('공개 북마크 로드 실패:', err);
    }
  };

  const loadUserBookmarks = async (userId: number) => {
    try {
      setLoading(true);
      const response = await bookmarkApi.getUserBookmarks(userId, 0, 20);
      setBookmarks(response.content);
    } catch (err) {
      setError('북마크 목록을 불러오는데 실패했습니다.');
      console.error(err);
    } finally {
      setLoading(false);
    }
  };

  const loadUserTags = async (userId: number) => {
    try {
      const tagList = await tagApi.getUserTags(userId);
      setTags(tagList);
    } catch (err) {
      console.error('태그 로드 실패:', err);
    }
  };

  const handleUserSelect = (user: User) => {
    setSelectedUser(user);
    loadUserBookmarks(user.id);
    loadUserTags(user.id);
  };

  const formatDate = (dateString: string) => {
    return new Date(dateString).toLocaleDateString('ko-KR', {
      year: 'numeric',
      month: 'short',
      day: 'numeric',
      hour: '2-digit',
      minute: '2-digit'
    });
  };

  const openBookmark = (url: string) => {
    window.open(url, '_blank');
  };

  if (loading) {
    return (
      <div className="app">
        <div className="loading">로딩 중...</div>
      </div>
    );
  }

  return (
    <div className="app">
      <header className="app-header">
        <h1>📚 Bookmark Service</h1>
        <p>URL 북마크 관리 및 태그 서비스</p>
      </header>

      <nav className="tab-nav">
        <button 
          className={activeTab === 'users' ? 'active' : ''} 
          onClick={() => setActiveTab('users')}
        >
          👥 사용자 관리
        </button>
        <button 
          className={activeTab === 'bookmarks' ? 'active' : ''} 
          onClick={() => setActiveTab('bookmarks')}
        >
          🔖 북마크 관리
        </button>
        <button 
          className={activeTab === 'public' ? 'active' : ''} 
          onClick={() => setActiveTab('public')}
        >
          🌐 공개 북마크
        </button>
      </nav>

      {error && (
        <div className="error">
          ❌ {error}
          <button onClick={() => setError(null)}>닫기</button>
        </div>
      )}

      <main className="app-main">
        {activeTab === 'users' && (
          <div className="users-section">
            <h2>사용자 목록</h2>
            <div className="user-list">
              {users.map(user => (
                <div 
                  key={user.id} 
                  className={`user-card ${selectedUser?.id === user.id ? 'selected' : ''}`}
                  onClick={() => handleUserSelect(user)}
                >
                  <div className="user-info">
                    <h3>{user.name}</h3>
                    <p>{user.email}</p>
                    <small>가입일: {formatDate(user.createdAt)}</small>
                  </div>
                </div>
              ))}
            </div>
          </div>
        )}

        {activeTab === 'bookmarks' && selectedUser && (
          <div className="bookmarks-section">
            <h2>{selectedUser.name}의 북마크</h2>
            <div className="bookmark-list">
              {bookmarks.map(bookmark => (
                <div key={bookmark.id} className="bookmark-card">
                  <div className="bookmark-header">
                    <h3 onClick={() => openBookmark(bookmark.url)}>
                      {bookmark.title || bookmark.url}
                    </h3>
                    <div className="bookmark-badges">
                      {bookmark.isFavorite && <span className="badge favorite">⭐ 즐겨찾기</span>}
                      {bookmark.isPublic && <span className="badge public">🌐 공개</span>}
                    </div>
                  </div>
                  {bookmark.description && (
                    <p className="bookmark-description">{bookmark.description}</p>
                  )}
                  <div className="bookmark-meta">
                    <p className="bookmark-url" onClick={() => openBookmark(bookmark.url)}>
                      🔗 {bookmark.url}
                    </p>
                    {bookmark.siteName && <p>📍 {bookmark.siteName}</p>}
                    <p>📅 {formatDate(bookmark.createdAt)}</p>
                  </div>
                  {bookmark.tags.length > 0 && (
                    <div className="bookmark-tags">
                      {bookmark.tags.map(tag => (
                        <span key={tag.id} className="tag">{tag.name}</span>
                      ))}
                    </div>
                  )}
                </div>
              ))}
            </div>
          </div>
        )}

        {activeTab === 'public' && (
          <div className="public-section">
            <h2>공개 북마크</h2>
            <div className="bookmark-list">
              {publicBookmarks.map(bookmark => (
                <div key={bookmark.id} className="bookmark-card">
                  <div className="bookmark-header">
                    <h3 onClick={() => openBookmark(bookmark.url)}>
                      {bookmark.title || bookmark.url}
                    </h3>
                    <div className="bookmark-badges">
                      {bookmark.isFavorite && <span className="badge favorite">⭐ 즐겨찾기</span>}
                      <span className="badge public">🌐 공개</span>
                    </div>
                  </div>
                  {bookmark.description && (
                    <p className="bookmark-description">{bookmark.description}</p>
                  )}
                  <div className="bookmark-meta">
                    <p className="bookmark-url" onClick={() => openBookmark(bookmark.url)}>
                      🔗 {bookmark.url}
                    </p>
                    {bookmark.siteName && <p>📍 {bookmark.siteName}</p>}
                    <p>📅 {formatDate(bookmark.createdAt)}</p>
                  </div>
                  {bookmark.tags.length > 0 && (
                    <div className="bookmark-tags">
                      {bookmark.tags.map(tag => (
                        <span key={tag.id} className="tag">{tag.name}</span>
                      ))}
                    </div>
                  )}
                </div>
              ))}
            </div>
          </div>
        )}
      </main>

      <footer className="app-footer">
        <p>
          🔗 <a href="https://zerobase-bookmark-service-0aab4ffd66ec.herokuapp.com/swagger-ui.html" target="_blank" rel="noopener noreferrer">
            Swagger UI
          </a> | 
          📊 <a href="https://github.com/kparkjun000/bookmark-service" target="_blank" rel="noopener noreferrer">
            GitHub
          </a>
        </p>
      </footer>
    </div>
  );
};

export default App;

