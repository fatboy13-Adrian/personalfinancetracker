import "../../style/tables/ExpenseTable.css";   

export default function ExpenseAction({expense, onUpdate, disabled}) {
    return (
        <div className = "action-buttons">
            <button
                onClick={() => onUpdate(expense)}
                disabled = {disabled}
            >
                Update
            </button>
        </div>
    );
}