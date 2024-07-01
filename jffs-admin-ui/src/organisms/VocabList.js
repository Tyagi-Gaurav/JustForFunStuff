import { useEffect, useState } from "react";
import { getWords, search } from "../api/backend_api";
import Paper from "@mui/material/Paper";
import TableContainer from "@mui/material/TableContainer";
import Table from "@mui/material/Table";
import TableHead from "@mui/material/TableHead";
import TableRow from "@mui/material/TableRow";
import TableBody from "@mui/material/TableBody";
import TableCell from "@mui/material/TableCell";
import { Button, styled } from "@mui/material";
import { TableFooter } from "@mui/material";

export default function VocabList({ editCallback }) {
  const [allWords, setAllWords] = useState();
  const [nextPage, setNextPage] = useState(1);
  const [previousPage, setPreviousPage] = useState(-1);
  const [totalPages, setTotalPages] = useState(0);
  const [currentPage, setCurrentPage] = useState(0);
  const [searchValue, setSearchValue] = useState("");
  const [searchType, setSearchType] = useState(0);

  const handleNext = () => {
    getData(nextPage);
  };

  const handleGo = () => {
    if (searchValue && searchType) {
      search(searchType, searchValue)
        .then((response) => {
          setPreviousPage(-1);
          setNextPage(-1);
          createRowsFromData([response.data]);
        })
        .catch((error) => {
          console.log(error);
        });
    }
  };

  const handleSearchSelection = (event) => {
    setSearchType(event.target.value);
  };

  const handleSearchSelectionValueChange = (event) => {
    setSearchValue(event.target.value);
  };

  const handlePrevious = () => {
    getData(previousPage);
  };

  function handleRowClick(word) {
    editCallback(word);
  }

  function createRowsFromData(data) {
    const rows = [];
    // console.log(data);
    if (data) {
      for (let i = 0; i < data.length; i++) {
        rows.push(
          <StyledTableRow
            key={i}
            className="table-primary"
            onClick={() => handleRowClick(data[i]["word"])}
          >
            <TableCell>{data[i]["word"]}</TableCell>
            <TableCell>{data[i]["meanings"][0]["definition"]}</TableCell>
          </StyledTableRow>
        );
      }
      setAllWords(rows);
    }
  }

  function getData(nextPage) {
    getWords(nextPage)
      .then((response) => {
        // console.log("Raw response: " + JSON.stringify(response));
        var data = response.data["words"];
        createRowsFromData(data);
        setTotalPages(response.data["totalPages"]);
        setPreviousPage(response.data["previousPage"]);
        setNextPage(response.data["nextPage"]);
        setCurrentPage(response.data["currentPage"]);
      })
      .catch((error) => {
        console.log(error);
      });
  }

  useEffect(() => {
    // preventDefault();
    getData(nextPage);
  }, []);

  const StyledTableRow = styled(TableRow)(({ theme }) => ({
    "&:nth-of-type(odd)": {
      backgroundColor: "#A0DEFF",
    },
    "&:nth-of-type(even)": {
      backgroundColor: "#B3C8CF",
    },
  }));

  return (
    <>
      <TableContainer component={Paper}>
        <Table sx={{ minWidth: 650 }} aria-label="simple table">
          <TableHead>
            <TableRow
              sx={{
                backgroundColor: "#FFF9D0",
              }}
            >
              <TableCell>
                <strong>Word</strong>
              </TableCell>
              <TableCell align="left">
                <strong>Meaning</strong>
              </TableCell>
            </TableRow>
          </TableHead>
          <TableBody>{allWords}</TableBody>
          <TableFooter>
            <TableRow>
              <TableCell>
                Page {currentPage} of {totalPages}
              </TableCell>
              <TableCell>
                <Button onClick={handlePrevious} disabled={currentPage === 1}>&lt; Previous</Button>
                <Button onClick={handleNext} disabled={currentPage === totalPages}>Next &gt;</Button>
              </TableCell>
            </TableRow>
          </TableFooter>
        </Table>
      </TableContainer>
    </>
  );
}
