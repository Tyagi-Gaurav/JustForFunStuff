import { useState } from "react";
import { getWords } from "../api/vocab";
import CountDownTimer from "../components/CountDownTimer";
import FloatableTextAreaWithLabel from "../components/FloatableTextAreaWithLabel";
import styles from "./VocabularyTesting.module.css";

function formattedArray(words) {
  if (words) {
    return words.join("<br/>");
  } else {
    return "";
  }
}

export default function VocabularyTesting() {
  const [allWords, setAllWords] = useState([]);
  const [word, setWord] = useState("");
  const [countDownValue, setCountDownValue] = useState(3);
  const [readyToRun, setReadyToRun] = useState(0);
  const [meaning, setMeaning] = useState("");
  const [synonyms, setSynonym] = useState("");
  const [example, setExample] = useState("");
  const [inProgress, setInProgress] = useState(false);
  const [timerExpired, setTimerExpired] = useState(true);
  const [currentWordCount, setCurrentWordCount] = useState(0);

  const displayWord = (selectedWord) => {
    setInProgress(true);
    setWord(selectedWord["word"]);
    setSynonym(formattedArray(selectedWord["synonyms"]));
    setMeaning(formattedArray(selectedWord["meaning"]));
    setExample(formattedArray(selectedWord["examples"]));
    setCountDownValue(countDownValue);
    setReadyToRun(readyToRun + 1);
    setTimerExpired(false);
    setCurrentWordCount(currentWordCount + 1);
  };

  const handleClick = (event) => {
    event.preventDefault();

    if (currentWordCount === 0) {
      getWords()
        .then((response) => {
          var data = response.data["words"];
          setAllWords(data);
          displayWord(data[0]);
        })
        .catch((error) => {
          console.log(error);
        });
    } else {
      if (currentWordCount < allWords.length) {
        displayWord(allWords[currentWordCount]);
      } else {
        console.log("Out of words for now");
      }
    }
  };

  const doActionWhenTimerExpires = () => {
    setTimerExpired(true);
  };

  return (
    <div>
      <div className={styles.heading + " text-center"}>
        <h1>Test your Vocabulary</h1>
      </div>
      {inProgress && (
        <div className="row mb-2 pr-0">
          <div className="col-sm-12">
            <h3
              className="display-1 rounded bg-light word"
              data-testid="word-text"
            >
              {word}
            </h3>
          </div>
        </div>
      )}

      <div className="row">
        <div className="col-sm-12 text-center">
          {inProgress ? (
            <CountDownTimer
              inputDelay={countDownValue}
              ready={readyToRun}
              action={doActionWhenTimerExpires}
            />
          ) : (
            <label>
              Can you think of the meaning before the timer runs out?
            </label>
          )}
        </div>
      </div>

      {inProgress && timerExpired && (
        <FloatableTextAreaWithLabel
          label="Meaning"
          testId="meaning-text"
          text={meaning}
        />
      )}

      {inProgress && synonyms && timerExpired && (
        <FloatableTextAreaWithLabel
          label="Synonyms"
          testId="synonym-text"
          text={synonyms}
        />
      )}

      {inProgress && example && timerExpired && (
        <FloatableTextAreaWithLabel
          label="Examples"
          testId="example-text"
          text={example}
        />
      )}

      <div className="row">
        <div
          className={
            styles.centered_button + " col-sm-12 justify-content-center pb-10"
          }
        >
          <button className="btn btn-primary" onClick={handleClick}>
            {inProgress ? "Next" : "Begin"}
          </button>
        </div>
      </div>
    </div>
  );
}
