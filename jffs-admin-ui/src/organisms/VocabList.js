import { useEffect, useState } from "react";
import { getWords } from "../api/backend_api";

export default function VocabList({editCallback}) {
  const [allWords, setAllWords] = useState();
  const [nextPage, setNextPage] = useState(1);
  const [previousPage, setPreviousPage] = useState(-1);
  const [totalPages, setTotalPages] = useState(0);
  const [currentPage, setCurrentPage] = useState(0);

  const handleNext = () => {
    getData(nextPage);
  };

  const handlePrevious = () => {
    getData(previousPage);
  };

  function handleRowClick(word) {
    console.log("Handle click for " + word);
    editCallback(word);
  }

  function getData(nextPage) {
    getWords(nextPage)
      .then((response) => {
        var data = response.data["words"];
        const rows = [];
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
      <nav aria-label="List Navigation">
        <ul className="pagination justify-content-center">
          <li
            id="previous"
            className={previousPage === -1 ? "page-item disabled" : "page-item"}
          >
            <a className="page-link" href="#" onClick={handlePrevious}>
              Previous
            </a>
          </li>
          <li
            id="next"
            className={nextPage === -1 ? "page-item disabled" : "page-item"}
          >
            <a className="page-link" onClick={handleNext}>
              Next
            </a>
          </li>
        </ul>
      </nav>
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
    </>
  );
}
