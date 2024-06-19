import axios from "axios";

export const getWords = (pageNum) => {
  return axios.get("/admin/v1/words/page/" + pageNum, {
    headers: {
      "accept-language": "application/json",
    },
  });
};

export const search = (searchType, searchValue) => {
  return axios.get(
    "/admin/v1/words/search?searchType=" + searchType + "&searchValue=" + searchValue,
    {
      headers: {
        "accept-language": "application/json",
        "Content-Type": "application/x-www-form-urlencoded",
      },
    }
  );
};

export const updateWord = (oldWord, word) => {
  return axios.put("/admin/v1/words/" + oldWord, word, {
    headers: {
      "Content-Type": "application/vnd+update.word.v1+json",
    },
  });
};

export const addWord = (word) => {
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
