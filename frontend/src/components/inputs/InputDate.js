export default function InputDate({name, label = "Date", value, onChange, disabled}) {
  const sgTime = new Date().toLocaleDateString("en-CA", {
    timeZone: "Asia/Singapore"
  });

  return (
    <div className = "mb-3">
      <label htmlFor = {name}>{label}</label>
        <input
          id = {name}
          type = "date"
          name = {name}
          value = {value}
          onChange = {onChange}
          disabled = {disabled}
          max = {sgTime}
          className = "form-control"
          required
        />
    </div>
  );
}
