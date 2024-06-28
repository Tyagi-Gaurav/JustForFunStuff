import Alert from "@mui/material/Alert";

export default function AlertMessage({ type, message }) {
  return <Alert severity={type}>{message}</Alert>;
}
