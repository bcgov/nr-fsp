import React, { useState } from 'react';
import { BrowserRouter, Routes, Route, Navigate } from 'react-router-dom';
import BCGovHeader from './components/Header';
import NavBar from './components/NavBar';
import BCGovFooter from './components/Footer';
import PlaceholderPage from './pages/PlaceholderPage';
import './App.css';

// ── Login Page ─────────────────────────────────────────────
function LoginPage({ onLogin }) {
  return (
    <main className="login-page" id="main-content">
      <div className="login-card">
        <div className="login-card__logo-wrap">
          <img
            src="/BCID_H_RGB_pos.png"
            alt="Government of British Columbia"
            className="login-card__logo"
          />
        </div>
        <h1 className="login-card__title">Forest Stewardship Plan</h1>
        <p className="login-card__subtitle">
          Sign in with your BC Government account to access and manage your plans.
        </p>

        <button type="button" onClick={onLogin} className="login-card__btn">
          <svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 21 21" width="20" height="20" aria-hidden="true" className="login-card__btn-icon">
            <rect x="0"  y="0"  width="10" height="10" fill="#f25022"/>
            <rect x="11" y="0"  width="10" height="10" fill="#7fba00"/>
            <rect x="0"  y="11" width="10" height="10" fill="#00a4ef"/>
            <rect x="11" y="11" width="10" height="10" fill="#ffb900"/>
          </svg>
          Log in with IDIR
        </button>

        <div className="login-card__divider"><span>or</span></div>

        <button type="button" onClick={onLogin} className="login-card__btn login-card__btn--secondary">
          <img src="/BCID_H_RGB_pos.png" alt="" aria-hidden="true" className="login-card__btn-icon login-card__btn-icon--bcid" />
          Log in with Business BCeID
        </button>

        <p className="login-card__help">
          Need help? <a href="#" className="login-card__link">Contact support</a>
        </p>
      </div>
    </main>
  );
}

// ── Shell wraps header + nav + routes + footer ─────────────
function AppShell({ isLoggedIn, userName, onLogin, onLogout }) {
  return (
    <div className="app-shell">
      <BCGovHeader
        isLoggedIn={isLoggedIn}
        userName={userName}
        onLoginClick={onLogin}
        onLogoutClick={onLogout}
      />

      {isLoggedIn && <NavBar />}

      {isLoggedIn ? (
        <Routes>
          {/* Default */}
          <Route path="/" element={<Navigate to="/search" replace />} />

          {/* Search */}
          <Route path="/search" element={<PlaceholderPage title="Search" description="Search for FSPs and related records." />} />
          <Route path="/links/fta"     element={<PlaceholderPage title="FTA" />} />
          <Route path="/links/results" element={<PlaceholderPage title="RESULTS" />} />
          <Route path="/links/mapview" element={<PlaceholderPage title="MapView" />} />
          <Route path="/links/cims"    element={<PlaceholderPage title="CIMS" />} />

          {/* Inbox */}
          <Route path="/inbox" element={<PlaceholderPage title="Inbox" description="View your incoming FSP submissions and notifications." />} />

          {/* FSP */}
          <Route path="/fsp/information"      element={<PlaceholderPage title="FSP Information"      description="Administrative details for each FSP. FSPs and amendments are created and submitted here." />} />
          <Route path="/fsp/attachments"      element={<PlaceholderPage title="Attachments"          description="Attach and view documents related to specific sections of the FSP or amendment." />} />
          <Route path="/fsp/stocking-standards" element={<PlaceholderPage title="Stocking Standards" description="Summary list of Stocking Standards associated to the current FSP/Amendment." />} />
          <Route path="/fsp/fdu-map"          element={<PlaceholderPage title="FDU/Map"              description="List of all Forest Development Units (FDU) included in an FSP." />} />
          <Route path="/fsp/identified-areas" element={<PlaceholderPage title="Identified Areas/Map" description="List of all Identified/Declared Areas included in an FSP." />} />
          <Route path="/fsp/workflow"         element={<PlaceholderPage title="Workflow"             description="Track an FSP/Amendment's stages of review and log approvals or rejections." />} />

          {/* Data Submission */}
          <Route path="/data-submission/xml" element={<PlaceholderPage title="XML Submission" description="Submit FSP data via XML." />} />

          {/* Admin */}
          <Route path="/admin/district-notification" element={<PlaceholderPage title="District Notification" description="Manage district notifications." />} />

          {/* Reports */}
          <Route path="/reports/jcrs" element={<PlaceholderPage title="JCRS Reports" description="View and generate JCRS reports." />} />

          {/* Catch-all */}
          <Route path="*" element={<Navigate to="/search" replace />} />
        </Routes>
      ) : (
        <LoginPage onLogin={onLogin} />
      )}

      <BCGovFooter />
    </div>
  );
}

// ── App ────────────────────────────────────────────────────
export default function App() {
  const [isLoggedIn, setIsLoggedIn] = useState(false);
  const [userName] = useState('Jane Smith');

  return (
    <BrowserRouter>
      <AppShell
        isLoggedIn={isLoggedIn}
        userName={userName}
        onLogin={() => setIsLoggedIn(true)}
        onLogout={() => setIsLoggedIn(false)}
      />
    </BrowserRouter>
  );
}
