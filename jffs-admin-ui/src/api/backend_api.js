import axios from "axios";

export const addWord = (word) => {
  //console.log("Adding word: " + word);
  return axios.post("/admin/v1/words", word, {
    headers: {
      "Content-Type": "application/vnd+add.word.v1+json",
    },
  });
};
