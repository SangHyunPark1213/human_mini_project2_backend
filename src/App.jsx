import { useState } from "react";
import "./App.css";
import Header from "./components/layout/Header";
import RestaurantCard from "./components/restaurant/RestaurantCard";
import { LoginModal, SignupModal } from "./components/common/AuthModal";
import RestaurantDetailModal from "./components/restaurant/RestaurantDetailModal";

/* ─── 샘플 데이터 ─── */
const ALL_RESTAURANTS = [
  {
    id: 1,
    thumbnail: "https://images.unsplash.com/photo-1555396273-367ea4eb4db5?w=600",
    name: "천안곱창맛집",
    category: "한식",
    average_rating: 4.5,
    location: "충남 천안시 동남구",
    description: "천안에서 유명한 곱창 맛집입니다. 신선한 재료로 매일 준비합니다.",
    popular_menu: "곱창볶음, 막창구이, 볶음밥",
    review_count: 128,
  },
  {
    id: 2,
    thumbnail: "https://images.unsplash.com/photo-1569050467447-ce54b3bbc37d?w=600",
    name: "스시오마카세",
    category: "일식",
    average_rating: 4.8,
    location: "충남 천안시 서북구",
    description: "셰프가 직접 엄선한 신선한 해산물로 만드는 정통 오마카세 스시.",
    popular_menu: "오마카세 코스, 연어초밥, 참치대뱃살",
    review_count: 87,
  },
  {
    id: 3,
    thumbnail: "https://images.unsplash.com/photo-1552566626-52f8b828add9?w=600",
    name: "파스타공방",
    category: "양식",
    average_rating: 4.3,
    location: "충남 천안시 동남구",
    description: "직접 뽑은 생면 파스타와 최고의 소스로 만드는 정통 이탈리안 요리.",
    popular_menu: "까르보나라, 봉골레, 토마토파스타",
    review_count: 54,
  },
];

function App() {
  // page: "main" | "login" | "signup"
  const [page, setPage] = useState("main");
  const [selectedRestaurant, setSelectedRestaurant] = useState(null);
  const [user, setUser] = useState(null);

  const handleLoginSuccess = (userData) => {
    setUser(userData);
    setPage("main");
  };

  const handleLogout = () => {
    setUser(null);
    setPage("main");
  };

  /* ── 로그인 페이지 ── */
  if (page === "login") {
    return (
      <LoginModal
        onClose={() => setPage("main")}
        onSuccess={handleLoginSuccess}
        onSwitchToSignup={() => setPage("signup")}
      />
    );
  }

  /* ── 회원가입 페이지 ── */
  if (page === "signup") {
    return (
      <SignupModal
        onClose={() => setPage("main")}
        onSuccess={handleLoginSuccess}
        onSwitchToLogin={() => setPage("login")}
      />
    );
  }

  /* ── 메인 페이지 ── */
  return (
    <div>
      <Header
        isLoggedIn={!!user}
        user={user}
        onLoginClick={() => setPage("login")}
        onSignupClick={() => setPage("signup")}
        onLogoutClick={handleLogout}
      />

      <main style={{ padding: "40px 32px" }}>
        <div style={{ maxWidth: 1200, margin: "0 auto" }}>
          <h2 style={{ fontSize: 22, fontWeight: 800, color: "#1a1a1a", marginBottom: 8 }}>
            천안 인기 맛집 🍽️
          </h2>
          <p style={{ color: "#888", fontSize: 14, marginBottom: 28 }}>
            총 {ALL_RESTAURANTS.length}개의 맛집
          </p>
          <div style={{
            display: "grid",
            gridTemplateColumns: "repeat(auto-fill, minmax(280px, 1fr))",
            gap: 24,
          }}>
            {ALL_RESTAURANTS.map((r) => (
              <RestaurantCard
                key={r.id}
                restaurant={r}
                onClick={setSelectedRestaurant}
              />
            ))}
          </div>
        </div>
      </main>

      {selectedRestaurant && (
        <RestaurantDetailModal
          restaurant={selectedRestaurant}
          onClose={() => setSelectedRestaurant(null)}
        />
      )}
    </div>
  );
}

export default App;
