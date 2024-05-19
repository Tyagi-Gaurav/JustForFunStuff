export default function Square({ id, value, onSquareClick }) {
    function getClassNames() {
      return "square " + value.color;
    }
  
    return (
      <button id={id} className={getClassNames()} onClick={onSquareClick}>
        {value.text}
      </button>
    );
  }