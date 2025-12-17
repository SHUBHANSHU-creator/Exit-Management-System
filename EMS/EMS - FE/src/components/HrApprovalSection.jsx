import { RESIGNATION_STATUS } from '../constants/resignation';
import '../styles/hr-approvals.css';

function HrApprovalSection({ reportees, currentUser, onAction, pendingByEmployee, loading }) {
  const unique = dedupeReportees(reportees);
  const approvable = unique.filter(
    ({ employeeResignationDetailsInfo }) =>
      employeeResignationDetailsInfo?.status === RESIGNATION_STATUS.APPROVED_BY_RM
  );

  if (!approvable.length) {
    return null;
  }

  return (
    <section className="card">
      <div className="card-header">
        <div>
          <p className="eyebrow">HR approvals</p>
          <h2>Finalize resignations</h2>
        </div>
        <p className="hint">Approve or reverse terminations after checklists are closed.</p>
      </div>

      <div className="approval-grid">
        {approvable.map(({ employeeInfo, employeeResignationDetailsInfo }) => {
          const empNumber = employeeInfo?.employeeNumber || employeeInfo?.EmployeeNumber;
          const pending = pendingByEmployee.get(empNumber) || { it: false, loan: false };
          const readyForApproval = !pending.it && !pending.loan;
          return (
            <div className="approval-card" key={`${currentUser}-${empNumber}`}>
              <div>
                <h3>
                  {employeeInfo?.employeeName || employeeInfo?.EmployeeName || 'Employee'}
                  <span className="muted">#{empNumber}</span>
                </h3>
                <p className="muted">Status: {employeeResignationDetailsInfo?.status}</p>
                <p className="muted">Reason: {employeeResignationDetailsInfo?.resignationReason || '—'}</p>
                <p className="checklist-state">
                  IT checklist: {pending.it ? 'Open' : 'Closed'} | Loan checklist: {pending.loan ? 'Open' : 'Closed'}
                </p>
                {!readyForApproval && (
                  <p className="warning">Close both checklists before HR approval.</p>
                )}
              </div>

              <div className="approval-actions">
                <button
                  onClick={() => onAction(empNumber, RESIGNATION_STATUS.APPROVED_BY_HR)}
                  disabled={loading || !readyForApproval}
                >
                  Approve termination
                </button>
                <button
                  className="ghost"
                  onClick={() => onAction(empNumber, RESIGNATION_STATUS.REVERSE_TERMINATION)}
                  disabled={loading}
                >
                  Reverse termination
                </button>
              </div>
            </div>
          );
        })}
      </div>
    </section>
  );
}

function dedupeReportees(reportees) {
  const seen = new Set();
  const result = [];
  reportees.forEach((item) => {
    const empNumber = item.employeeInfo?.employeeNumber || item.employeeInfo?.EmployeeNumber;
    if (!empNumber || seen.has(empNumber)) return;
    seen.add(empNumber);
    result.push(item);
  });
  return result;
}

export default HrApprovalSection;
