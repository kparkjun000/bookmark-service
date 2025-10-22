import { useNavigate } from "react-router-dom";
import { logout, getUser } from "../utils/auth";
import "./Navbar.css";

function Navbar() {
  const navigate = useNavigate();
  const user = getUser();

  const handleLogout = () => {
    logout();
    navigate("/login");
  };

  return (
    <nav className="navbar">
      <div className="navbar-container">
        <div className="navbar-brand">
          <h2>📝 Todo App</h2>
        </div>

        {user && (
          <div className="navbar-user">
            <span className="user-name">👤 {user.username}</span>
            <button className="btn btn-secondary btn-sm" onClick={handleLogout}>
              로그아웃
            </button>
          </div>
        )}
      </div>
    </nav>
  );
}

export default Navbar;
