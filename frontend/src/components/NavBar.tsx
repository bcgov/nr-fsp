import { useState, useRef, useEffect } from 'react';
import { Link, useLocation } from 'react-router-dom';
import './NavBar.css';

interface NavLeaf {
  label: string;
  to: string;
}

interface NavBranch {
  label: string;
  children: Array<NavLeaf | NavBranch>;
}

type NavNode = NavLeaf | NavBranch;

function isBranch(node: NavNode): node is NavBranch {
  return 'children' in node;
}

const NAV_ITEMS: NavBranch[] = [
  {
    label: 'Search',
    children: [
      { label: 'Search', to: '/search' },
      {
        label: 'Links',
        children: [
          { label: 'FTA',     to: '/links/fta' },
          { label: 'RESULTS', to: '/links/results' },
          { label: 'MapView', to: '/links/mapview' },
          { label: 'CIMS',    to: '/links/cims' },
        ],
      },
    ],
  },
  {
    label: 'Inbox',
    children: [
      { label: 'Inbox', to: '/inbox' },
    ],
  },
  {
    label: 'FSP',
    children: [
      { label: 'FSP Information',      to: '/fsp/information' },
      { label: 'Attachments',          to: '/fsp/attachments' },
      { label: 'Stocking Standards',   to: '/fsp/stocking-standards' },
      { label: 'FDU/Map',              to: '/fsp/fdu-map' },
      { label: 'Identified Areas/Map', to: '/fsp/identified-areas' },
      { label: 'Workflow',             to: '/fsp/workflow' },
      { label: 'History',              to: '/fsp/history' },
    ],
  },
  {
    label: 'Data Submission',
    children: [
      { label: 'XML Submission', to: '/data-submission/xml' },
    ],
  },
  {
    label: 'Admin',
    children: [
      { label: 'District Notification', to: '/admin/district-notification' },
    ],
  },
  {
    label: 'Reports',
    children: [
      { label: 'JCRS Reports', to: '/reports/jcrs' },
    ],
  },
];

function NavItem({ item }: { item: NavBranch }) {
  const [open, setOpen] = useState(false);
  const ref = useRef<HTMLLIElement>(null);
  const location = useLocation();

  const isActive = item.children.some(c =>
    isBranch(c)
      ? c.children.some(gc => !isBranch(gc) && location.pathname.startsWith(gc.to))
      : location.pathname.startsWith(c.to)
  );

  useEffect(() => {
    function handler(e: MouseEvent) {
      if (ref.current && !ref.current.contains(e.target as Node)) setOpen(false);
    }
    document.addEventListener('mousedown', handler);
    return () => document.removeEventListener('mousedown', handler);
  }, []);

  return (
    <li ref={ref} className={`navbar__item ${open ? 'is-open' : ''} ${isActive ? 'is-active' : ''}`}>
      <button
        className="navbar__trigger"
        onClick={() => setOpen(v => !v)}
        aria-expanded={open}
        aria-haspopup="true"
        type="button"
      >
        {item.label}
        <svg className="navbar__chevron" viewBox="0 0 10 6" aria-hidden="true">
          <path d="M0 0l5 6 5-6z" fill="currentColor" />
        </svg>
      </button>

      {open && (
        <ul className="navbar__dropdown" role="menu">
          {item.children.map(child => (
            <DropdownItem key={child.label} item={child} onClose={() => setOpen(false)} />
          ))}
        </ul>
      )}
    </li>
  );
}

interface DropdownItemProps {
  item: NavNode;
  onClose: () => void;
}

function DropdownItem({ item, onClose }: DropdownItemProps) {
  const [open, setOpen] = useState(false);

  if (isBranch(item)) {
    return (
      <li
        className={`navbar__dropdown-item navbar__dropdown-item--has-children ${open ? 'is-open' : ''}`}
        onMouseEnter={() => setOpen(true)}
        onMouseLeave={() => setOpen(false)}
        role="none"
      >
        <button className="navbar__dropdown-btn" type="button" aria-haspopup="true" aria-expanded={open}>
          {item.label}
          <svg className="navbar__chevron navbar__chevron--right" viewBox="0 0 6 10" aria-hidden="true">
            <path d="M0 0l6 5-6 5z" fill="currentColor" />
          </svg>
        </button>
        {open && (
          <ul className="navbar__dropdown navbar__dropdown--sub" role="menu">
            {item.children.map(child => (
              <DropdownItem key={child.label} item={child} onClose={onClose} />
            ))}
          </ul>
        )}
      </li>
    );
  }

  return (
    <li className="navbar__dropdown-item" role="none">
      <Link to={item.to} className="navbar__dropdown-link" role="menuitem" onClick={onClose}>
        {item.label}
      </Link>
    </li>
  );
}

export default function NavBar() {
  return (
    <nav className="navbar" aria-label="Main navigation">
      <div className="navbar__container">
        <ul className="navbar__list" role="list">
          {NAV_ITEMS.map(item => <NavItem key={item.label} item={item} />)}
        </ul>
      </div>
    </nav>
  );
}
