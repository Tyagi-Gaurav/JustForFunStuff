import { useEffect, useState } from "react";
import { getWords } from "../api/vocab";
import CountDownTimer from "../organisms/CountDownTimer";
import styles from "./VocabularyTesting.module.css";
import AlertMessage from "../atoms/AlertMessage";
import TextInABox from "../atoms/TextInABox";
import Heading from "../atoms/Heading";

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
  const [error, setError] = useState(false);

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
    if (allWords && allWords.length > 0) {
      var selectedWord = allWords[index];
      setInProgress(true);
      setWord(selectedWord["word"]);
      var meaning = selectedWord["meanings"][0];
      setSynonym(formattedArray(meaning["synonyms"]));
      setMeaning(meaning["definition"]);
      setExample(formattedArray(meaning["examples"]));
      setCountDownValue(countDownValue);
      setReadyToRun(readyToRun + 1);
      setTimerExpired(false);
      setCurrentWordCount(index + 1);
      setButtonText("Next");
    } else {
      setError(true);
    }
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
      <Heading headingStyle={styles.heading + " text-center"} headingMessage="Test your Vocabulary"/>

      {error && 
        (<AlertMessage type="danger" message="There seems to be some problem. Please try again later"/>)
      }

      {inProgress && word && (
        <TextInABox word={word} testId="word-text"/>
      )}

      {!error && (
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
      )}

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
            <div id="collapseOne" className="accordion-collapse collapse show">
              <div className="accordion-body">
                <strong data-testid="meanings-text">{meaning}</strong>
              </div>
            </div>
          </div>
          <div className="accordion-item  p-0">
            <h2 className="accordion-header">
              <button
                className="accordion-button"
                type="button"
                data-bs-toggle="collapse"
                data-bs-target="#collapseTwo"
                aria-expanded="false"
                aria-controls="collapseTwo"
              >
                Synonyms
              </button>
            </h2>
            <div id="collapseTwo" className="accordion-collapse collapse show">
              <div className="accordion-body">
                <strong data-testid="synonym-text">{synonyms}</strong>
              </div>
            </div>
          </div>
          <div className="accordion-item  p-0">
            <h2 className="accordion-header">
              <button
                className="accordion-button collapse show"
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
              className="accordion-collapse collapse show"
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
