import React, {useState, useEffect} from "react";
import axios from "axios";
import {useNavigate} from "react-router-dom";
import AverageTable from "../components/tables/AverageTable";
import "../styles/dashboards/AverageDashboard.css";

export default function AverageDashboard() {
    const [average, setAverage] = useState([]);
    const [error, setError] = useState("");
    const [loading, setLoading] = useState(true);
    const navigate = useNavigate();

    useEffect(() => {
        setLoading(true);
        setError("");

        axios.get("http://localhost:8080/budgets/average")
            .then((res) => setAverage(res.data))
            .catch((error) => {
                setError(
                    error.response?.data?.message ||
                    "Failed to retrieve average record."
                );
            })
            .finally(() => setLoading(false));
    }, []);

    return(
        <div className = "averageDashboard-center">
            <h1>View Average Record</h1>
                    {loading ? (
                        <p>Loading...</p>
                    ) : error ? (
                        <div className = "error-message" role = "alert">
                            {error}
                        </div>
                    ) : (
                        <>
                            {average.length === 0 ? (
                                <p>No average records found.</p>
                            ) : (
                                <AverageTable averages = {average} />
                            )}
                            <div className = "buttons-center">
                                <button className = "custom-btn" onClick = {() => navigate("/budgets")}>
                                    Budget
                                </button>
                                <button className = "custom-btn" onClick = {() => navigate("/expenses")}>
                                    Expense
                                </button>
                                <button className = "custom-btn" onClick = {() => navigate("/budgets/summary")}>
                                    Summary
                                </button>
                                <button className = "custom-btn" onClick = {() => navigate("/home")}>
                                    Home
                                </button>
                            </div>
                        </>
                    )}
        </div>
    );
}