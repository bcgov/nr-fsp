import React from 'react';
import './PlaceholderPage.css';

export default function PlaceholderPage({ title, description }) {
  return (
    <main className="placeholder-page" id="main-content">
      <div className="placeholder-page__container">
        <h1 className="placeholder-page__title">{title}</h1>
        {description && (
          <p className="placeholder-page__desc">{description}</p>
        )}
        <div className="placeholder-page__body">
          <p>This page is under construction.</p>
        </div>
      </div>
    </main>
  );
}
