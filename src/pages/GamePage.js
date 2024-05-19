import { Link } from "react-router-dom";
import "./GamePage.css";

export default function GamePage() {
  return (
    <div className="card-deck mt-5">
      <div className="card">
        <Link to="/games/tictactoe">
          <img
            className="card-img-top"
            src="/image/Noughts_crosses.png"
            alt="noughts_crosses"
          />
        </Link>
        <div className="card-body">
          <h5 className="card-title">Tic-Tac-Toe</h5>
          <p className="card-text">
            Tic-tac-toe, noughts and crosses, or Xs and Os is a paper-and-pencil
            game for two players who take turns marking the spaces in a
            three-by-three grid with either X or O.
          </p>
        </div>
      </div>
      <div className="card">
        <Link to="/games/vocabtesting">
          <img className="card-img-top" src="/image/VocabRevision.png" alt="vocab_test" />
        </Link>
        <div className="card-body">
          <h5 className="card-title">Vocabulary Revision</h5>
          <p className="card-text">
            Let's have some fun with a vocabulary game where we learn new
            words—who's ready to play and become a word wizard?
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
        </div>
      </div>
    </div>
  );
}
