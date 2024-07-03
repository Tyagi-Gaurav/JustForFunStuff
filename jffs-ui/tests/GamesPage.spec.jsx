import { describe, test, expect } from "vitest";
import { render, screen } from "@testing-library/react";
import GamePage from "../src/pages/GamePage";
import { BrowserRouter } from "react-router-dom";

describe("Home Page", () => {
  describe("Layout", () => {
    test("shows tic-tac-toe game card heading", () => {
      render(
        <BrowserRouter>
          <GamePage />
        </BrowserRouter>
      );
      const tictactoeHeading = screen.getByTestId("tictactoe-heading");
      expect(tictactoeHeading).toBeVisible();
    });

    test("shows tic-tac-toe game card description", () => {
      render(
        <BrowserRouter>
          <GamePage />
        </BrowserRouter>
      );

      const description =
        "Tic-tac-toe, noughts and crosses, or Xs and Os is a paper-and-pencil " +
        "game for two players who take turns marking the spaces in a " +
        "three-by-three grid with either X or O.";
      const tictactoeDescription = screen.getByText(description);
      expect(tictactoeDescription).toBeVisible();
    });

    test("shows vocabulary test description", () => {
      render(
        <BrowserRouter>
          <GamePage />
        </BrowserRouter>
      );

      const description =
        "Let's have some fun with a vocabulary game where we learn new words—who's ready to play and become a word wizard?";
      const vocabularyDescription = screen.getByText(description);
      expect(vocabularyDescription).toBeVisible();
    });
  });
});
