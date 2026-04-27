import { ReactNode } from 'react';
import './PageLayout.css';

interface PageLayoutProps {
  title: string;
  screenId: string;
  children: ReactNode;
}

export default function PageLayout({ title, screenId, children }: PageLayoutProps) {
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
