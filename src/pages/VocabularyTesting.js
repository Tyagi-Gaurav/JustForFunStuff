import { useState } from "react";
import styles from "./VocabularyTesting.module.css";
import { getWords } from "../api/vocab";
import FloatableTextAreaWithLabel from "../components/FloatableTextAreaWithLabel";
import CountDownTimer from "../components/CountDownTimer";

function getRandomInt(max) {
  return Math.floor(Math.random() * max);
}

function formattedArray(words) {
  if (words) {
    return words.join("<br/>");
  } else {
    return "";
  }
}

export default function VocabularyTesting() {
  const [word, setWord] = useState("");
  const [countDownValue, setCountDownValue] = useState(3);
  const [readyToRun, setReadyToRun] = useState(false);
  const [meaning, setMeaning] = useState("");
  const [synonyms, setSynonym] = useState("");
  const [example, setExample] = useState("");
  const [inProgress, setInProgress] = useState(false);

  const handleClick = (event) => {
    event.preventDefault();
    getWords()
      .then((response) => {
        var data = response.data["words"];
        var index = getRandomInt(data.length);
        var selectedWord = data[index];
        setWord(selectedWord["word"]);
        setSynonym(formattedArray(selectedWord["synonyms"]));
        setMeaning(formattedArray(selectedWord["meaning"]));
        setExample(formattedArray(selectedWord["examples"]));
        setInProgress(true);
        setCountDownValue(countDownValue);
        setReadyToRun(true);
      })
      .catch((error) => {
        console.log(error);
      });
  };

  const doActionWhenTimerExpires = () => {
    console.log("Timer expired");
  }

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
            <CountDownTimer inputDelay={countDownValue} ready={readyToRun} action={doActionWhenTimerExpires} />
          ) : (
            <label>
              Can you think of the meaning before the timer runs out?
            </label>
          )}
        </div>
      </div>

      {inProgress && (
        <FloatableTextAreaWithLabel
          label="Meaning"
          testId="meaning-text"
          text={meaning}
        />
      )}

      {inProgress && synonyms && (
        <FloatableTextAreaWithLabel
          label="Synonyms"
          testId="synonym-text"
          text={synonyms}
        />
      )}

      {inProgress && example && (
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
