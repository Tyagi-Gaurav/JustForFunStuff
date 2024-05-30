import "./NavBar.css";

export default function NavBar() {
  return (
    <nav className="navbar navbar-expand-lg navbar-dark navbar-custom">
      <a className="navbar-brand navbar-text" href="/">
        <img src="/image/JFF_Logo.jpeg" alt="" width="35" height="35"/>
      </a>
      <button
        className="navbar-toggler"
        type="button"
        data-toggle="collapse"
        data-target="#navbarNavAltMarkup"
        aria-controls="navbarNavAltMarkup"
        aria-expanded="false"
        aria-label="Toggle navigation"
      >
        <span className="navbar-toggler-icon"></span>
      </button>
      <div className="collapse navbar-collapse" id="navbarNavAltMarkup">
        <div className="navbar-nav">
          <a className="nav-item nav-link active navbar-text" href="/">
            Games <span className="sr-only"></span>
          </a>
        </div>
      </div>
    </nav>
  );
}
