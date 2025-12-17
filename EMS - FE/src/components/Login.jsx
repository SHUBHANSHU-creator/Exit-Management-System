import { useState } from 'react';
import '../styles/login.css';

const loginImage =
  'https://images.unsplash.com/photo-1521737604893-d14cc237f11d?auto=format&fit=crop&w=900&q=80';

function Login({ onLogin, loading, message }) {
  const [employeeNumber, setEmployeeNumber] = useState('');

  const handleSubmit = (e) => {
    e.preventDefault();
    const sanitized = employeeNumber.replace(/\D/g, '');
    if (!sanitized) return;
    onLogin(Number(sanitized));
  };

  return (
    <div className="login-page">
      <div className="login-illustration" style={{ backgroundImage: `url(${loginImage})` }}>
        <div className="overlay">
          <h2>Welcome to EMS</h2>
          <p>Submit resignations, manage approvals, and close checklists effortlessly.</p>
        </div>
      </div>
      <div className="login-form-panel">
        <form className="login-form" onSubmit={handleSubmit}>
          <h1>Employee Login</h1>
          <p>Enter your employee number to continue</p>
          <label htmlFor="employeeNumber">Employee Number</label>
          <input
            id="employeeNumber"
            type="text"
            inputMode="numeric"
            pattern="[0-9]*"
            value={employeeNumber}
            onChange={(e) => setEmployeeNumber(e.target.value.replace(/[^0-9]/g, ''))}
            placeholder="e.g. 12345"
            required
          />
          <button type="submit" disabled={loading || !employeeNumber}>
            {loading ? 'Signing in...' : 'Login'}
          </button>
          {message && <div className="form-message">{message}</div>}
        </form>
      </div>
    </div>
  );
}

export default Login;
