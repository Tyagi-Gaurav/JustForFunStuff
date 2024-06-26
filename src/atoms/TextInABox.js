import { Typography } from "@mui/material";

export default function TextInABox({ text, testId }) {
  return (
    <Typography variant="h1" gutterBottom data-testid={testId}>
      {text}
    </Typography>
  );
}
