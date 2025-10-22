import { useState } from "react";
import { useNavigate, Link } from "react-router-dom";
import { authAPI } from "../services/api";
import "./Auth.css";

function Signup() {
  const navigate = useNavigate();
  const [formData, setFormData] = useState({
    username: "",
    email: "",
    password: "",
    confirmPassword: "",
  });
  const [errors, setErrors] = useState({});
  const [loading, setLoading] = useState(false);

  const handleChange = (e) => {
    setFormData({
      ...formData,
      [e.target.name]: e.target.value,
    });
    // Clear error for this field
    if (errors[e.target.name]) {
      setErrors({
        ...errors,
        [e.target.name]: "",
      });
    }
  };

  const validate = () => {
    const newErrors = {};

    if (formData.username.length < 3) {
      newErrors.username = "사용자명은 3자 이상이어야 합니다.";
    }

    const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
    if (!emailRegex.test(formData.email)) {
      newErrors.email = "유효한 이메일 주소를 입력하세요.";
    }

    if (formData.password.length < 6) {
      newErrors.password = "비밀번호는 6자 이상이어야 합니다.";
    }

    if (formData.password !== formData.confirmPassword) {
      newErrors.confirmPassword = "비밀번호가 일치하지 않습니다.";
    }

    setErrors(newErrors);
    return Object.keys(newErrors).length === 0;
  };

  const handleSubmit = async (e) => {
    e.preventDefault();

    if (!validate()) {
      return;
    }

    setLoading(true);

    try {
      const { confirmPassword, ...signupData } = formData;
      await authAPI.signup(signupData);

      alert("회원가입이 완료되었습니다! 로그인해주세요.");
      navigate("/login");
    } catch (err) {
      setErrors({
        submit:
          err.response?.data?.message ||
          "회원가입에 실패했습니다. 다시 시도해주세요.",
      });
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="auth-container">
      <div className="auth-card card">
        <h2 className="auth-title">회원가입</h2>
        <p className="auth-subtitle">새 계정을 만드세요</p>

        {errors.submit && (
          <div className="alert alert-error">{errors.submit}</div>
        )}

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
            {errors.username && (
              <span className="error-message">{errors.username}</span>
            )}
          </div>

          <div className="form-group">
            <label className="form-label">이메일</label>
            <input
              type="email"
              name="email"
              className="form-control"
              placeholder="이메일을 입력하세요"
              value={formData.email}
              onChange={handleChange}
              required
            />
            {errors.email && (
              <span className="error-message">{errors.email}</span>
            )}
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
            {errors.password && (
              <span className="error-message">{errors.password}</span>
            )}
          </div>

          <div className="form-group">
            <label className="form-label">비밀번호 확인</label>
            <input
              type="password"
              name="confirmPassword"
              className="form-control"
              placeholder="비밀번호를 다시 입력하세요"
              value={formData.confirmPassword}
              onChange={handleChange}
              required
            />
            {errors.confirmPassword && (
              <span className="error-message">{errors.confirmPassword}</span>
            )}
          </div>

          <button
            type="submit"
            className="btn btn-primary btn-block"
            disabled={loading}
          >
            {loading ? "가입 중..." : "회원가입"}
          </button>
        </form>

        <div className="auth-footer">
          <p>
            이미 계정이 있으신가요?{" "}
            <Link to="/login" className="auth-link">
              로그인
            </Link>
          </p>
        </div>
      </div>
    </div>
  );
}

export default Signup;
