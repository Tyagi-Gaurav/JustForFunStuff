import { useEffect, useState } from "react";
import { getWords } from "../api/backend_api";

export default function VocabList() {
  const [allWords, setAllWords] = useState();
  const [nextPage, setNextPage] = useState(1);
  
  useEffect(() => {
    getWords(nextPage)
      .then((response) => {
        var data = response.data["words"];
        const rows = [];
        for (let i = 0; i < data.length; i++) {
          rows.push(
            <tr className="table-primary">
              <td>{data[i]["word"]}</td>
              <td>{data[i]["meanings"][0]["definition"]}</td>
            </tr>
          );
        }
        setAllWords(rows);
      })
      .catch((error) => {
        console.log(error);
      });
  }, []);

  return (
    <>
      <div>
        <table className="table table-striped">
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
