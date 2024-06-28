import { Typography } from "@mui/material";

export default function Heading({color, headingMessage}) {
  return (
    <Typography level="h1" 
      fontWeight={500} 
      fontFamily="Shantell Sans"
      fontSize="2rem"
      textAlign={"center"}
      color={color}
      paddingTop={4}
      justifyContent="center">{headingMessage}</Typography>
  );
}
