import { useState } from "react";
import "./App.css";
import Header from "./components/layout/Header";
import { LoginModal, SignupModal } from "./components/common/AuthModal";
import MainPage from "./pages/MainPage";
/* ─── 샘플 데이터 (백엔드 Restaurant API 연동 시 교체 예정) ─── */

function App() {
  const [modal, setModal] = useState(null);
  const [user, setUser] = useState(null);

  const handleLoginSuccess = (userData) => {
    setUser(userData);
    setModal(null);
  };

  const handleLogout = () => {
    setUser(null);
    setModal(null);
  };

  return (
    <div>
      <Header
        isLoggedIn={!!user}
        user={user}
        onLoginClick={() => setModal("login")}
        onSignupClick={() => setModal("signup")}
        onLogoutClick={handleLogout}
      />

      <MainPage />

      {modal === "login" && (
        <LoginModal
          onClose={() => setModal(null)}
          onSuccess={handleLoginSuccess}
          onSwitchToSignup={() => setModal("signup")}
        />
      )}

      {modal === "signup" && (
        <SignupModal
          onClose={() => setModal(null)}
          onSuccess={handleLoginSuccess}
          onSwitchToLogin={() => setModal("login")}
        />
      )}
    </div>
  );
}

export default App;