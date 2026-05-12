import { useState } from "react";
import "./AuthModal.css";
import { loginApi, joinApi } from "../../api/memberApi";

/* ── 비밀번호 강도 계산 ── 8자이상,대문자,숫자,특수문자 */
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

/* ────────────────────────────────────────
   로그인 페이지
──────────────────────────────────────── */
export const LoginModal = ({ onClose, onSuccess, onSwitchToSignup }) => {
  const [form, setForm] = useState({
    email: "",
    password: "",
    remember: false,
  });
  const [error, setError] = useState("");
  const [loading, setLoading] = useState(false);

  const handleChange = (field) => (e) =>
    setForm((prev) => ({
      ...prev,
      [field]: e.target.type === "checkbox" ? e.target.checked : e.target.value,
    }));

  const handleSubmit = async (e) => {
    e.preventDefault();
    setError("");
    if (!form.email.trim() || !form.password.trim()) {
      setError("이메일과 비밀번호를 입력해주세요.");
      return;
    }
    setLoading(true);
    try {
      const userData = await loginApi({
        email: form.email,
        password: form.password,
      });
      onSuccess(userData);
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

  return (
    <div className="auth-page">
      {/* 왼쪽 배경 */}
      <div className="auth-left">
        <div
          className="auth-left-bg"
          style={{
            backgroundImage:
              "url('https://images.unsplash.com/photo-1555396273-367ea4eb4db5?w=1200')",
          }}
        />
        <div className="auth-left-overlay" />
        <div className="auth-left-content">
          <h1 className="auth-left-title">천안맛.zip</h1>
          <p className="auth-left-sub">
            실제 리뷰 기반으로 찾는 우리 동네 진짜 맛집
          </p>
          <div className="auth-left-features">
            {[
              {
                icon: "✓",
                title: "신뢰할 수 있는 리뷰",
                desc: "실제 방문자들의 진실한 후기",
              },
              {
                icon: "✓",
                title: "개인화된 추천",
                desc: "취향에 맞는 맛집 발견",
              },
              {
                icon: "✓",
                title: "커뮤니티 기반",
                desc: "맛집 정보 공유와 소통",
              },
            ].map((f) => (
              <div className="auth-feature-item" key={f.title}>
                <div className="auth-feature-icon">
                  <svg
                    viewBox="0 0 16 16"
                    fill="none"
                    stroke="white"
                    strokeWidth="2.5"
                  >
                    <polyline points="2,8 6,12 14,4" />
                  </svg>
                </div>
                <div className="auth-feature-text">
                  <strong>{f.title}</strong>
                  <span>{f.desc}</span>
                </div>
              </div>
            ))}
          </div>
        </div>
      </div>

      {/* 오른쪽 폼 */}
      <div className="auth-right">
        <button className="auth-back-btn" onClick={onClose}>
          <svg
            width="16"
            height="16"
            viewBox="0 0 24 24"
            fill="none"
            stroke="currentColor"
            strokeWidth="2"
          >
            <polyline points="15,18 9,12 15,6" />
          </svg>
          홈으로
        </button>

        <div className="auth-form-wrap">
          <h2 className="auth-form-title">로그인</h2>
          <p className="auth-form-sub">맛집로그에 오신 것을 환영합니다</p>

          {error && <p className="auth-error">{error}</p>}

          <form className="auth-form" onSubmit={handleSubmit}>
            <div className="auth-field">
              <label htmlFor="login-email">이메일</label>
              <div className="auth-input-wrap">
                <svg
                  width="16"
                  height="16"
                  viewBox="0 0 24 24"
                  fill="none"
                  stroke="currentColor"
                  strokeWidth="2"
                >
                  <rect x="2" y="4" width="20" height="16" rx="2" />
                  <polyline points="2,4 12,13 22,4" />
                </svg>
                <input
                  id="login-email"
                  type="email"
                  placeholder="email@example.com"
                  value={form.email}
                  onChange={handleChange("email")}
                  disabled={loading}
                  autoFocus
                />
              </div>
            </div>

            <div className="auth-field">
              <label htmlFor="login-pw">비밀번호</label>
              <div className="auth-input-wrap">
                <svg
                  width="16"
                  height="16"
                  viewBox="0 0 24 24"
                  fill="none"
                  stroke="currentColor"
                  strokeWidth="2"
                >
                  <rect x="3" y="11" width="18" height="11" rx="2" />
                  <path d="M7 11V7a5 5 0 0 1 10 0v4" />
                </svg>
                <input
                  id="login-pw"
                  type="password"
                  placeholder="••••••••"
                  value={form.password}
                  onChange={handleChange("password")}
                  disabled={loading}
                  autoComplete="current-password"
                />
              </div>
            </div>

            <div className="auth-options-row">
              <label className="auth-checkbox-label">
                <input
                  type="checkbox"
                  checked={form.remember}
                  onChange={handleChange("remember")}
                />
                로그인 상태 유지
              </label>
              <button type="button" className="auth-forgot-btn">
                비밀번호 찾기
              </button>
            </div>

            <button
              type="submit"
              className="auth-btn-primary"
              disabled={loading}
            >
              {loading ? (
                <span className="btn-spinner">
                  <span className="spinner" /> 로그인 중...
                </span>
              ) : (
                "로그인"
              )}
            </button>
          </form>

          <div className="auth-divider">또는</div>

          <div className="auth-social-btns">
            <button type="button" className="auth-social-btn kakao">
              <span style={{ fontSize: 16 }}>💛</span> 카카오로 시작하기
            </button>
            <button type="button" className="auth-social-btn naver">
              <span className="social-icon naver-icon">N</span> 네이버로
              시작하기
            </button>
            <button type="button" className="auth-social-btn google">
              <svg width="16" height="16" viewBox="0 0 24 24">
                <path
                  fill="#4285F4"
                  d="M22.56 12.25c0-.78-.07-1.53-.2-2.25H12v4.26h5.92c-.26 1.37-1.04 2.53-2.21 3.31v2.77h3.57c2.08-1.92 3.28-4.74 3.28-8.09z"
                />
                <path
                  fill="#34A853"
                  d="M12 23c2.97 0 5.46-.98 7.28-2.66l-3.57-2.77c-.98.66-2.23 1.06-3.71 1.06-2.86 0-5.29-1.93-6.16-4.53H2.18v2.84C3.99 20.53 7.7 23 12 23z"
                />
                <path
                  fill="#FBBC05"
                  d="M5.84 14.09c-.22-.66-.35-1.36-.35-2.09s.13-1.43.35-2.09V7.07H2.18C1.43 8.55 1 10.22 1 12s.43 3.45 1.18 4.93l3.66-2.84z"
                />
                <path
                  fill="#EA4335"
                  d="M12 5.38c1.62 0 3.06.56 4.21 1.64l3.15-3.15C17.45 2.09 14.97 1 12 1 7.7 1 3.99 3.47 2.18 7.07l3.66 2.84c.87-2.6 3.3-4.53 6.16-4.53z"
                />
              </svg>
              Google로 시작하기
            </button>
          </div>

          <p className="auth-switch">
            아직 계정이 없으신가요?
            <button onClick={onSwitchToSignup} disabled={loading}>
              회원가입
            </button>
          </p>
        </div>
      </div>
    </div>
  );
};

/* ────────────────────────────────────────
   회원가입 페이지
──────────────────────────────────────── */
export const SignupModal = ({ onClose, onSuccess, onSwitchToLogin }) => {
  const [form, setForm] = useState({
    name: "",
    email: "",
    nickname: "",
    password: "",
    confirm: "",
    agreeTerms: false,
    agreePrivacy: false,
    agreeMarketing: false,
  });
  const [error, setError] = useState("");
  const [loading, setLoading] = useState(false);

  const strength = getPasswordStrength(form.password);

  const handleChange = (field) => (e) =>
    setForm((prev) => ({
      ...prev,
      [field]: e.target.type === "checkbox" ? e.target.checked : e.target.value,
    }));

  const validate = () => {
    if (!form.name.trim()) return "이름을 입력해주세요.";
    if (!form.email.trim()) return "이메일을 입력해주세요.";
    if (!form.nickname.trim() || form.nickname.trim().length < 2)
      return "닉네임은 2자 이상이어야 합니다.";
    if (form.password.length < 6) return "비밀번호는 6자 이상이어야 합니다.";
    if (form.password !== form.confirm) return "비밀번호가 일치하지 않습니다.";
    if (!form.agreeTerms) return "이용약관에 동의해주세요.";
    if (!form.agreePrivacy) return "개인정보 처리방침에 동의해주세요.";
    return null;
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    setError("");
    const err = validate();
    if (err) {
      setError(err);
      return;
    }

    setLoading(true);
    try {
      await joinApi({
        email: form.email.trim(),
        password: form.password,
        nickname: form.nickname.trim(),
      });
      const userData = await loginApi({
        email: form.email.trim(),
        password: form.password,
      });
      onSuccess(userData);
      onClose();
    } catch (err) {
      if (
        err.message.toLowerCase().includes("duplicate") ||
        err.message.includes("이미")
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
    <div className="auth-page">
      {/* 왼쪽 배경 */}
      <div className="auth-left">
        <div
          className="auth-left-bg"
          style={{
            backgroundImage:
              "url('https://images.unsplash.com/photo-1504674900247-0877df9cc836?w=1200')",
          }}
        />
        <div className="auth-left-overlay" />
        <div className="auth-left-content">
          <h1 className="auth-left-title">시작하세요</h1>
          <p className="auth-left-sub">
            맛집로그와 함께 새로운 맛집 여정을 시작하세요
          </p>
          <div className="auth-left-features">
            {[
              {
                title: "맛집 발견",
                desc: "AI 기반 추천으로 나에게 딱 맞는 맛집을 찾아보세요",
              },
              {
                title: "리뷰 작성",
                desc: "방문한 맛집의 후기를 남기고 다른 사람들과 공유하세요",
              },
              {
                title: "리워드 획득",
                desc: "활동에 따라 포인트를 받고 다양한 혜택을 누리세요",
              },
            ].map((f) => (
              <div className="auth-feature-item" key={f.title}>
                <div className="auth-feature-icon">
                  <svg
                    viewBox="0 0 16 16"
                    fill="none"
                    stroke="white"
                    strokeWidth="2.5"
                  >
                    <polyline points="2,8 6,12 14,4" />
                  </svg>
                </div>
                <div className="auth-feature-text">
                  <strong>{f.title}</strong>
                  <span>{f.desc}</span>
                </div>
              </div>
            ))}
          </div>
        </div>
      </div>

      {/* 오른쪽 폼 */}
      <div className="auth-right">
        <button className="auth-back-btn" onClick={onSwitchToLogin}>
          <svg
            width="16"
            height="16"
            viewBox="0 0 24 24"
            fill="none"
            stroke="currentColor"
            strokeWidth="2"
          >
            <polyline points="15,18 9,12 15,6" />
          </svg>
          로그인으로
        </button>

        <div className="auth-form-wrap">
          <h2 className="auth-form-title">회원가입</h2>
          <p className="auth-form-sub">맛집로그 커뮤니티에 참여하세요</p>

          {error && <p className="auth-error">{error}</p>}

          <form className="auth-form" onSubmit={handleSubmit}>
            {/* 이름 */}
            <div className="auth-field">
              <label htmlFor="signup-name">이름</label>
              <div className="auth-input-wrap">
                <svg
                  width="16"
                  height="16"
                  viewBox="0 0 24 24"
                  fill="none"
                  stroke="currentColor"
                  strokeWidth="2"
                >
                  <circle cx="12" cy="8" r="4" />
                  <path d="M4 20c0-4 3.6-7 8-7s8 3 8 7" />
                </svg>
                <input
                  id="signup-name"
                  type="text"
                  placeholder="홍길동"
                  value={form.name}
                  onChange={handleChange("name")}
                  disabled={loading}
                  autoFocus
                />
              </div>
            </div>

            {/* 이메일 */}
            <div className="auth-field">
              <label htmlFor="signup-email">이메일</label>
              <div className="auth-input-wrap">
                <svg
                  width="16"
                  height="16"
                  viewBox="0 0 24 24"
                  fill="none"
                  stroke="currentColor"
                  strokeWidth="2"
                >
                  <rect x="2" y="4" width="20" height="16" rx="2" />
                  <polyline points="2,4 12,13 22,4" />
                </svg>
                <input
                  id="signup-email"
                  type="email"
                  placeholder="email@example.com"
                  value={form.email}
                  onChange={handleChange("email")}
                  disabled={loading}
                />
              </div>
            </div>

            {/* 닉네임 */}
            <div className="auth-field">
              <label htmlFor="signup-nickname">닉네임</label>
              <div className="auth-input-wrap">
                <svg
                  width="16"
                  height="16"
                  viewBox="0 0 24 24"
                  fill="none"
                  stroke="currentColor"
                  strokeWidth="2"
                >
                  <circle cx="12" cy="8" r="4" />
                  <path d="M4 20c0-4 3.6-7 8-7s8 3 8 7" />
                </svg>
                <input
                  id="signup-nickname"
                  type="text"
                  placeholder="맛집러버"
                  value={form.nickname}
                  onChange={handleChange("nickname")}
                  disabled={loading}
                />
              </div>
              <span style={{ fontSize: 12, color: "#aaa", marginTop: 2 }}>
                다른 사용자에게 표시되는 이름입니다
              </span>
            </div>

            {/* 비밀번호 */}
            <div className="auth-field">
              <label htmlFor="signup-pw">비밀번호</label>
              <div className="auth-input-wrap">
                <svg
                  width="16"
                  height="16"
                  viewBox="0 0 24 24"
                  fill="none"
                  stroke="currentColor"
                  strokeWidth="2"
                >
                  <rect x="3" y="11" width="18" height="11" rx="2" />
                  <path d="M7 11V7a5 5 0 0 1 10 0v4" />
                </svg>
                <input
                  id="signup-pw"
                  type="password"
                  placeholder="••••••••"
                  value={form.password}
                  onChange={handleChange("password")}
                  disabled={loading}
                  autoComplete="new-password"
                />
              </div>
              {form.password && (
                <div className="password-strength">
                  <div className="strength-bars">
                    {[1, 2, 3, 4].map((n) => (
                      <div
                        key={n}
                        className={`strength-bar${strength.level >= n ? ` active level-${strength.level}` : ""}`}
                      />
                    ))}
                  </div>
                  <span className={`strength-label level-${strength.level}`}>
                    {strength.label}
                  </span>
                </div>
              )}
            </div>

            {/* 비밀번호 확인 */}
            <div className="auth-field">
              <label htmlFor="signup-pw-confirm">비밀번호 확인</label>
              <div className="auth-input-wrap">
                <svg
                  width="16"
                  height="16"
                  viewBox="0 0 24 24"
                  fill="none"
                  stroke="currentColor"
                  strokeWidth="2"
                >
                  <rect x="3" y="11" width="18" height="11" rx="2" />
                  <path d="M7 11V7a5 5 0 0 1 10 0v4" />
                </svg>
                <input
                  id="signup-pw-confirm"
                  type="password"
                  placeholder="••••••••"
                  value={form.confirm}
                  onChange={handleChange("confirm")}
                  disabled={loading}
                  autoComplete="new-password"
                />
              </div>
            </div>

            {/* 약관 동의 */}
            <div className="auth-terms">
              <label className="auth-terms-item">
                <input
                  type="checkbox"
                  checked={form.agreeTerms}
                  onChange={handleChange("agreeTerms")}
                />
                <span>
                  <span className="auth-terms-required">(필수)</span>이용약관
                  동의
                </span>
              </label>
              <label className="auth-terms-item">
                <input
                  type="checkbox"
                  checked={form.agreePrivacy}
                  onChange={handleChange("agreePrivacy")}
                />
                <span>
                  <span className="auth-terms-required">(필수)</span>개인정보
                  처리방침 동의
                </span>
              </label>
              <label className="auth-terms-item">
                <input
                  type="checkbox"
                  checked={form.agreeMarketing}
                  onChange={handleChange("agreeMarketing")}
                />
                <span style={{ color: "#888" }}>
                  (선택) 마케팅 정보 수신 동의
                </span>
              </label>
            </div>

            <button
              type="submit"
              className="auth-btn-primary"
              disabled={loading}
            >
              {loading ? (
                <span className="btn-spinner">
                  <span className="spinner" /> 가입 중...
                </span>
              ) : (
                "회원가입"
              )}
            </button>
          </form>

          <p className="auth-switch">
            이미 계정이 있으신가요?
            <button onClick={onSwitchToLogin} disabled={loading}>
              로그인
            </button>
          </p>
        </div>
      </div>
    </div>
  );
};
