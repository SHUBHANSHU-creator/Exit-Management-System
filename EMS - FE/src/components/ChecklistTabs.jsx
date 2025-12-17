import { useState } from 'react';
import '../styles/checklists.css';

function ChecklistTabs({ pendingIt, pendingLoan, onCloseChecklist, loading }) {
  const [activeTab, setActiveTab] = useState('IT');

  const activeList = activeTab === 'IT' ? pendingIt : pendingLoan;
  const checklistType = activeTab;

  return (
    <section className="card">
      <div className="card-header">
        <div>
          <p className="eyebrow">HR workspace</p>
          <h2>Checklists</h2>
        </div>
        <div className="tab-buttons">
          <button
            className={activeTab === 'IT' ? 'active' : ''}
            onClick={() => setActiveTab('IT')}
            disabled={loading}
          >
            IT Checklist ({pendingIt.length})
          </button>
          <button
            className={activeTab === 'LOAN' ? 'active' : ''}
            onClick={() => setActiveTab('LOAN')}
            disabled={loading}
          >
            Loan Checklist ({pendingLoan.length})
          </button>
        </div>
      </div>

      {activeList.length === 0 ? (
        <p className="muted">No pending items for this checklist.</p>
      ) : (
        <div className="checklist-grid">
          {activeList.map((employee) => (
            <ChecklistCard
              key={employee.employeeNumber || employee.EmployeeNumber}
              employee={employee}
              checklistType={checklistType}
              onCloseChecklist={onCloseChecklist}
              loading={loading}
            />
          ))}
        </div>
      )}
    </section>
  );
}

function ChecklistCard({ employee, checklistType, onCloseChecklist, loading }) {
  const [formState, setFormState] = useState(initialState(checklistType));
  const employeeNumber = employee.employeeNumber || employee.EmployeeNumber;

  const handleChange = (field) => (e) => {
    const value = e.target.type === 'checkbox' ? e.target.checked : e.target.value;
    setFormState((prev) => ({ ...prev, [field]: value }));
  };

  const handleSubmit = (e) => {
    e.preventDefault();
    onCloseChecklist({
      employeeNumber,
      checklistType,
      ...formState
    });
  };

  return (
    <form className="checklist-card" onSubmit={handleSubmit}>
      <h3>
        {employee.employeeName || employee.EmployeeName || 'Employee'}
        <span className="muted">#{employeeNumber}</span>
      </h3>

      {checklistType === 'IT' ? (
        <div className="field-group">
          <label className="inline">
            <input
              type="checkbox"
              checked={formState.submitted}
              onChange={handleChange('submitted')}
              required
            />
            Assets submitted
          </label>
          <label className="inline">
            <input type="checkbox" checked={formState.damaged} onChange={handleChange('damaged')} required />
            Assets damaged
          </label>
          <label className="inline">
            <input type="checkbox" checked={formState.paid} onChange={handleChange('paid')} required />
            Penalties paid
          </label>
        </div>
      ) : (
        <div className="field-group">
          <label className="inline">
            <input
              type="checkbox"
              checked={formState.loanTaken}
              onChange={handleChange('loanTaken')}
              required
            />
            Loan taken
          </label>
          <label className="inline">
            <input type="checkbox" checked={formState.repaid} onChange={handleChange('repaid')} required />
            Loan repaid
          </label>
        </div>
      )}

      <button type="submit" disabled={loading}>
        Close {checklistType.toLowerCase()} checklist
      </button>
    </form>
  );
}

function initialState(type) {
  if (type === 'IT') {
    return { submitted: false, damaged: false, paid: false };
  }
  return { loanTaken: false, repaid: false };
}

export default ChecklistTabs;
