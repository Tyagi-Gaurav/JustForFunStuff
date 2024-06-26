import styles from "../pages/TicTacToe.module.css";
import { Box } from "@mui/material";

export default function Square({ id, value, onSquareClick }) {
  function getClassNames() {
    return styles.square + " " + styles[value.color];
  }

  return (
    <Box paddingX={0} >
      <button id={id} className={getClassNames()} onClick={onSquareClick}>
        {value.text}
      </button>
    </Box>
  );
}
