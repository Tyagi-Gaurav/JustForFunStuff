import { useEffect, useState } from "react";
import { updateWord, deleteWord } from "../api/backend_api";
import { searchGraphQL } from "../api/backend_graphql_api";
import AlertMessage from "../atoms/AlertMessage";
import { TextField, Button, Box } from "@mui/material";

export default function VocabWordEdit({ wordToEdit, listCallback }) {
  const [word, setWord] = useState("");
  const [oldWord, setOldWord] = useState("");
  const [meaning, setMeaning] = useState("");
  const [synonyms, setSynonym] = useState([]);
  const [example, setExample] = useState([]);
  const [success, setSuccess] = useState(false);
  const [error, setError] = useState(false);
  const [errorMsg, setErrorMsg] = useState("");

  const handleWordChange = (event) => {
    setSuccess(false);
    setError(false);
    setWord(event.target.value);
  };

  const handleMeaningChange = (event) => {
    setSuccess(false);
    setError(false);
    setMeaning(event.target.value);
  };

  const handleSynonymsChange = (event) => {
    setSuccess(false);
    setError(false);
    setSynonym(event.target.value.split(","));
  };

  const handleExamplesChange = (event) => {
    setSuccess(false);
    setError(false);
    setExample(event.target.value);
  };

  const handleSave = () => {
    updateWord(oldWord, {
      word: word,
      meanings: [
        {
          definition: meaning,
          synonyms: synonyms,
          examples: example,
        },
      ],
    })
      .then((value) => {
        setSuccess(true);
      })
      .catch((error) => {
        setError(true);
        setErrorMsg(error);
      });
  };

  const handleCancel = () => {
    listCallback();
  };

  const handleDelete = () => {
    deleteWord(oldWord)
      .then((value) => {
        listCallback();
      })
      .catch((error) => {
        setError(true);
        setErrorMsg(error);
      });
  };

  useEffect(() => {
    setOldWord(wordToEdit);
    searchGraphQL("WORD", wordToEdit).then((wordResponse) => {
      console.log("Word response: " + JSON.stringify(wordResponse));
      setWord(wordResponse.data.search["word"]);
      var meaning = wordResponse.data.search["meanings"][0];
      setSynonym(meaning["synonyms"]);
      setMeaning(meaning["definition"]);
      setExample(meaning["examples"]);
    });
  }, []);

  return (
    <>
      {success && <AlertMessage type="success" message="Success" />}
      {error && <AlertMessage type="error" message="{errorMsg}" />}
      <h2>Edit Word</h2>
      <form>
        <TextField
          type="text"
          variant="outlined"
          color="secondary"
          label="Word"
          value={word}
          fullWidth
          onChange={handleWordChange}
          required
          sx={{ mb: 4 }}
        />
        <TextField
          type="text"
          variant="outlined"
          color="secondary"
          label="Meaning"
          value={meaning}
          onChange={handleMeaningChange}
          fullWidth
          required
          sx={{ mb: 4 }}
        />
        <TextField
          type="text"
          variant="outlined"
          color="secondary"
          label="Synonyms"
          placeholder="synonym1, synonym2, etc."
          value={synonyms}
          onChange={handleSynonymsChange}
          fullWidth
          sx={{ mb: 4 }}
        />
        <TextField
          type="text"
          variant="outlined"
          color="secondary"
          label="Examples"
          placeholder="example1, example2, etc."
          value={example}
          onChange={handleExamplesChange}
          fullWidth
          sx={{ mb: 4 }}
        />
        <Box
          sx={{
            display: "flex",
            justifyContent: "space-between",
          }}
        >
          <Button
            variant="contained"
            color="secondary"
            type="submit"
            onClick={handleSave}
          >
            Submit
          </Button>
          <Button
            variant="outlined"
            color="error"
            type="submit"
            onClick={handleCancel}
          >
            Back
          </Button>
          <Button
            variant="outlined"
            color="error"
            type="submit"
            onClick={handleDelete}
          >
            Delete
          </Button>
        </Box>
      </form>
    </>
  );
}
