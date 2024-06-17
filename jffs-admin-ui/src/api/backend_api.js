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