import { useState, useMemo } from "react";
import "./App.css";
import Header from "./components/layout/Header";
import RestaurantCard from "./components/restaurant/RestaurantCard";
import { LoginModal, SignupModal } from "./components/common/AuthModal";
import RestaurantDetailModal from "./components/restaurant/RestaurantDetailModal";

/* ─── 샘플 데이터 (백엔드 Restaurant API 연동 시 교체 예정) ─── */
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

/* ─── 마이페이지 모달 (간단 인라인 구현) ─── */
const MyPageModal = ({ user, onClose }) => (
  <div
    className="mypage-backdrop"
    onClick={(e) => e.target === e.currentTarget && onClose()}
  >
    <div className="mypage-box">
      <button className="modal-close-btn" onClick={onClose} aria-label="닫기">
        <svg width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2">
          <line x1="18" y1="6" x2="6" y2="18"/><line x1="6" y1="6" x2="18" y2="18"/>
        </svg>
      </button>
      <div className="mypage-avatar">
        {user.nickname?.charAt(0).toUpperCase()}
      </div>
      <h2 className="mypage-name">{user.nickname}</h2>
      <p className="mypage-email">{user.email}</p>
      <div className="mypage-info-row">
        <span className="mypage-label">등급</span>
        <span className="mypage-value mypage-role">{user.role === "ROLE_ADMIN" ? "관리자" : "일반 회원"}</span>
      </div>
      <div className="mypage-info-row">
        <span className="mypage-label">회원번호</span>
        <span className="mypage-value">#{user.id}</span>
      </div>
      <button className="mypage-close-btn" onClick={onClose}>닫기</button>
    </div>
  </div>
);

/* ─── App ─── */
function App() {
  const [modal, setModal] = useState(null); // "login" | "signup" | "mypage" | null
  const [selectedRestaurant, setSelectedRestaurant] = useState(null);
  const [user, setUser] = useState(null); // MemberResponse: { id, email, nickname, role }
  const [search, setSearch] = useState({ location: "", keyword: "" });

  /* 로그인 성공 콜백 */
  const handleLoginSuccess = (userData) => {
    setUser(userData);
    setModal(null);
  };

  /* 로그아웃 */
  const handleLogout = () => {
    setUser(null);
    setModal(null);
  };

  /* 마이페이지 클릭 */
  const handleMyPageClick = () => {
    if (!user) {
      setModal("login");
    } else {
      setModal("mypage");
    }
  };

  /* 검색 필터링 */
  const filteredRestaurants = useMemo(() => {
    const { location, keyword } = search;
    if (!location && !keyword) return ALL_RESTAURANTS;

    return ALL_RESTAURANTS.filter((r) => {
      const matchLocation = !location || r.location.includes(location);
      const matchKeyword =
        !keyword ||
        r.name.includes(keyword) ||
        r.category.includes(keyword) ||
        r.popular_menu.includes(keyword) ||
        r.description.includes(keyword);
      return matchLocation && matchKeyword;
    });
  }, [search]);

  return (
    <div>
      <Header
        isLoggedIn={!!user}
        user={user}
        onLoginClick={() => setModal("login")}
        onSignupClick={() => setModal("signup")}
        onMyPageClick={handleMyPageClick}
        onLogoutClick={handleLogout}
        onSearch={setSearch}
      />

      <main style={{ padding: "40px 32px" }}>
        <div style={{ maxWidth: 1200, margin: "0 auto" }}>
          <h2 style={{ fontSize: 22, fontWeight: 800, color: "#1a1a1a", marginBottom: 8 }}>
            {search.keyword || search.location
              ? `"${[search.location, search.keyword].filter(Boolean).join(" / ")}" 검색 결과`
              : "천안 인기 맛집 🍽️"}
          </h2>
          <p style={{ color: "#888", fontSize: 14, marginBottom: 28 }}>
            {filteredRestaurants.length > 0
              ? `총 ${filteredRestaurants.length}개의 맛집`
              : "검색 결과가 없습니다."}
          </p>

          {filteredRestaurants.length > 0 ? (
            <div style={{
              display: "grid",
              gridTemplateColumns: "repeat(auto-fill, minmax(280px, 1fr))",
              gap: 24,
            }}>
              {filteredRestaurants.map((r) => (
                <RestaurantCard
                  key={r.id}
                  restaurant={r}
                  onClick={setSelectedRestaurant}
                />
              ))}
            </div>
          ) : (
            <div style={{
              textAlign: "center",
              padding: "60px 0",
              color: "#aaa",
              fontSize: 15,
            }}>
              😢 검색 결과가 없어요. 다른 키워드로 찾아보세요!
            </div>
          )}
        </div>
      </main>

      {/* 로그인 모달 */}
      {modal === "login" && (
        <LoginModal
          onClose={() => setModal(null)}
          onSuccess={handleLoginSuccess}
          onSwitchToSignup={() => setModal("signup")}
        />
      )}

      {/* 회원가입 모달 */}
      {modal === "signup" && (
        <SignupModal
          onClose={() => setModal(null)}
          onSuccess={handleLoginSuccess}
          onSwitchToLogin={() => setModal("login")}
        />
      )}

      {/* 마이페이지 모달 */}
      {modal === "mypage" && user && (
        <MyPageModal
          user={user}
          onClose={() => setModal(null)}
        />
      )}

      {/* 음식점 상세 모달 */}
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
