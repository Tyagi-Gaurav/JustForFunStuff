import { useEffect, useState } from "react";
import { getWords } from "../api/vocab";
import CountDownTimer from "../components/CountDownTimer";
import FloatableTextAreaWithLabel from "../components/FloatableTextAreaWithLabel";
import styles from "./VocabularyTesting.module.css";

function formattedArray(words) {
  if (words) {
    return words.join(", ");
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
  const [buttonText, setButtonText] = useState("Begin");

  useEffect(() => {
    if (setInProgress) {
      getWords()
        .then((response) => {
          var data = response.data["words"];
          setAllWords(data);
        })
        .catch((error) => {
          console.log(error);
        });
      }
  }, [inProgress]);

  const displayWord = (index) => {
    var selectedWord = allWords[index];
    setInProgress(true);
    setWord(selectedWord["word"]);
    setSynonym(formattedArray(selectedWord["synonyms"]));
    setMeaning(formattedArray(selectedWord["meaning"]));
    setExample(formattedArray(selectedWord["examples"]));
    setCountDownValue(countDownValue);
    setReadyToRun(readyToRun + 1);
    setTimerExpired(false);
    setCurrentWordCount(index + 1);
    setButtonText("Next");
  };

  const handleClick = (event) => {
    event.preventDefault();
    if (currentWordCount < allWords.length) {
      displayWord(currentWordCount);
    } else {
      displayWord(0);
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
        <div className="accordion row pt-5" id="mean-syn-exa">
          <div className="accordion-item p-0">
            <h2 className="accordion-header">
              <button
                className="accordion-button"
                type="button"
                data-bs-toggle="collapse"
                data-bs-target="#collapseOne"
                aria-expanded="true"
                aria-controls="collapseOne"
              >
                Meaning
              </button>
            </h2>
            <div
              id="collapseOne"
              className="accordion-collapse collapse show"
              data-bs-parent="#mean-syn-exa"
            >
              <div className="accordion-body">
                <strong data-testid="meanings-text">{meaning}</strong>
              </div>
            </div>
          </div>
          <div className="accordion-item  p-0">
            <h2 className="accordion-header">
              <button
                className="accordion-button collapsed"
                type="button"
                data-bs-toggle="collapse"
                data-bs-target="#collapseTwo"
                aria-expanded="false"
                aria-controls="collapseTwo"
              >
                Synonyms
              </button>
            </h2>
            <div
              id="collapseTwo"
              className="accordion-collapse collapse"
              data-bs-parent="#mean-syn-exa"
            >
              <div className="accordion-body">
                <strong data-testid="synonym-text">{synonyms}</strong>
              </div>
            </div>
          </div>
          <div className="accordion-item  p-0">
            <h2 className="accordion-header">
              <button
                className="accordion-button collapsed"
                type="button"
                data-bs-toggle="collapse"
                data-bs-target="#collapseThree"
                aria-expanded="false"
                aria-controls="collapseThree"
              >
                Examples
              </button>
            </h2>
            <div
              id="collapseThree"
              className="accordion-collapse collapse"
              data-bs-parent="#mean-syn-exa"
            >
              <div className="accordion-body">
                <strong data-testid="examples-text">{example}</strong>
              </div>
            </div>
          </div>
        </div>
      )}

      <div className="row">
        <div
          className={
            styles.centered_button + " col-sm-12 justify-content-center pb-10"
          }
        >
          <button className="btn btn-primary" onClick={handleClick}>
            {buttonText}
          </button>
        </div>
      </div>
    </div>
  );
}
