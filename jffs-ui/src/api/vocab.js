import axios from "axios";

export const getWords = () => {
  return axios.get("/api/v1/words", null, {
    headers: {
      "accept-language": "application/json"
    },
  });
};

export const postTrackingEvent = (body) => {
  return axios.post("/api/v1/ui/event", body, {
    headers: {
      "accept-language": "application/json"
    },
  });
};