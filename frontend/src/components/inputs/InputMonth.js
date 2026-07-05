export default function InputMonth({name, value, onChange, disabled}) {
    const sgMonth = new Date().toLocaleDateString("en-CA", {
        timeZone: "Asia/Singapore", year: "numeric", month: "2-digit"
    });

    return (
        <div>
            <label htmlFor="input-month">Select Month: </label>

            <input
                id="input-month"
                type="month"
                name={name}
                value={value || ""}
                onChange={onChange}
                disabled={disabled}
                max = {sgMonth}
            />
        </div>
    );
}