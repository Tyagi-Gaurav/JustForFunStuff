import { useState } from "react";
import { addWordGraphQL } from "../api/backend_graphql_api";
import AlertMessage from "../atoms/AlertMessage";
import { TextField, Button } from "@mui/material";

export default function VocabWordAdd({ listCallback }) {
  const [word, setWord] = useState("");
  const [meaning, setMeaning] = useState("");
  const [synonyms, setSynonym] = useState([]);
  const [example, setExample] = useState([]);
  const [success, setSuccess] = useState(false);
  const [error, setError] = useState("");

  const handleWordChange = (event) => {
    setWord(event.target.value);
    setSuccess(false);
    setError("");
  };

  const handleMeaningChange = (event) => {
    setMeaning(event.target.value);
    setSuccess(false);
    setError("");
  };

  const handleSynonymsChange = (event) => {
    setSynonym(event.target.value.split(","));
    setSuccess(false);
    setError("");
  };

  const handleExamplesChange = (event) => {
    setExample(event.target.value.split(","));
    setSuccess(false);
    setError("");
  };

  const handleSave = (event) => {
    event.preventDefault()
    addWordGraphQL({
      word,
      meanings: [{
          definition: meaning,
          synonyms: synonyms,
          examples: example,
        }]})
    .then((value) => {
        setSuccess(true);
        setError("");
        setSynonym([]);
        setExample([]);
        setMeaning("");
        setWord("");
      })
    .catch((error) => {
        setError(error);
    });
  };

  return (
    <>
      {success && <AlertMessage type="success" message="Success" />}
      {error && <AlertMessage type="danger" message={error} />}
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
