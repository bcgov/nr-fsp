import React from 'react';
import './PageLayout.css';

export default function PageLayout({ title, screenId, children }) {
  return (
    <main className="page-layout" id="main-content">
      <div className="page-layout__container">
        <div className="page-layout__header">
          <span className="page-layout__screen-id">{screenId}</span>
          <h1 className="page-layout__title">{title}</h1>
        </div>
        <div className="page-layout__body">
          {children}
        </div>
      </div>
    </main>
  );
}
