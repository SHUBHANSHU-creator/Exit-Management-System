import { RESIGNATION_STATUS } from '../constants/resignation';
import '../styles/reportees.css';

function ReporteeActions({ reportees, currentUser, onAction, loading, isHr }) {
  const uniqueReportees = dedupeReportees(reportees);
  const relevantReportees = uniqueReportees.filter((entry) => {
    const rmNumber = entry.employeeInfo?.rmEmployeeNumber || entry.employeeInfo?.RmEmployeeNumber;
    return rmNumber === currentUser || isHr;
  });

  if (!relevantReportees.length) {
    return null;
  }

  return (
    <section className="card">
      <div className="card-header">
        <div>
          <p className="eyebrow">People manager workspace</p>
          <h2>Reportees with resignations</h2>
        </div>
        <p className="hint">Approve or schedule meetings for your team members.</p>
      </div>

      <div className="reportee-list">
        {relevantReportees.map(({ employeeInfo, employeeResignationDetailsInfo }) => {
          const empNumber = employeeInfo?.employeeNumber || employeeInfo?.EmployeeNumber;
          const status = employeeResignationDetailsInfo?.status;
          return (
            <div key={empNumber} className="reportee-card">
              <div>
                <h3>
                  {employeeInfo?.employeeName || employeeInfo?.EmployeeName || 'Reportee'}
                  <span className="muted">#{empNumber}</span>
                </h3>
                <p className="muted">{employeeInfo?.designation || employeeInfo?.Designation || '—'}</p>
                <p className="status-text">Status: {status || '—'}</p>
                <p className="muted">Reason: {employeeResignationDetailsInfo?.resignationReason || '—'}</p>
              </div>
              <div className="reportee-actions">
                <button
                  onClick={() => onAction(empNumber, RESIGNATION_STATUS.APPROVED_BY_RM)}
                  disabled={loading || status === RESIGNATION_STATUS.APPROVED_BY_RM}
                >
                  Approve
                </button>
                <button
                  className="ghost"
                  onClick={() => onAction(empNumber, RESIGNATION_STATUS.SCHEDULED_MEETING)}
                  disabled={loading}
                >
                  Schedule meeting
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

export default ReporteeActions;
