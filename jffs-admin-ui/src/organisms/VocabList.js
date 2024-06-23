import { useEffect, useState } from "react";
import { getWords, search } from "../api/backend_api";

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
    if (data) {
      for (let i = 0; i < data.length; i++) {
        rows.push(
          <tr
            key={i}
            className="table-primary"
            onClick={() => handleRowClick(data[i]["word"])}
          >
            <td>{data[i]["word"]}</td>
            <td>{data[i]["meanings"][0]["definition"]}</td>
          </tr>
        );
      }
      setAllWords(rows);
    }
  }

  function getData(nextPage) {
    getWords(nextPage)
      .then((response) => {
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
    getData(nextPage);
  }, []);

  return (
    <>
      <form className="row pb-3 pt-5">
        <div className="col-2">
          <select className="form-select" onChange={handleSearchSelection}>
            <option value="">Search by</option>
            <option value="WORD">Word</option>
            <option value="SYNONYM">Synonym</option>
          </select>
        </div>
        <div className="col-3">
          <input
            type="text"
            className="form-control"
            placeholder="Search term"
            onChange={handleSearchSelectionValueChange}
          />
        </div>
        <div className="col-2">
          <button type="button" className="btn btn-primary" onClick={handleGo}>
            Go
          </button>
        </div>
      </form>
      <div className="text-success">
        <hr />
      </div>
      <div>
        <table className="table table-striped">
          <caption>
            Page {currentPage} of {totalPages}
          </caption>
          <thead>
            <tr>
              <th scope="col">Word</th>
              <th scope="col">Meaning</th>
            </tr>
          </thead>
          <tbody>{allWords}</tbody>
        </table>
      </div>
      <nav aria-label="List Navigation">
        <ul className="pagination justify-content-center">
          <li
            id="previous"
            className={previousPage === -1 ? "page-item disabled" : "page-item"}
            style={{ paddingRight: "2%" }}
          >
            <a className="page-link" href="#" onClick={handlePrevious}>
              Previous
            </a>
          </li>
          <li
            id="next"
            className={nextPage === -1 ? "page-item disabled" : "page-item"}
            style={{ paddingLeft: "2%" }}
          >
            <a className="page-link" onClick={handleNext}>
              Next
            </a>
          </li>
        </ul>
      </nav>
    </>
  );
}
