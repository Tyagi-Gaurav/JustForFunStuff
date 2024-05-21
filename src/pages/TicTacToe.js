import { useState } from "react";
import styles from "./TicTacToe.module.css";
import Square from "../components/Square.js"

export default function TicTacToe() {
  const [squares, setSquares] = useState(
    Array(9).fill({ text: "", color: "" })
  );
  const [xIsNext, setXIsNext] = useState(true);
  const line_won = calculateWinner(squares);
  let status;

  if (line_won) {
    if (line_won.length > 0) {
      status = "Winner " + squares[line_won[0]].text;
      line_won.map(
        (lc) => (squares[lc] = { text: squares[lc].text, color: "wincolor" })
      );
    } else {
      status = "Match Draw";
    }
  } else {
    status = "Next Player to Play: " + (xIsNext ? "X" : "O");
  }

  function resetGame() {
    setSquares(Array(9).fill({ text: "", color: "" }));
    setXIsNext(true);
  }

  function handleClick(index) {
    if (calculateWinner(squares)) {
      return;
    }

    const nextSquares = squares.slice(); //creates a copy of the squares array

    if (nextSquares[index].text === "") {
      if (xIsNext) {
        nextSquares[index] = { text: "X", color: "xcolor" };
      } else {
        nextSquares[index] = { text: "O", color: "ocolor" };
      }
      setSquares(nextSquares);
      setXIsNext(!xIsNext);
    }
  }

  return (
    <>
      <div className={styles.page}>
        <div className={"text-center " + styles.heading}>
          <h1>Tic-Tac-Toe</h1>
        </div>
        <div className={styles.status + " pt-5"}>
          <p className="text-center">{status}</p>
        </div>
        <div className={styles.row}>
          <Square
            id="square-1"
            value={squares[0]}
            onSquareClick={() => handleClick(0)}
          />
          <Square
            id="square-2"
            value={squares[1]}
            onSquareClick={() => handleClick(1)}
          />
          <Square
            id="square-3"
            value={squares[2]}
            onSquareClick={() => handleClick(2)}
          />
        </div>
        <div className={styles.row}>
          <Square
            id="square-4"
            value={squares[3]}
            onSquareClick={() => handleClick(3)}
          />
          <Square
            id="square-5"
            value={squares[4]}
            onSquareClick={() => handleClick(4)}
          />
          <Square
            id="square-6"
            value={squares[5]}
            onSquareClick={() => handleClick(5)}
          />
        </div>
        <div className={styles.row}>
          <Square
            id="square-7"
            value={squares[6]}
            onSquareClick={() => handleClick(6)}
          />
          <Square
            id="square-8"
            value={squares[7]}
            onSquareClick={() => handleClick(7)}
          />
          <Square
            id="square-9"
            value={squares[8]}
            onSquareClick={() => handleClick(8)}
          />
        </div>
        <div className={styles.replay + " justify-content-center"}>
          <button
            className="btn btn-primary"
            onClick={() => resetGame()}>
            Restart
          </button>
        </div>
      </div>
    </>
  );
}

function calculateWinner(squares) {
  const lines = [
    [0, 1, 2],
    [3, 4, 5],
    [6, 7, 8],
    [0, 3, 6],
    [1, 4, 7],
    [2, 5, 8],
    [0, 4, 8],
    [2, 4, 6],
  ];

  for (let i = 0; i < lines.length; i++) {
    const [a, b, c] = lines[i];
    if (
      squares[a].text !== "" &&
      squares[a].text === squares[b].text &&
      squares[b].text === squares[c].text
    ) {
      return [a, b, c];
    }
  }

  for (let i = 0; i < 9; i++) {
    if (squares[i].text === "") {
      return null;
    }
  }

  return [];
}
