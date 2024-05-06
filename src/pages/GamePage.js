import { Link } from "react-router-dom";
import "./GamePage.css";

export default function GamePage() {

  return (
    <div className="card-deck mt-5">
      <div className="card">
        <Link to="/games/tictactoe">
          <img className="card-img-top" src="/image/Noughts_crosses.png" alt="noughs_crosses" />
        </Link>
        <div className="card-body">
          <h5 className="card-title">Tic-Tac-Toe</h5>
          <p className="card-text">
            Tic-tac-toe, noughts and crosses, or Xs and Os is a paper-and-pencil
            game for two players who take turns marking the spaces in a
            three-by-three grid with X or O.
          </p>
          <p className="card-text">
            <small className="text-muted">Last updated 3 mins ago</small>
          </p>
        </div>
      </div>
      <div className="card">
        <img className="card-img-top" src="..." alt="" />
        <div className="card-body">
          <h5 className="card-title">Coming Soon</h5>
          <p className="card-text">
            This card has supporting text below as a natural lead-in to
            additional content.
          </p>
          <p className="card-text">
            <small className="text-muted">Last updated 3 mins ago</small>
          </p>
        </div>
      </div>
      <div className="card">
        <img className="card-img-top" src="..." alt="" />
        <div className="card-body">
          <h5 className="card-title">Coming Soon</h5>
          <p className="card-text">
          This card has supporting text below as a natural lead-in to
          additional content.
          </p>
          <p className="card-text">
            <small className="text-muted">Last updated 3 mins ago</small>
          </p>
        </div>
      </div>
    </div>
  );
}
