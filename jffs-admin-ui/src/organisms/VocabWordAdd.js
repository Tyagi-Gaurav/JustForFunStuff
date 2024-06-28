import { useState } from "react";
import { addWord } from "../api/backend_api";
import AlertMessage from "../atoms/AlertMessage";
import { TextField, Button } from "@mui/material";

export default function VocabWordAdd({ listCallback }) {
  const [word, setWord] = useState("");
  const [meaning, setMeaning] = useState("");
  const [synonyms, setSynonym] = useState([]);
  const [example, setExample] = useState([]);
  const [success, setSuccess] = useState(false);

  const handleWordChange = (event) => {
    setWord(event.target.value);
  };

  const handleMeaningChange = (event) => {
    setMeaning(event.target.value);
  };

  const handleSynonymsChange = (event) => {
    setSynonym(event.target.value.split(","));
  };

  const handleExamplesChange = (event) => {
    setExample(event.target.value.split(","));
  };

  const handleSave = (event) => {
    event.preventDefault()
    console.log("Inside Handle Save");
    addWord({
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
        setSynonym([]);
        setExample([]);
        setMeaning("");
        setWord("");
      })
      .catch((error) => {
        //TODO Show error message
        console.log("Error occurred " + error);
      });
  };

  return (
    <>
      {success && <AlertMessage type="success" message="Success" />}
      <h2>Add Word</h2>
      <form onSubmit={handleSave}>
        <TextField
          type="text"
          variant="outlined"
          color="secondary"
          label="Word"
          value={word}
          fullWidth
          onChange={handleWordChange}
          required
          sx={{mb: 4}}
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
        <Button variant="outlined" color="secondary" type="submit">Submit</Button>
      </form>
    </>
  );
}
