import { useState } from "react";
import "./TicTacToe.css";

function Square({ value, onSquareClick }) {
  function getClassNames() {
    return "square " + value.color;
  }

  return (
    <button className={getClassNames()} onClick={onSquareClick}>
      {value.text}
    </button>
  );
}

export default function Board() {
  const [squares, setSquares] = useState(
    Array(9).fill({ text: "", color: "" })
  );
  const [xIsNext, setXIsNext] = useState(true);

  const line_won = calculateWinner(squares);
  let status;

  if (line_won) {
    status = "Winner " + squares[line_won[0]].text;
    line_won.map((lc) => squares[lc] = {text: squares[lc].text, color: "wincolor"})
  } else {
    status = "Next Player: " + (xIsNext ? "X" : "O");
  }

  function handleClick(index) {
    if (calculateWinner(squares)) {
      return;
    }

    const nextSquares = squares.slice(); //creates a copy of the squares array

    if (xIsNext) {
      nextSquares[index] = { text: "X", color: "xcolor" };
    } else {
      nextSquares[index] = { text: "O", color: "ocolor" };
    }
    setSquares(nextSquares);
    setXIsNext(!xIsNext);
  }

  return (
    <div className="page">
      <div className="heading">Tic-Tac-Toe</div>
      <div className="status">{status}</div>
      <div className="board">
        <div>
          <Square value={squares[0]} onSquareClick={() => handleClick(0)} />
          <Square value={squares[1]} onSquareClick={() => handleClick(1)} />
          <Square value={squares[2]} onSquareClick={() => handleClick(2)} />
        </div>
        <div>
          <Square value={squares[3]} onSquareClick={() => handleClick(3)} />
          <Square value={squares[4]} onSquareClick={() => handleClick(4)} />
          <Square value={squares[5]} onSquareClick={() => handleClick(5)} />
        </div>
        <div>
          <Square value={squares[6]} onSquareClick={() => handleClick(6)} />
          <Square value={squares[7]} onSquareClick={() => handleClick(7)} />
          <Square value={squares[8]} onSquareClick={() => handleClick(8)} />
        </div>
      </div>
    </div>
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
      squares[a].text != '' &&
      squares[a].text === squares[b].text &&
      squares[b].text === squares[c].text
    ) {
      return [a, b, c];
    }
  }

  return null;
}
