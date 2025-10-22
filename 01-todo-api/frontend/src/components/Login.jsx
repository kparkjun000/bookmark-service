import { useState } from "react";
import { useNavigate, Link } from "react-router-dom";
import { authAPI } from "../services/api";
import { setToken, setUser } from "../utils/auth";
import "./Auth.css";

function Login() {
  const navigate = useNavigate();
  const [formData, setFormData] = useState({
    username: "",
    password: "",
  });
  const [error, setError] = useState("");
  const [loading, setLoading] = useState(false);

  const handleChange = (e) => {
    setFormData({
      ...formData,
      [e.target.name]: e.target.value,
    });
    setError("");
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    setLoading(true);
    setError("");

    try {
      const response = await authAPI.login(formData);
      const { token, ...user } = response.data;

      setToken(token);
      setUser(user);

      navigate("/");
    } catch (err) {
      setError(
        err.response?.data?.message ||
          "로그인에 실패했습니다. 다시 시도해주세요."
      );
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="auth-container">
      <div className="auth-card card">
        <h2 className="auth-title">로그인</h2>
        <p className="auth-subtitle">계정에 로그인하세요</p>

        {error && <div className="alert alert-error">{error}</div>}

        <form onSubmit={handleSubmit}>
          <div className="form-group">
            <label className="form-label">사용자명</label>
            <input
              type="text"
              name="username"
              className="form-control"
              placeholder="사용자명을 입력하세요"
              value={formData.username}
              onChange={handleChange}
              required
            />
          </div>

          <div className="form-group">
            <label className="form-label">비밀번호</label>
            <input
              type="password"
              name="password"
              className="form-control"
              placeholder="비밀번호를 입력하세요"
              value={formData.password}
              onChange={handleChange}
              required
            />
          </div>

          <button
            type="submit"
            className="btn btn-primary btn-block"
            disabled={loading}
          >
            {loading ? "로그인 중..." : "로그인"}
          </button>
        </form>

        <div className="auth-footer">
          <p>
            계정이 없으신가요?{" "}
            <Link to="/signup" className="auth-link">
              회원가입
            </Link>
          </p>
        </div>
      </div>
    </div>
  );
}

export default Login;
