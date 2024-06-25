import { Link } from "react-router-dom";
import "./GamePage.css";

export default function GamePage() {
  return (
    <div className="row row-cols-1 row-cols-md-3 g-4">
      <div className="col">
        <div className="card h-100">
          <Link to="/games/tictactoe">
            <img
              className="card-img-top"
              src="/image/NoughtsCrosses.png"
              alt="noughts_crosses"
            />
          </Link>
          <div className="card-body">
            <h5 className="card-title">Tic-Tac-Toe</h5>
            <p className="card-text">
              Tic-tac-toe, noughts and crosses, or Xs and Os is a
              paper-and-pencil game for two players who take turns marking the
              spaces in a three-by-three grid with either X or O.
            </p>
          </div>
        </div>
      </div>
      <div className="col">
        <div className="card h-100">
          <Link to="/games/vocabtesting">
            <img
              className="card-img-top"
              src="/image/english_vocab.png"
              alt="vocab_test"
            />
          </Link>
          <div className="card-body">
            <h5 className="card-title">Vocabulary Revision</h5>
            <p className="card-text">
              Let's have some fun with a vocabulary game where we learn new
              words—who's ready to play and become a word wizard?
            </p>
          </div>
        </div>
      </div>
      <div className="col">
        <div className="card h-100">
          <img src="..." className="card-img-top" alt="..." />
          <div className="card-body">
            <h5 className="card-title">Coming Soon...</h5>
            <p className="card-text">
              Coming Soon..
            </p>
          </div>
        </div>
      </div>
    </div>
  );
}
