import {Routes, Route, Navigate} from "react-router-dom";
import Home from "../homepages/Home";
import Expense from "../homepages/Expense";
import Budget from "../homepages/Budget";
import CreateBudget from "../pages/CreateBudget";
import UpdateBudget from "../pages/UpdateBudget";
import CreateExpense from "../pages/CreateExpense";
import UpdateExpense from "../pages/UpdateExpense";
import BudgetDashboard from "../dashboards/BudgetDashboard";
import SummaryDashboard from "../dashboards/SummaryDashboard";
import ExpenseDashboard from "../dashboards/ExpenseDashboard";
import AverageDashboard from "../dashboards/AverageDashboard";

export default function AppRoutes() {
    return (
        <Routes>
            <Route path  =  "/" element  =  {<Navigate to  =  "/home" replace/>}/>
            <Route path  =  "/home" element  =  {<Home/>}/>
            <Route path = "/expenses" element = {<Expense/>}/>
            <Route path = "/expenses/create" element = {<CreateExpense/>}/>
            <Route path = "/expenses/update" element = {<UpdateExpense/>}/>
            <Route path = "/expenses/retrieve" element = {<ExpenseDashboard/>}/>
            <Route path = "/budgets" element = {<Budget/>}/>
            <Route path = "/budgets/create" element = {<CreateBudget/>}/>
            <Route path = "/budgets/update" element = {<UpdateBudget/>}/>
            <Route path = "/budgets/retrieve" element = {<BudgetDashboard/>}/>
            <Route path = "/budgets/summary" element = {<SummaryDashboard/>}/>
            <Route path = "/budgets/average" element = {<AverageDashboard/>}/>
            <Route path = "*" element = {<Navigate to = "/home" replace/>}/>
        </Routes>
    );
}