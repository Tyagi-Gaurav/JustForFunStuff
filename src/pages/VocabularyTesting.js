import { useEffect, useState } from "react";
import { getWords } from "../api/vocab";
import CountDownTimer from "../organisms/CountDownTimer";
import styles from "./VocabularyTesting.module.css";
import AlertMessage from "../atoms/AlertMessage";
import TextInABox from "../atoms/TextInABox";
import Heading from "../atoms/Heading";
import { Button, Box, Typography } from "@mui/material";
import Accordion from "@mui/material/Accordion";
import AccordionSummary from "@mui/material/AccordionSummary";
import ExpandMoreIcon from "@mui/icons-material/ExpandMore";
import AccordionDetails from "@mui/material/AccordionDetails";
import { blue } from "@mui/material/colors";

function formattedArray(words) {
  if (words) {
    return words.join(", ");
  } else {
    return "";
  }
}

export default function VocabularyTesting() {
  const [allWords, setAllWords] = useState();
  const [word, setWord] = useState("");
  const [countDownValue, setCountDownValue] = useState(3);
  const [readyToRun, setReadyToRun] = useState(0);
  const [meaning, setMeaning] = useState("");
  const [synonyms, setSynonyms] = useState("");
  const [example, setExample] = useState("");
  const [inProgress, setInProgress] = useState(false);
  const [timerExpired, setTimerExpired] = useState(true);
  const [currentWordCount, setCurrentWordCount] = useState(0);
  const [error, setError] = useState(false);

  useEffect(() => {
    if (!inProgress) {
      getWords()
        .then((response) => {
          let data = response.data["words"];
          //          console.log("Response Data Received in UI: " + JSON.stringify(response));
          //          console.log("Data Received in UI: " + JSON.stringify(data));
          setAllWords(data);
        })
        .catch((error) => {
          console.log(error);
        });
    }
  }, [inProgress]);

  const displayWord = (index) => {
    if (allWords && allWords !== "") {
      let selectedWord = allWords[index];
      setInProgress(true);
      setWord(selectedWord["word"]);
      let meaning = selectedWord["meanings"][0];
      setSynonyms(formattedArray(meaning["synonyms"]));
      setMeaning(meaning["definition"]);
      setExample(formattedArray(meaning["examples"]));
      setCountDownValue(countDownValue);
      setReadyToRun(readyToRun + 1);
      setTimerExpired(false);
      setCurrentWordCount(index + 1);
    } else {
      //      console.log("Setting Error to true while displaying words.")
      setError(true);
    }
  };

  const handleClick = (event) => {
    event.preventDefault();
    if (allWords) {
      if (currentWordCount < allWords.length) {
        displayWord(currentWordCount);
      } else {
        displayWord(0);
      }
    } else {
      //      console.log("Setting Error to true while handling begin event.")
      setError(true);
    }
  };

  const doActionWhenTimerExpires = () => {
    setTimerExpired(true);
  };

  return (
    <Box>
      <Heading headingMessage="Test your Vocabulary" color="#ff8a08" />

      {!inProgress && !word && (
        <Box>
          <Heading
            headingMessage="Can you think of the meaning before the timer runs out?"
            color="#ff8a08"
          />
        </Box>
      )}

      {error && (
        <Box justifyContent="center" display="flex">
          <AlertMessage
            type="danger"
            message="There seems to be some problem. Please try again later"
          />
        </Box>
      )}

      {inProgress && word && (
        <Box>
          <TextInABox text={word} testId="word-text" />
        </Box>
      )}

      {!error && inProgress && (
        <Box display="flex" justifyContent="center">
          <CountDownTimer
            inputDelay={countDownValue}
            ready={readyToRun}
            action={doActionWhenTimerExpires}
          />
        </Box>
      )}

      {inProgress && timerExpired && (
        <Box paddingTop="10px">
          <Accordion defaultExpanded>
            <AccordionSummary
              expandIcon={<ExpandMoreIcon />}
              aria-controls="panel1-content"
              id="panel1-header"
              sx={{ bgcolor: "primary.light" }}
            >
              Meaning
            </AccordionSummary>
            <AccordionDetails>
              <Typography>
                <strong data-testid="meanings-text">{meaning}</strong>
              </Typography>
            </AccordionDetails>
          </Accordion>
          <Accordion defaultExpanded>
            <AccordionSummary
              expandIcon={<ExpandMoreIcon />}
              aria-controls="panel1-content"
              id="panel1-header"
              sx={{ bgcolor: "secondary.light" }}
            >
              Synonyms
            </AccordionSummary>
            <AccordionDetails>
              <Typography>
                <strong data-testid="synonym-text">{synonyms}</strong>
              </Typography>
            </AccordionDetails>
          </Accordion>
          <Accordion defaultExpanded>
            <AccordionSummary
              expandIcon={<ExpandMoreIcon />}
              aria-controls="panel1-content"
              id="panel1-header"
              sx={{ bgcolor: "warning.light" }}
            >
              Examples
            </AccordionSummary>
            <AccordionDetails>
              <Typography>
                <strong data-testid="examples-text">{example}</strong>
              </Typography>
            </AccordionDetails>
          </Accordion>
        </Box>
      )}

      <Box display="flex" justifyContent="center" paddingTop={5}>
        {!inProgress && (
          <Box
            justifyContent="center"
            alignSelf="center"
            paddingTop={4}
            width="100"
          >
            <Button variant="contained" onClick={handleClick}>
              Begin
            </Button>
          </Box>
        )}

        {inProgress && timerExpired && (
          <Button variant="contained" onClick={handleClick}>
            Next
          </Button>
        )}
      </Box>
    </Box>
  );
}
