import axios from "axios";

export const getWords = (pageNum) => {
  return axios.get("/admin/v1/words/" + pageNum, null, {
    headers: {
      "accept-language": "application/json"
    },
  });
};

export const getWord = (word) => {
  return axios.get("/admin/v1/word/" + word, null, {
    headers: {
      "accept-language": "application/json"
    },
  });
};

export const updateWord = (oldWord, word) => {
  return axios.put("/admin/v1/word/" + oldWord, word, {
    headers: {
      "content-type": "application/vnd+update.word.v1+json"
    },
  });
};

export const addWord = (word) => {
  return axios.post("/admin/v1/word", {
    headers: {
      "content-type": "application/vnd+add.word.v1+json"
    },
  });
};