import axios from "axios";

export const getWords = (body) => {
  return axios.get("/api/v1/words", null, {
    headers: {
      "accept-language": "application/json"
    },
  });
};