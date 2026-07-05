import "../../styles/tables/BudgetTable.css";   

export default function BudgetAction({budget, onUpdate, disabled}) {
    return (
        <div className = "action-buttons">
            <button
                onClick={() => onUpdate(budget)}
                disabled = {disabled}
            >
                Update
            </button>
        </div>
    );
}