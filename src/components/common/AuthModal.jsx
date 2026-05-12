import { useState } from "react";
import "./AuthModal.css";
import { loginApi, joinApi } from "../../api/memberAPI";
const getPasswordStrength = (pw) => {
  if (!pw) return { level: 0, label: "" };
  let score = 0;
  if (pw.length >= 8) score++;
  if (/[A-Z]/.test(pw)) score++;
  if (/[0-9]/.test(pw)) score++;
  if (/[^A-Za-z0-9]/.test(pw)) score++;
  const labels = ["", "취약", "보통", "강함", "매우 강함"];
  return { level: score, label: labels[score] };
};
export default function AuthModal({
  mode,
  onClose,
  onLoginSuccess,
  onSwitchMode,
}) {
  const isLogin = mode === "login";
  const [loginForm, setLoginForm] = useState({
    email: "",
    password: "",
    remember: false,
  });
  const [signupForm, setSignupForm] = useState({
    name: "",
    email: "",
    nickname: "",
    password: "",
    confirm: "",
    agreeTerms: false,
    agreePrivacy: false,
  });
  const [error, setError] = useState("");
  const [loading, setLoading] = useState(false);
  const strength = getPasswordStrength(signupForm.password);
  const handleLoginChange = (field) => (e) => {
    setLoginForm((prev) => ({
      ...prev,
      [field]: e.target.type === "checkbox" ? e.target.checked : e.target.value,
    }));
  };
  const handleSignupChange = (field) => (e) => {
    setSignupForm((prev) => ({
      ...prev,
      [field]: e.target.type === "checkbox" ? e.target.checked : e.target.value,
    }));
  };
  const handleLoginSubmit = async (e) => {
    e.preventDefault();
    setError("");
    if (!loginForm.email.trim() || !loginForm.password.trim()) {
      setError("이메일과 비밀번호를 입력해주세요.");
      return;
    }
    setLoading(true);
    try {
      const userData = await loginApi({
        email: loginForm.email,
        password: loginForm.password,
      });
      onLoginSuccess(userData);
      onClose();
    } catch (err) {
      if (err.message.includes("401") || err.message.includes("403")) {
        setError("이메일 또는 비밀번호가 올바르지 않습니다.");
      } else {
        setError(err.message || "로그인 중 오류가 발생했습니다.");
      }
    } finally {
      setLoading(false);
    }
  };
  const validateSignup = () => {
    if (!signupForm.name.trim()) {
      return "이름을 입력해주세요.";
    }
    if (!signupForm.email.trim()) {
      return "이메일을 입력해주세요.";
    }
    if (!signupForm.nickname.trim() || signupForm.nickname.trim().length < 2) {
      return "닉네임은 2자 이상이어야 합니다.";
    }
    if (signupForm.password.length < 6) {
      return "비밀번호는 6자 이상이어야 합니다.";
    }
    if (signupForm.password !== signupForm.confirm) {
      return "비밀번호가 일치하지 않습니다.";
    }
    if (!signupForm.agreeTerms) {
      return "이용약관에 동의해주세요.";
    }
    if (!signupForm.agreePrivacy) {
      return "개인정보 처리방침에 동의해주세요.";
    }
    return null;
  };
  const handleSignupSubmit = async (e) => {
    e.preventDefault();
    setError("");
    const validationError = validateSignup();
    if (validationError) {
      setError(validationError);
      return;
    }
    setLoading(true);
    try {
      await joinApi({
        email: signupForm.email.trim(),
        password: signupForm.password,
        nickname: signupForm.nickname.trim(),
      });
      const userData = await loginApi({
        email: signupForm.email.trim(),
        password: signupForm.password,
      });
      onLoginSuccess(userData);
      onClose();
    } catch (err) {
      if (
        err.message.includes("이미") ||
        err.message.toLowerCase().includes("duplicate")
      ) {
        setError("이미 사용 중인 이메일입니다.");
      } else {
        setError(err.message || "회원가입 중 오류가 발생했습니다.");
      }
    } finally {
      setLoading(false);
    }
  };
  return (
    <div
      className="auth-modal-overlay"
      onClick={(e) => {
        if (e.target === e.currentTarget && !loading) {
          onClose();
        }
      }}
    >
      {" "}
      <div className="auth-page">
        {" "}
        <div className="auth-left">
          {" "}
          <div
            className="auth-left-bg"
            style={{
              backgroundImage: isLogin
                ? "url('https://images.unsplash.com/photo-1555396273-367ea4eb4db5?w=1200')"
                : "url('https://images.unsplash.com/photo-1504674900247-0877df9cc836?w=1200')",
            }}
          />{" "}
          <div className="auth-left-overlay" />{" "}
          <div className="auth-left-content">
            {" "}
            <h1 className="auth-left-title">
              {" "}
              {isLogin ? "천안맛.zip" : "시작하세요"}{" "}
            </h1>{" "}
            <p className="auth-left-sub">
              {" "}
              {isLogin
                ? "실제 리뷰 기반으로 찾는 우리 동네 진짜 맛집"
                : "맛집로그와 함께 새로운 맛집 여정을 시작하세요"}{" "}
            </p>{" "}
          </div>{" "}
        </div>{" "}
        <div className="auth-right">
          {" "}
          <button type="button" className="auth-close-btn" onClick={onClose}>
            {" "}
            ✕{" "}
          </button>{" "}
          <div className="auth-form-wrap">
            {" "}
            <h2 className="auth-form-title">
              {" "}
              {isLogin ? "로그인" : "회원가입"}{" "}
            </h2>{" "}
            <p className="auth-form-sub">
              {" "}
              {isLogin
                ? "맛집로그에 오신 것을 환영합니다"
                : "맛집로그 커뮤니티에 참여하세요"}{" "}
            </p>{" "}
            {error && <p className="auth-error">{error}</p>}{" "}
            {isLogin ? (
              <form className="auth-form" onSubmit={handleLoginSubmit}>
                {" "}
                <div className="auth-field">
                  {" "}
                  <label>이메일</label>{" "}
                  <div className="auth-input-wrap">
                    {" "}
                    <input
                      type="email"
                      placeholder="email@example.com"
                      value={loginForm.email}
                      onChange={handleLoginChange("email")}
                      disabled={loading}
                    />{" "}
                  </div>{" "}
                </div>{" "}
                <div className="auth-field">
                  {" "}
                  <label>비밀번호</label>{" "}
                  <div className="auth-input-wrap">
                    {" "}
                    <input
                      type="password"
                      placeholder="••••••••"
                      value={loginForm.password}
                      onChange={handleLoginChange("password")}
                      disabled={loading}
                    />{" "}
                  </div>{" "}
                </div>{" "}
                <div className="auth-options-row">
                  {" "}
                  <label className="auth-checkbox-label">
                    {" "}
                    <input
                      type="checkbox"
                      checked={loginForm.remember}
                      onChange={handleLoginChange("remember")}
                    />{" "}
                    로그인 상태 유지{" "}
                  </label>{" "}
                  <button type="button" className="auth-forgot-btn">
                    {" "}
                    비밀번호 찾기{" "}
                  </button>{" "}
                </div>{" "}
                <button
                  type="submit"
                  className="auth-btn-primary"
                  disabled={loading}
                >
                  {" "}
                  {loading ? (
                    <span className="btn-spinner">
                      {" "}
                      <span className="spinner" /> 로그인 중...{" "}
                    </span>
                  ) : (
                    "로그인"
                  )}{" "}
                </button>{" "}
                <p className="auth-switch">
                  {" "}
                  아직 계정이 없으신가요?{" "}
                  <button type="button" onClick={() => onSwitchMode("signup")}>
                    {" "}
                    회원가입{" "}
                  </button>{" "}
                </p>{" "}
              </form>
            ) : (
              <form className="auth-form" onSubmit={handleSignupSubmit}>
                {" "}
                <div className="auth-field">
                  {" "}
                  <label>이름</label>{" "}
                  <div className="auth-input-wrap">
                    {" "}
                    <input
                      type="text"
                      placeholder="홍길동"
                      value={signupForm.name}
                      onChange={handleSignupChange("name")}
                      disabled={loading}
                    />{" "}
                  </div>{" "}
                </div>{" "}
                <div className="auth-field">
                  {" "}
                  <label>이메일</label>{" "}
                  <div className="auth-input-wrap">
                    {" "}
                    <input
                      type="email"
                      placeholder="email@example.com"
                      value={signupForm.email}
                      onChange={handleSignupChange("email")}
                      disabled={loading}
                    />{" "}
                  </div>{" "}
                </div>{" "}
                <div className="auth-field">
                  {" "}
                  <label>닉네임</label>{" "}
                  <div className="auth-input-wrap">
                    {" "}
                    <input
                      type="text"
                      placeholder="맛집러버"
                      value={signupForm.nickname}
                      onChange={handleSignupChange("nickname")}
                      disabled={loading}
                    />{" "}
                  </div>{" "}
                </div>{" "}
                <div className="auth-field">
                  {" "}
                  <label>비밀번호</label>{" "}
                  <div className="auth-input-wrap">
                    {" "}
                    <input
                      type="password"
                      placeholder="••••••••"
                      value={signupForm.password}
                      onChange={handleSignupChange("password")}
                      disabled={loading}
                    />{" "}
                  </div>{" "}
                  {signupForm.password && (
                    <div className="password-strength">
                      {" "}
                      <div className="strength-bars">
                        {" "}
                        {[1, 2, 3, 4].map((n) => (
                          <div
                            key={n}
                            className={`strength-bar${strength.level >= n ? ` active level-${strength.level}` : ""}`}
                          />
                        ))}{" "}
                      </div>{" "}
                      <span
                        className={`strength-label level-${strength.level}`}
                      >
                        {" "}
                        {strength.label}{" "}
                      </span>{" "}
                    </div>
                  )}{" "}
                </div>{" "}
                <div className="auth-field">
                  {" "}
                  <label>비밀번호 확인</label>{" "}
                  <div className="auth-input-wrap">
                    {" "}
                    <input
                      type="password"
                      placeholder="••••••••"
                      value={signupForm.confirm}
                      onChange={handleSignupChange("confirm")}
                      disabled={loading}
                    />{" "}
                  </div>{" "}
                </div>{" "}
                <div className="auth-terms">
                  {" "}
                  <label className="auth-terms-item">
                    {" "}
                    <input
                      type="checkbox"
                      checked={signupForm.agreeTerms}
                      onChange={handleSignupChange("agreeTerms")}
                    />{" "}
                    <span>
                      {" "}
                      <span className="auth-terms-required"> (필수) </span>{" "}
                      이용약관 동의{" "}
                    </span>{" "}
                  </label>{" "}
                  <label className="auth-terms-item">
                    {" "}
                    <input
                      type="checkbox"
                      checked={signupForm.agreePrivacy}
                      onChange={handleSignupChange("agreePrivacy")}
                    />{" "}
                    <span>
                      {" "}
                      <span className="auth-terms-required"> (필수) </span>{" "}
                      개인정보 처리방침 동의{" "}
                    </span>{" "}
                  </label>{" "}
                </div>{" "}
                <button
                  type="submit"
                  className="auth-btn-primary"
                  disabled={loading}
                >
                  {" "}
                  {loading ? (
                    <span className="btn-spinner">
                      {" "}
                      <span className="spinner" /> 가입 중...{" "}
                    </span>
                  ) : (
                    "회원가입"
                  )}{" "}
                </button>{" "}
                <p className="auth-switch">
                  {" "}
                  이미 계정이 있으신가요?{" "}
                  <button type="button" onClick={() => onSwitchMode("login")}>
                    {" "}
                    로그인{" "}
                  </button>{" "}
                </p>{" "}
              </form>
            )}{" "}
          </div>{" "}
        </div>{" "}
      </div>{" "}
    </div>
  );
}
