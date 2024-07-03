import { BrowserRouter } from "react-router-dom";
import { describe, test, expect } from "vitest";
import TicTacToe from "../src/pages/TicTacToe";
import { render, screen, act } from "@testing-library/react";

describe("TicTacToe Page", () => {
  describe("Layout", () => {
    test("has status that shows who is the next player", () => {
      render(
        <BrowserRouter>
          <TicTacToe />
        </BrowserRouter>
      );
      const status = screen.getByText("Next Player to Play: X");
      expect(status).toBeVisible();
    });

    test("has a restart button", () => {
      render(
        <BrowserRouter>
          <TicTacToe />
        </BrowserRouter>
      );

      const restartButton = screen.getByRole("button", { name: "Restart" });
      expect(restartButton).toBeVisible();
    });

    test("has a grid of 9 squares", () => {
      render(
        <BrowserRouter>
          <TicTacToe />
        </BrowserRouter>
      );

      for (let index = 1; index <= 9; index++) {
        const square = screen.getByTestId("square-" + index);
        expect(square).toBeVisible();
      }
    });
  });

  describe("Interactions", () => {
    test("Clicking on box displays X or O", () => {
      render(
        <BrowserRouter>
          <TicTacToe />
        </BrowserRouter>
      );

      const square1 = screen.getByTestId("square-1");
      act(() => square1.click());
      expect(square1.querySelector("h3").innerHTML).toBe("X");

      const square2 = screen.getByTestId("square-2");
      act(() => square2.click());
      expect(square2.querySelector("h3").innerHTML).toBe("O");
    });

    test("Clicking on all boxes produces X and O alternately", () => {
      render(
        <BrowserRouter>
          <TicTacToe />
        </BrowserRouter>
      );
      let nextIsX = true;
      const moves = [1, 2, 3, 4, 5, 6, 8, 7, 9];

      for (let move of moves) {
        let nextString = nextIsX ? "X" : "O";
        const square = screen.getByTestId("square-" + move);
        expect(square).toBeVisible();
        act(() => square.click());
        expect(square.querySelector("h3").innerHTML).toBe(nextString);
        nextIsX = !nextIsX;
      }
    });

    test.each([
        { moves: [1, 2, 3, 4, 5, 6, 7], winner: "X"},
        { moves: [5, 1, 8, 3, 4, 2], winner: "O" }
    ])(`Produces $winner as winner based on $moves`,(game) => {
        render(
            <BrowserRouter>
              <TicTacToe />
            </BrowserRouter>
          );
       let nextIsX = true;
       for (let move of game.moves) {
         let nextString = nextIsX ? "X" : "O";
         const square = screen.getByTestId("square-" + move);
         expect(square).toBeVisible();
         act(() => square.click());
         expect(square.querySelector("h3").innerHTML).toBe(nextString);
         nextIsX = !nextIsX;
       }

       expect(screen.getByText("Winner " +game.winner)).toBeVisible();
    });
  });
});