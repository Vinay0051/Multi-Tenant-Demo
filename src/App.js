import "./App.css";
import { useState } from "react";

function App() {
  const [view, setView] = useState("onboard");

  return (
    <div className="input-container">
      <div className="toggle-button">
        <button onClick={() => setView("onboard")} className="toggle-btn">Onboard</button>
        <button onClick={() => setView("insert")} className="toggle-btn">Insert</button>
      </div>
      {view === "onboard" && (
        <div className="input-box">
          <h2>Tenant Onboarding</h2>
          <table>
            <tr>
              <td><h3>Bank Name:</h3></td>
              <td><input type="text" placeholder="Enter a bank name" /></td>
            </tr>
            <tr>
              <td colSpan="2" className="button-onboard"><button>Onboard</button></td>
            </tr>
          </table>
        </div>
      )}
      {view === "insert" && (
        <div className="input-box">
          <h2>Tenant Onboarding</h2>
          <table>
            <tr>
              <td><h3>Bank Name:</h3></td>
              <td><input type="text" placeholder="Enter a bank name" /></td>
            </tr>
            <tr>
              <td><h3>Service Name:</h3></td>
              <td><input type="text" placeholder="Enter a service name" /></td>
            </tr>
            <tr>
              <td><h3>Account Holder Name:</h3></td>
              <td><input type="text" placeholder="Enter a account holder name" /></td>
            </tr>
            <tr>
              <td colSpan="2" className="button-onboard"><button>Insert Account</button></td>
            </tr>
          </table>
        </div>
      )}
    </div>
  );
}

export default App;
