import styles from "../pages/TicTacToe.module.css";


export default function Square({ id, value, onSquareClick }) {
    function getClassNames() {
      return styles.square + " " + styles[value.color];
    }
  
    return (
      <button id={id} className={getClassNames()} onClick={onSquareClick}>
        {value.text}
      </button>
    );
  }