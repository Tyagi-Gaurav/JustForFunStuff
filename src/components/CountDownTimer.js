import { useEffect, useRef, useState } from "react";
import styles from "./countdowntimer.module.css";

export default function CountDownTimer(props) {
  const [seconds, setSeconds] = useState(parseInt(props.inputDelay));
  const [readyToRun, setReadyToRun] = useState(props.ready);
  var timer = 0;
  const countRef = useRef(props.inputDelay);

  useEffect(() => {
    setReadyToRun(props.ready);
    setSeconds(parseInt(props.inputDelay));
    if (readyToRun) {
      countRef.current = props.inputDelay;
      startTimer();
    }
  }, [props]);

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

  return <label data-testid="countdown" className={styles.circle}>{seconds}</label>;
}
