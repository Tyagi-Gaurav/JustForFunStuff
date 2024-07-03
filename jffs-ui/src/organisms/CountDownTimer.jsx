import { useEffect, useRef, useState } from "react";
import styles from "./countdowntimer.module.css";
import { Typography } from "@mui/material";

export default function CountDownTimer(props) {
  const [seconds, setSeconds] = useState(parseInt(props.inputDelay));
  var timer = 0;
  const countRef = useRef(props.inputDelay);

  useEffect(() => {
    setSeconds(parseInt(props.inputDelay));
    countRef.current = props.inputDelay;
    startTimer();
  }, [props.ready]);

  const startTimer = () => {
    if (!timer && props.inputDelay > 0) {
      timer = setInterval(countDown, 1000);
    }
  };

  function countDown() {
    let newSeconds = countRef.current - 1;
    setSeconds(newSeconds);
    countRef.current = newSeconds;

    if (newSeconds <= 0) {
      props.action();
      clearInterval(timer);
      timer = null;
    }
  }

  return (
    <Typography
      level="h1"
      justifyContent="center"
      data-testid="countdown"
      className={styles.circle}
      fontSize={60}
      textAlign="center"
    >
      {seconds}
    </Typography>
  );
}
