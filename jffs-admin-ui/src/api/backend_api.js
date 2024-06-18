import axios from "axios";

export const getWords = (pageNum) => {
  return axios.get("/admin/v1/words/" + pageNum, {
    headers: {
      "accept-language": "application/json"
    },
  });
};

export const getWord = (word) => {
  return axios.get("/admin/v1/word/" + word, {
    headers: {
      "accept-language": "application/json"
    },
  });
};

export const updateWord = (oldWord, word) => {
  return axios.put("/admin/v1/word/" + oldWord, word, {
    headers: {
      "Content-Type": "application/vnd+update.word.v1+json"
    },
  });
};

export const addWord = (word) => {
  return axios.post("/admin/v1/word", word, {
    headers: {
      "Content-Type": "application/vnd+add.word.v1+json"
    },
  });
};

export const deleteWord = (word) => {
  return axios.delete("/admin/v1/word/" + word, {
    headers: {
      "Content-Type": "application/vnd+delete.word.v1+json"
    },
  });
};