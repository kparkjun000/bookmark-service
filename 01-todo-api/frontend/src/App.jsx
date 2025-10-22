import {
  BrowserRouter as Router,
  Routes,
  Route,
  Navigate,
} from "react-router-dom";
import Login from "./components/Login";
import Signup from "./components/Signup";
import TodoList from "./components/TodoList";
import Navbar from "./components/Navbar";
import PrivateRoute from "./components/PrivateRoute";
import { isAuthenticated } from "./utils/auth";
import "./styles/global.css";

function App() {
  return (
    <Router>
      <div className="app">
        {isAuthenticated() && <Navbar />}

        <Routes>
          <Route
            path="/login"
            element={
              isAuthenticated() ? <Navigate to="/" replace /> : <Login />
            }
          />
          <Route
            path="/signup"
            element={
              isAuthenticated() ? <Navigate to="/" replace /> : <Signup />
            }
          />
          <Route
            path="/"
            element={
              <PrivateRoute>
                <TodoList />
              </PrivateRoute>
            }
          />
          <Route path="*" element={<Navigate to="/" replace />} />
        </Routes>
      </div>
    </Router>
  );
}

export default App;
