import axios from "axios";

export const getWords = (body) => {
  return axios.get("/api/words", null, {
    headers: {
      "accept-language": "application/json"
    },
  });
};