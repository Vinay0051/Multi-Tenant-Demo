import React, { useState } from "react";
import "./tenantOnboarding.css"; 

const TenantOnboarding = () => {
  const [view, setView] = useState("onboard");

  return (
    <div className="input-container">
      <div className="toggle-button">
        <button onClick={() => setView("onboard")} className="toggle-btn">Onboard</button>
        <button onClick={() => setView("insert")} className="toggle-btn">Insert</button>
      </div>
      {view === "onboard" && (
        <div className="tenant-onboarding">
          <h2>Tenant Onboarding</h2>
          <div className="input-group">
            <label htmlFor="bank-name">Bank Name:</label>
            <input type="text" id="bank-name" placeholder="Enter a bank name" />
          </div>
          <div className="button-onboard">
            <button>Onboard</button>
          </div>
        </div>
      )}
      {view === "insert" && (
        <div className="tenant-onboarding">
          <h2>Tenant Onboarding</h2>
          <div className="input-group">
            <label htmlFor="bank-name">Bank Name:</label>
            <input type="text" id="bank-name" placeholder="Enter a bank name" />
          </div>
          <div className="input-group">
            <label htmlFor="service-name">Service Name:</label>
            <input type="text" id="service-name" placeholder="Enter a service name" />
          </div>
          <div className="input-group">
            <label htmlFor="account-holder">Account Holder Name:</label>
            <input type="text" id="account-holder" placeholder="Enter an account holder name" />
          </div>
          <div className="button-onboard">
            <button>Insert Account</button>
          </div>
        </div>
      )}
    </div>
  );
};

export default TenantOnboarding;
