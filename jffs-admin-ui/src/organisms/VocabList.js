import { useEffect, useState } from "react";
import { getWords, search } from "../api/backend_api";
import { getWordsGraphQL } from "../api/backend_graphql_api";
import Paper from "@mui/material/Paper";
import TableContainer from "@mui/material/TableContainer";
import Table from "@mui/material/Table";
import TableHead from "@mui/material/TableHead";
import TableRow from "@mui/material/TableRow";
import TableBody from "@mui/material/TableBody";
import TableCell from "@mui/material/TableCell";
import FormControl from "@mui/material/FormControl";
import SendIcon from '@mui/icons-material/Send';
import AlertMessage from "../atoms/AlertMessage";
import {
  Button,
  styled,
  TextField,
  Box,
  InputLabel,
  Select,
  MenuItem
} from "@mui/material";
import { TableFooter } from "@mui/material";

export default function VocabList({ editCallback }) {
  const [allWords, setAllWords] = useState();
  const [nextPage, setNextPage] = useState(1);
  const [previousPage, setPreviousPage] = useState(-1);
  const [totalPages, setTotalPages] = useState(0);
  const [currentPage, setCurrentPage] = useState(0);
  const [failedSearch, setFailedSearch] = useState(false);
  const [searchValue, setSearchValue] = useState("");
  const [searchType, setSearchType] = useState("WORD");
  const [totalWords, setTotalWords] = useState(0);

  const handleNext = () => {
    getData(nextPage);
  };

  const handleGo = () => {
//    console.log("Handle Go with " + searchValue);
//    console.log("Handle Go with " + searchType);
    if (searchValue && searchType) {
      search(searchType, searchValue)
        .then((response) => {
          setPreviousPage(-1);
          setNextPage(-1);
          createRowsFromData([response.data]);
        })
        .catch((error) => {
          console.log(error);
          setFailedSearch(true);
        });
    }
  };

  const handleSearchSelection = (event) => {
    setSearchType(event.target.value);
    setFailedSearch(false);
  };

  const handleSearchSelectionValueChange = (event) => {
    setSearchValue(event.target.value);
    setFailedSearch(false);
  };

  const handlePrevious = () => {
    getData(previousPage);
    setFailedSearch(false);
  };

  function handleRowClick(word) {
    editCallback(word);
    setFailedSearch(false);
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
    getWordsGraphQL(nextPage)
      .then((response) => {
        // console.log("Raw response: " + JSON.stringify(response));
        var data = response.data.allWords["words"];
        createRowsFromData(data);
        setTotalWords(response.data.allWords["totalWords"])
        setTotalPages(response.data.allWords["totalPages"]);
        setPreviousPage(response.data.allWords["previousPage"]);
        setNextPage(response.data.allWords["nextPage"]);
        setCurrentPage(response.data.allWords["currentPage"]);
      })
      .catch((error) => {
        console.log(error);
      });
  }

  useEffect(() => {
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
     <p/>
     {failedSearch && <AlertMessage type="error" message="Word not found" />}
      <h1>Total Words: {totalWords}</h1>
      <Box
        display="flex"
        flexDirection="row"
        component="fieldset"
        sx={{ p: 2, borderRadius: "16px", border: 1, borderColor: "grey.500"}}
      >
        <legend>Search Box</legend>
        <FormControl
          sx={{ m: 1, minWidth: 80, flexDirection: "row", justifyContent: "space-between" }}
          fullWidth
          display="flex"
        >
          <InputLabel id="search-by-label">Search By</InputLabel>
          <Select
            labelId="search-by-label"
            id="search-by-zselect"
            data-testId="search-by-select"
            label="Search By"
            sx={{ minWidth: 150 }}
            value={searchType}
            onChange={handleSearchSelection}>
            <MenuItem value={"WORD"}>Word</MenuItem>
            <MenuItem value={"SYNONYM"}>Synonym</MenuItem>
          </Select>
          <TextField
            type="text"
            variant="outlined"
            color="secondary"
            label="Word"
            required
            onChange={handleSearchSelectionValueChange}
            sx={{ minWidth: 400 }}
          />
          <Button variant="contained" endIcon={<SendIcon />} onClick={handleGo}>Go</Button>
        </FormControl>
      </Box>
      <TableContainer component={Paper} sx={{ paddingTop: "10px" }}>
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
                <Button onClick={handlePrevious} disabled={currentPage === 1}>
                  &lt; Previous
                </Button>
                <Button
                  onClick={handleNext}
                  disabled={currentPage === totalPages}
                >
                  Next &gt;
                </Button>
              </TableCell>
            </TableRow>
          </TableFooter>
        </Table>
      </TableContainer>
    </>
  );
}
