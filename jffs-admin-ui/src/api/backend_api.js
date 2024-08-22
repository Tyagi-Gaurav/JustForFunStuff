import axios from "axios";

export const updateWord = (oldWord, word) => {
  return axios.put("/admin/v1/words/" + oldWord, word, {
    headers: {
      "Content-Type": "application/vnd+update.word.v1+json",
    },
  });
};

export const addWord = (word) => {
  //console.log("Adding word: " + word);
  return axios.post("/admin/v1/words", word, {
    headers: {
      "Content-Type": "application/vnd+add.word.v1+json",
    },
  });
};

export const deleteWord = (word) => {
  return axios.delete("/admin/v1/words/" + word, {
    headers: {
      "Content-Type": "application/vnd+delete.word.v1+json",
    },
  });
};
