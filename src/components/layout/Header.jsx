import SearchBar from "../common/SearchBar";
import Button from "../common/Button";
import "./Header.css";

const Header = () => {
  return (
    <header className="header">
      <div className="header-inner">
        <h1 className="logo">천안맛.zip</h1>

        <div className="header-search">
          <SearchBar placeholder="천안 맛집 검색..." />
        </div>

        <nav>
          <Button variant="ghost">로그인</Button>
          <Button variant="primary">회원가입</Button>
        </nav>
      </div>
    </header>
  );
};

export default Header;