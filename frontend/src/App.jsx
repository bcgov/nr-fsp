import React, { useEffect, useState } from 'react';
import './App.css';

function App() {
  const [visible, setVisible] = useState(false);

  useEffect(() => {
    const t = setTimeout(() => setVisible(true), 100);
    return () => clearTimeout(t);
  }, []);

  return (
    <div className={`app ${visible ? 'app--visible' : ''}`}>
      <div className="grid-bg" aria-hidden="true" />

      <main className="stage">
        <div className="card">
          <span className="eyebrow">React · OpenShift · GitHub Actions</span>
          <h1 className="headline">
            Hello,<br />
            <em>World.</em>
          </h1>
          <p className="body">
            Your pipeline is live. Edit <code>src/App.js</code> and push to deploy.
          </p>
          <div className="pill-row">
            <span className="pill">Node {import.meta.env.VITE_NODE_ENV || 'production'}</span>
            <span className="pill pill--accent">v{import.meta.env.VITE_VERSION || '1.0.0'}</span>
          </div>
        </div>

        <div className="orb orb--a" aria-hidden="true" />
        <div className="orb orb--b" aria-hidden="true" />
      </main>
    </div>
  );
}

export default App;
