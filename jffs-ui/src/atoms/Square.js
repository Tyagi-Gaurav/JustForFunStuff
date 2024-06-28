import styles from "../pages/TicTacToe.module.css";
import { Box, Button, Typography } from "@mui/material";

export default function Square({ id, value, onSquareClick }) {
  function getColor() {
    if (value.color === "ocolor") {
      return "#f2613f";
    }
    if (value.color === "xcolor") {
      return "#481e14";
    }
    
    return "#ffc94a";
  }

  return (
    <Box paddingX={0} >
      <Button variant="outlined" id={id} className={styles.square} onClick={onSquareClick}>
        <Typography variant="h3" color={getColor()}>{value.text}</Typography>
      </Button>
    </Box>
  );
}
