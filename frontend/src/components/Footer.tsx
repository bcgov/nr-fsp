import './Footer.css';

interface FooterLink {
  label: string;
  href: string;
}

const footerLinks: FooterLink[] = [
  { label: 'Home', href: '/' },
  { label: 'Disclaimer', href: 'https://www2.gov.bc.ca/gov/content/home/disclaimer' },
  { label: 'Privacy', href: 'https://www2.gov.bc.ca/gov/content/home/privacy' },
  { label: 'Accessibility', href: 'https://www2.gov.bc.ca/gov/content/home/accessible-government' },
  { label: 'Copyright', href: 'https://www2.gov.bc.ca/gov/content/home/copyright' },
  { label: 'Contact Us', href: 'https://www2.gov.bc.ca/gov/content/home/get-support' },
];

export default function Footer() {
  return (
    <footer className="bcgov-footer" role="contentinfo">
      {/* Gold divider */}
      <div className="bcgov-footer__divider" aria-hidden="true" />

      {/* Main footer body */}
      <div className="bcgov-footer__body">
        <div className="bcgov-footer__container bcgov-footer__body-inner">
          {/* Logo + tagline */}
          <div className="bcgov-footer__brand">
            <img
              src="https://www2.gov.bc.ca/StaticWebResources/static/gov3/images/gov_bc_logo.png"
              alt="Government of British Columbia"
              className="bcgov-footer__logo"
              onError={e => { (e.target as HTMLImageElement).style.display = 'none'; }}
            />
          </div>

          {/* Nav links */}
          <nav className="bcgov-footer__nav" aria-label="Footer navigation">
            <ul className="bcgov-footer__links">
              {footerLinks.map(link => (
                <li key={link.label}>
                  <a
                    href={link.href}
                    className="bcgov-footer__link"
                    target={link.href.startsWith('http') ? '_blank' : undefined}
                    rel={link.href.startsWith('http') ? 'noopener noreferrer' : undefined}
                  >
                    {link.label}
                  </a>
                </li>
              ))}
            </ul>
          </nav>
        </div>
      </div>

      {/* Copyright bar */}
      <div className="bcgov-footer__copyright">
        <div className="bcgov-footer__container">
          <p>
            © {new Date().getFullYear()} Government of British Columbia
          </p>
        </div>
      </div>
    </footer>
  );
}
