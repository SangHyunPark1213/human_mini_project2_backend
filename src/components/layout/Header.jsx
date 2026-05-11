import { useState } from "react";
import "./Header.css";

const Header = ({ onLoginClick, onSignupClick, onMyPageClick, onLogoutClick, isLoggedIn, user }) => {
  const [location, setLocation] = useState("");
  const [keyword, setKeyword] = useState("");

  const handleSearch = () => {
    if (keyword.trim() || location.trim()) {
      console.log("검색:", { location, keyword });
      // TODO: 검색 로직 연결
    }
  };

  const handleKeyDown = (e) => {
    if (e.key === "Enter") handleSearch();
  };

  return (
    <header className="header">
      <div className="header-inner">

        {/* 로고 */}
        <div className="logo-area">
          <span className="logo-icons">
            <svg xmlns="http://www.w3.org/2000/svg" width="22" height="22" viewBox="0 0 24 24" fill="none" stroke="#ff6b35" strokeWidth="2" strokeLinecap="round" strokeLinejoin="round">
              <path d="M20 10c0 4.993-5.539 10.193-7.399 11.799a1 1 0 0 1-1.202 0C9.539 20.193 4 14.993 4 10a8 8 0 0 1 16 0"/>
              <circle cx="12" cy="10" r="3"/>
            </svg>
            <svg xmlns="http://www.w3.org/2000/svg" width="22" height="22" viewBox="0 0 24 24" fill="none" stroke="#ff6b35" strokeWidth="2" strokeLinecap="round" strokeLinejoin="round">
              <path d="M15 11h.01"/>
              <path d="M11 15h.01"/>
              <path d="M16 16h.01"/>
              <path d="m2 16 20 6-6-20A20 20 0 0 0 2 16"/>
              <path d="M5.71 17.11a17.04 17.04 0 0 1 11.4-11.4"/>
            </svg>
          </span>
          <div className="logo-text-wrap">
            <span className="logo-text">천안맛.zip</span>
          </div>
        </div>

        {/* 검색바 */}
        <div className="search-bar">
          <div className="search-left">
            <svg xmlns="http://www.w3.org/2000/svg" width="17" height="17" viewBox="0 0 24 24" fill="none" stroke="#aaa" strokeWidth="2" strokeLinecap="round" strokeLinejoin="round">
              <path d="M20 10c0 4.993-5.539 10.193-7.399 11.799a1 1 0 0 1-1.202 0C9.539 20.193 4 14.993 4 10a8 8 0 0 1 16 0"/>
              <circle cx="12" cy="10" r="3"/>
            </svg>
            <input
              className="search-input"
              type="text"
              placeholder="지역 검색"
              value={location}
              onChange={(e) => setLocation(e.target.value)}
              onKeyDown={handleKeyDown}
            />
          </div>

          <div className="search-divider" />

          <div className="search-right">
            <svg xmlns="http://www.w3.org/2000/svg" width="17" height="17" viewBox="0 0 24 24" fill="none" stroke="#aaa" strokeWidth="2" strokeLinecap="round" strokeLinejoin="round">
              <circle cx="11" cy="11" r="8"/>
              <path d="m21 21-4.3-4.3"/>
            </svg>
            <input
              className="search-input"
              type="text"
              placeholder="음식, 메뉴, 식당 검색"
              value={keyword}
              onChange={(e) => setKeyword(e.target.value)}
              onKeyDown={handleKeyDown}
            />
          </div>

          <button className="search-btn" onClick={handleSearch} aria-label="검색">
            <svg xmlns="http://www.w3.org/2000/svg" width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="white" strokeWidth="2.5" strokeLinecap="round" strokeLinejoin="round">
              <circle cx="11" cy="11" r="8"/>
              <path d="m21 21-4.3-4.3"/>
            </svg>
          </button>
        </div>

        {/* 네비 */}
        <nav className="header-nav">
          {isLoggedIn ? (
            <>
              <span className="user-greeting">👋 {user?.nickname}님</span>
              <button className="btn-mypage" onClick={onMyPageClick}>마이페이지</button>
              <button className="btn-logout" onClick={onLogoutClick}>로그아웃</button>
            </>
          ) : (
            <>
              <button className="btn-login" onClick={onLoginClick}>로그인</button>
              <button className="btn-signup" onClick={onSignupClick}>회원가입</button>
              <button className="btn-mypage" onClick={onMyPageClick}>마이페이지</button>
            </>
          )}
        </nav>

      </div>
    </header>
  );
};

export default Header;
