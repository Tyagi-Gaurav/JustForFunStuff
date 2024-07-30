import { Link } from "react-router-dom";
import "./GamePage.css";
import * as React from "react";
import Card from "@mui/material/Card";
import CardContent from "@mui/material/CardContent";
import CardMedia from "@mui/material/CardMedia";
import Typography from "@mui/material/Typography";
import { CardActionArea, Box } from "@mui/material";
import { useTracking } from "react-tracking";

export default function GamePage() {
  const { Track, trackEvent } = useTracking();

  return (
    <Track>
      <Box sx={{ display: "flex" }}>
        <Card
          sx={{ maxWidth: 400, maxHeight: 550, marginTop: 4, marginLeft: 4 }}
        >
          <CardActionArea>
            <Link to="/games/tictactoe"
            onClick={() => trackEvent({component:'TicTacToe', action: "Card-Clicked"})}>
              <CardMedia
                component="img"
                height="400"
                width=""
                image="/image/NoughtsCrosses.png"
                alt="noughts_crosses"
              />
            </Link>
            <CardContent>
              <Typography
                gutterBottom
                variant="h5"
                component="div"
                data-testid="tictactoe-heading"
              >
                Tic-Tac-Toe
              </Typography>
              <Typography variant="body2" color="text.secondary">
                Tic-tac-toe, noughts and crosses, or Xs and Os is a
                paper-and-pencil game for two players who take turns marking the
                spaces in a three-by-three grid with either X or O.
              </Typography>
            </CardContent>
          </CardActionArea>
        </Card>
        <Card
          sx={{ maxWidth: 400, maxHeight: 550, marginTop: 4, marginLeft: 4 }}
        >
          <CardActionArea>
            <Link to="/games/vocabtesting"
            onClick={() => trackEvent({component:'VocabTest', action: "Card-Clicked"})}>>
              <CardMedia
                component="img"
                height="400"
                width=""
                image="/image/english_vocab.png"
                alt="vocab_test"
              />
            </Link>
            <CardContent>
              <Typography gutterBottom variant="h5" component="div">
                Enrich your Vocabulary
              </Typography>
              <Typography variant="body2" color="text.secondary">
                Let's have some fun with a vocabulary game where we learn new
                words—who's ready to play and become a word wizard?
              </Typography>
            </CardContent>
          </CardActionArea>
        </Card>
      </Box>
    </Track>
  );
}
