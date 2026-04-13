import React, { useState } from 'react';
import { BrowserRouter, Routes, Route, Navigate } from 'react-router-dom';
import BCGovHeader from './components/Header';
import NavBar from './components/NavBar';
import BCGovFooter from './components/Footer';

// Pages
import WelcomePage            from './pages/WelcomePage';
import SearchPage             from './pages/SearchPage';
import InboxPage              from './pages/InboxPage';
import FspInformationPage     from './pages/FspInformationPage';
import AmendInformationPage   from './pages/AmendInformationPage';
import ExtensionRequestPage   from './pages/ExtensionRequestPage';
import ExtensionSummaryPage   from './pages/ExtensionSummaryPage';
import ReplaceInformationPage from './pages/ReplaceInformationPage';
import AttachmentsPage        from './pages/AttachmentsPage';
import StockingStandardsPage  from './pages/StockingStandardsPage';
import FduMapPage             from './pages/FduMapPage';
import IdentifiedAreasPage    from './pages/IdentifiedAreasPage';
import WorkflowPage           from './pages/WorkflowPage';
import HistoryPage            from './pages/HistoryPage';
import DistrictNotificationPage from './pages/DistrictNotificationPage';
import XmlSubmissionPage      from './pages/XmlSubmissionPage';
import JcrsReportsPage        from './pages/JcrsReportsPage';

import './App.css';

// ── Login Page ─────────────────────────────────────────────
function LoginPage({ onLogin }) {
  return (
    <main className="login-page" id="main-content">
      <div className="login-card">
        <div className="login-card__logo-wrap">
          <img src="/BCID_H_RGB_pos.png" alt="Government of British Columbia" className="login-card__logo" />
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

// ── App Shell ──────────────────────────────────────────────
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
          <Route path="/"               element={<Navigate to="/welcome" replace />} />
          <Route path="/welcome"        element={<WelcomePage userName={userName} />} />

          {/* Search */}
          <Route path="/search"         element={<SearchPage />} />
          <Route path="/links/fta"      element={<WelcomePage userName={userName} />} />
          <Route path="/links/results"  element={<WelcomePage userName={userName} />} />
          <Route path="/links/mapview"  element={<WelcomePage userName={userName} />} />
          <Route path="/links/cims"     element={<WelcomePage userName={userName} />} />

          {/* InBox */}
          <Route path="/inbox"          element={<InboxPage />} />

          {/* FSP */}
          <Route path="/fsp/information"        element={<FspInformationPage />} />
          <Route path="/fsp/amend-information"  element={<AmendInformationPage />} />
          <Route path="/fsp/extension-request"  element={<ExtensionRequestPage />} />
          <Route path="/fsp/extension-summary"  element={<ExtensionSummaryPage />} />
          <Route path="/fsp/replace-information" element={<ReplaceInformationPage />} />
          <Route path="/fsp/attachments"        element={<AttachmentsPage />} />
          <Route path="/fsp/stocking-standards" element={<StockingStandardsPage />} />
          <Route path="/fsp/fdu-map"            element={<FduMapPage />} />
          <Route path="/fsp/identified-areas"   element={<IdentifiedAreasPage />} />
          <Route path="/fsp/workflow"           element={<WorkflowPage />} />
          <Route path="/fsp/history"            element={<HistoryPage />} />

          {/* Data Submission */}
          <Route path="/data-submission/xml"    element={<XmlSubmissionPage />} />

          {/* Admin */}
          <Route path="/admin/district-notification" element={<DistrictNotificationPage />} />

          {/* Reports */}
          <Route path="/reports/jcrs"           element={<JcrsReportsPage />} />

          {/* Catch-all */}
          <Route path="*" element={<Navigate to="/welcome" replace />} />
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
  const [userName]  = useState('Jane Smith');

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
