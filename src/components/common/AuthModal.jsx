import { useState } from "react";
import "./AuthModal.css";
import { loginApi, joinApi } from "../../api/memberApi";

/* ────────────────────────────────────────
   로그인 모달
──────────────────────────────────────── */
export const LoginModal = ({ onClose, onSuccess, onSwitchToSignup }) => {
  const [form, setForm] = useState({ email: "", password: "" });
  const [error, setError] = useState("");
  const [loading, setLoading] = useState(false);

  const handleChange = (field) => (e) =>
    setForm((prev) => ({ ...prev, [field]: e.target.value }));

  const handleSubmit = async (e) => {
    e.preventDefault();
    setError("");

    if (!form.email.trim() || !form.password.trim()) {
      setError("이메일과 비밀번호를 입력해주세요.");
      return;
    }

    setLoading(true);
    try {
      const userData = await loginApi({ email: form.email, password: form.password });
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
    <div
      className="modal-backdrop"
      onClick={(e) => e.target === e.currentTarget && !loading && onClose()}
    >
      <div className="modal-box">
        <button className="modal-close" onClick={onClose} disabled={loading} aria-label="닫기">
          <svg width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2">
            <line x1="18" y1="6" x2="6" y2="18" /><line x1="6" y1="6" x2="18" y2="18" />
          </svg>
        </button>

        <div className="modal-logo">
          <svg xmlns="http://www.w3.org/2000/svg" width="28" height="28" viewBox="0 0 24 24" fill="none" stroke="#ff6b35" strokeWidth="2" strokeLinecap="round" strokeLinejoin="round">
            <path d="M20 10c0 4.993-5.539 10.193-7.399 11.799a1 1 0 0 1-1.202 0C9.539 20.193 4 14.993 4 10a8 8 0 0 1 16 0" />
            <circle cx="12" cy="10" r="3" />
          </svg>
          <span>천안맛.zip</span>
        </div>

        <h2 className="modal-title">로그인</h2>
        <p className="modal-subtitle">맛있는 맛집 정보를 공유해보세요 🍽️</p>

        {error && <p className="modal-error">{error}</p>}

        <form onSubmit={handleSubmit} className="modal-form">
          <div className="form-group">
            <label htmlFor="login-email">이메일</label>
            <input
              id="login-email"
              type="email"
              placeholder="example@email.com"
              value={form.email}
              onChange={handleChange("email")}
              disabled={loading}
              autoComplete="email"
              autoFocus
            />
          </div>
          <div className="form-group">
            <label htmlFor="login-pw">비밀번호</label>
            <input
              id="login-pw"
              type="password"
              placeholder="비밀번호를 입력하세요"
              value={form.password}
              onChange={handleChange("password")}
              disabled={loading}
              autoComplete="current-password"
            />
          </div>

          <button type="submit" className="modal-btn-primary" disabled={loading}>
            {loading ? <span className="btn-spinner"><span className="spinner" /> 로그인 중...</span> : "로그인"}
          </button>
        </form>

        <p className="modal-switch">
          아직 회원이 아니신가요?{" "}
          <button onClick={onSwitchToSignup} disabled={loading}>회원가입</button>
        </p>
      </div>
    </div>
  );
};

/* ────────────────────────────────────────
   회원가입 모달
──────────────────────────────────────── */
export const SignupModal = ({ onClose, onSuccess, onSwitchToLogin }) => {
  const [form, setForm] = useState({ nickname: "", email: "", password: "", confirm: "" });
  const [error, setError] = useState("");
  const [loading, setLoading] = useState(false);

  const handleChange = (field) => (e) =>
    setForm((prev) => ({ ...prev, [field]: e.target.value }));

  const validate = () => {
    if (!form.nickname.trim() || !form.email.trim() || !form.password.trim()) return "모든 항목을 입력해주세요.";
    if (form.nickname.trim().length < 2) return "닉네임은 2자 이상이어야 합니다.";
    if (form.password.length < 6) return "비밀번호는 6자 이상이어야 합니다.";
    if (form.password !== form.confirm) return "비밀번호가 일치하지 않습니다.";
    return null;
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    setError("");

    const validationError = validate();
    if (validationError) { setError(validationError); return; }

    setLoading(true);
    try {
      await joinApi({
        email: form.email.trim(),
        password: form.password,
        nickname: form.nickname.trim(),
      });

      // 회원가입 성공 후 자동 로그인
      const userData = await loginApi({
        email: form.email.trim(),
        password: form.password,
      });

      onSuccess(userData);
      onClose();
    } catch (err) {
      if (err.message.includes("이미") || err.message.toLowerCase().includes("duplicate")) {
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
      className="modal-backdrop"
      onClick={(e) => e.target === e.currentTarget && !loading && onClose()}
    >
      <div className="modal-box">
        <button className="modal-close" onClick={onClose} disabled={loading} aria-label="닫기">
          <svg width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2">
            <line x1="18" y1="6" x2="6" y2="18" /><line x1="6" y1="6" x2="18" y2="18" />
          </svg>
        </button>

        <div className="modal-logo">
          <svg xmlns="http://www.w3.org/2000/svg" width="28" height="28" viewBox="0 0 24 24" fill="none" stroke="#ff6b35" strokeWidth="2" strokeLinecap="round" strokeLinejoin="round">
            <path d="M20 10c0 4.993-5.539 10.193-7.399 11.799a1 1 0 0 1-1.202 0C9.539 20.193 4 14.993 4 10a8 8 0 0 1 16 0" />
            <circle cx="12" cy="10" r="3" />
          </svg>
          <span>천안맛.zip</span>
        </div>

        <h2 className="modal-title">회원가입</h2>
        <p className="modal-subtitle">천안맛.zip과 함께 맛있는 여행을 시작하세요!</p>

        {error && <p className="modal-error">{error}</p>}

        <form onSubmit={handleSubmit} className="modal-form">
          <div className="form-group">
            <label htmlFor="signup-nickname">닉네임</label>
            <input
              id="signup-nickname"
              type="text"
              placeholder="2자 이상 입력하세요"
              value={form.nickname}
              onChange={handleChange("nickname")}
              disabled={loading}
              autoFocus
            />
          </div>
          <div className="form-group">
            <label htmlFor="signup-email">이메일</label>
            <input
              id="signup-email"
              type="email"
              placeholder="example@email.com"
              value={form.email}
              onChange={handleChange("email")}
              disabled={loading}
              autoComplete="email"
            />
          </div>
          <div className="form-group">
            <label htmlFor="signup-pw">비밀번호</label>
            <input
              id="signup-pw"
              type="password"
              placeholder="6자 이상 입력하세요"
              value={form.password}
              onChange={handleChange("password")}
              disabled={loading}
              autoComplete="new-password"
            />
          </div>
          <div className="form-group">
            <label htmlFor="signup-pw-confirm">비밀번호 확인</label>
            <input
              id="signup-pw-confirm"
              type="password"
              placeholder="비밀번호를 다시 입력하세요"
              value={form.confirm}
              onChange={handleChange("confirm")}
              disabled={loading}
              autoComplete="new-password"
            />
          </div>

          <button type="submit" className="modal-btn-primary" disabled={loading}>
            {loading ? <span className="btn-spinner"><span className="spinner" /> 가입 중...</span> : "회원가입"}
          </button>
        </form>

        <p className="modal-switch">
          이미 회원이신가요?{" "}
          <button onClick={onSwitchToLogin} disabled={loading}>로그인</button>
        </p>
      </div>
    </div>
  );
};
