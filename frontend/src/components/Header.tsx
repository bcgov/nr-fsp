import { useState } from 'react';
import './Header.css';

const BCGovLogo = () => (
  <img
    src="/BCID_H_RGB_rev.png"
    alt="Government of British Columbia"
    className="header__logo-img"
  />
);

interface HeaderProps {
  isLoggedIn: boolean;
  userName?: string;
  onLoginClick?: () => void;
  onLogoutClick?: () => void;
}

export default function Header({ onLoginClick, isLoggedIn, userName, onLogoutClick }: HeaderProps) {
  const [menuOpen, setMenuOpen] = useState(false);

  return (
    <header className="bcgov-header" role="banner">
      {/* Top blue bar */}
      <div className="bcgov-header__top">
        <div className="bcgov-header__container">
          {/* Logo */}
          <a href="/" className="bcgov-header__logo-link" aria-label="Go to homepage">
            <BCGovLogo />
          </a>

          {/* App title */}
          <div className="bcgov-header__title">
            <span className="bcgov-header__app-name">Forest Stewardship Plan</span>
          </div>

          {/* Right side: login/user */}
          <nav className="bcgov-header__nav" aria-label="User navigation">
            {isLoggedIn ? (
              <div className="bcgov-header__user">
                <span className="bcgov-header__username">{userName || 'User'}</span>
                <button
                  className="bcgov-header__btn bcgov-header__btn--ghost"
                  onClick={onLogoutClick}
                  type="button"
                >
                  Log out
                </button>
              </div>
            ) : (
              <button
                className="bcgov-header__btn bcgov-header__btn--primary"
                onClick={onLoginClick}
                type="button"
              >
                Log in
              </button>
            )}
          </nav>

          {/* Mobile hamburger */}
          <button
            className={`bcgov-header__hamburger ${menuOpen ? 'is-open' : ''}`}
            aria-label={menuOpen ? 'Close menu' : 'Open menu'}
            aria-expanded={menuOpen}
            onClick={() => setMenuOpen(v => !v)}
            type="button"
          >
            <span /><span /><span />
          </button>
        </div>
      </div>

      {/* Gold divider */}
      <div className="bcgov-header__divider" aria-hidden="true" />

      {/* Mobile dropdown */}
      {menuOpen && (
        <div className="bcgov-header__mobile-nav" role="navigation" aria-label="Mobile user navigation">
          {isLoggedIn ? (
            <>
              <span className="bcgov-header__mobile-username">{userName || 'User'}</span>
              <button
                className="bcgov-header__btn bcgov-header__btn--ghost"
                onClick={() => { setMenuOpen(false); onLogoutClick?.(); }}
                type="button"
              >
                Log out
              </button>
            </>
          ) : (
            <button
              className="bcgov-header__btn bcgov-header__btn--primary"
              onClick={() => { setMenuOpen(false); onLoginClick?.(); }}
              type="button"
            >
              Log in
            </button>
          )}
        </div>
      )}
    </header>
  );
}
