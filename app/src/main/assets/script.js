const rows = document.getElementById("rows");
const table = document.getElementById("table");

let headVal = 64;
document.getElementById("btnFeedHead").onclick = () => {
  headVal++;
  let tHead = rows.innerHTML;
  tHead += `<th>${String.fromCharCode(headVal)}</th>`;
  rows.innerHTML = tHead;
};
document.getElementById("btnFeedData").onclick = () => {
  let tData = table.innerHTML;
  tData += "<tr>";
  for (let i = 0; i < headVal - 64; i++) tData += `<td>${i}</td>`;
  tData += "</tr>";
  table.innerHTML = tData;

  document.getElementById('display').innerText = table.innerHTML;
};