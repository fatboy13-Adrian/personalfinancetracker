import "../../styles/tables/AverageTable.css";

function formatMoney(value) {
  if (value === null || value === undefined || value === "") return "-";
  return `$${Number(value).toFixed(2)}`;
}

export default function AverageTable({averages = [],}) {
    return(
        <div className = "average-table-wrapper">
            <table className = "average-table">
                <thead>
                    <tr>
                        <th>Income</th>
                        <th>Retirement</th>
                        <th>Insurance</th>
                        <th>Mobile Phone</th>
                        <th>Internet</th>
                        <th>Utilities</th>
                        <th>Tax</th>
                        <th>Mortgage</th>
                        <th>Debt</th>
                        <th>Allowances For Parents</th>
                        <th>Transport</th>
                        <th>Food</th>
                        <th>Groceries</th>
                        <th>Haircut</th>
                        <th>Medical</th>
                        <th>Misc</th>
                        <th>Savings</th>
                    </tr>
                </thead>

                <tbody>
                    {(averages || []).map((average, index) => (
                        <tr key={index}>
                            <td>{formatMoney(average.income)}</td>
                            <td>{formatMoney(average.retirement)}</td>
                            <td>{formatMoney(average.insurance)}</td>
                            <td>{formatMoney(average.mobilePhone)}</td>
                            <td>{formatMoney(average.internet)}</td>
                            <td>{formatMoney(average.utilities)}</td>
                            <td>{formatMoney(average.tax)}</td>
                            <td>{formatMoney(average.mortgage)}</td>
                            <td>{formatMoney(average.debt)}</td>
                            <td>{formatMoney(average.allowancesForParents)}</td>
                            <td>{formatMoney(average.transport)}</td>
                            <td>{formatMoney(average.food)}</td>
                            <td>{formatMoney(average.groceries)}</td>
                            <td>{formatMoney(average.haircut)}</td>
                            <td>{formatMoney(average.medical)}</td>
                            <td>{formatMoney(average.misc)}</td>
                            <td>{formatMoney(average.savings)}</td>
                        </tr>
                    ))}
                </tbody>
            </table>
        </div>
    );
}