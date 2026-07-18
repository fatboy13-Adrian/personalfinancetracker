import React, {useEffect, useState} from "react";
import axios from "axios";
import {useNavigate} from "react-router-dom";
import ExpenseTable from "../components/tables/ExpenseTable";
import "../styles/dashboards/ExpenseDashboard.css";

export default function ExpenseDashboard() {
    const [expenses, setExpenses] = useState([]);
    const [selectedMonth, setSelectedMonth] = useState(() => {
        const today = new Date();
        const year = today.getFullYear();
        const month = String(today.getMonth() + 1).padStart(2, '0');
        return `${year}-${month}`;
    });
    const [error, setError] = useState("");
    const [loading, setLoading] = useState(true);

    const navigate = useNavigate();

    useEffect(() => {
        setLoading(true);
        setError("");
        axios.get("http://localhost:8080/expenses")
            .then((res) => {
                setExpenses(res.data);
            })
            .catch((error) => {
                setError(error.response?.data?.message || "Failed to retrieve expense list.");
            })
            .finally(() => {
                setLoading(false);
            });
    }, []);

    const filteredExpenses = expenses
    .filter((expense) => expense.date)
    .filter((expense) => {
        if (selectedMonth === "") return true;
        return expense.date.substring(0, 7) === selectedMonth;
    })
    .sort((a, b) => new Date(b.date) - new Date(a.date));

    const handleResetToCurrentMonth = () => {
        const today = new Date();
        const year = today.getFullYear();
        const month = String(today.getMonth() + 1).padStart(2, '0');
        setSelectedMonth(`${year}-${month}`);
    };

    return (
        <div className = "expenseDashboard-center">
            <h1>View Expense Records</h1>
            <div className = "filter-section">
                <input 
                    type = "month" 
                    value = {selectedMonth} 
                    onChange = {(e) => setSelectedMonth(e.target.value)} 
                    className = "input" 
                    max = {new Date().toISOString().substring(0, 7)}
                />
            </div>
            {loading ? (
                <p>Loading...</p>
            ) : error ? (
                <div className = "error-message" role = "alert">
                    {error}
                </div>
            ) : (
                <>
                    <div className = "expense-table-wrapper">
                        <ExpenseTable expenses = {filteredExpenses}/>
                    </div>

                    <div className = "buttons-center">
                        <button className = "custom-btn" onClick={() => navigate("/expenses/create")}>
                            Create
                        </button>
                        <button className = "custom-btn" onClick = {() => setSelectedMonth("")}>
                            Retrieve
                        </button>
                        <button className = "custom-btn" onClick={() => navigate("/expenses/update")}>
                            Update
                        </button>
                        <button className = "custom-btn" onClick = {handleResetToCurrentMonth}>
                            Current Month
                        </button>
                        <button className = "custom-btn" onClick={() => navigate("/expenses")}>
                            Expense
                        </button>
                        <button className = "custom-btn" onClick={() => navigate("/home")}>
                            Home
                        </button>
                    </div>
                </>
            )}
        </div>
    );
}