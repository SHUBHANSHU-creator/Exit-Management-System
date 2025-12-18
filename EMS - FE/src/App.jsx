import { useCallback, useEffect, useMemo, useState } from 'react';
import Login from './components/Login';
import ResignationSection from './components/ResignationSection';
import ReporteeActions from './components/ReporteeActions';
import ChecklistTabs from './components/ChecklistTabs';
import HrApprovalSection from './components/HrApprovalSection';
import {
  closeChecklist,
  fetchPendingChecklists,
  login,
  submitHrAction,
  submitResignation,
  submitRmAction
} from './services/api';
import { RESIGNATION_STATUS } from './constants/resignation';
import './styles/app.css';

function App() {
  const [employeeNumber, setEmployeeNumber] = useState(null);
  const [resignationDetails, setResignationDetails] = useState(null);
  const [reportees, setReportees] = useState([]);
  const [alertMessage, setAlertMessage] = useState('');
  const [pendingIt, setPendingIt] = useState([]);
  const [pendingLoan, setPendingLoan] = useState([]);
  const [loading, setLoading] = useState(false);

  const isLoggedIn = useMemo(() => employeeNumber !== null, [employeeNumber]);

  const rmReportees = useMemo(
    () =>
      reportees.filter(
        (reportee) =>
          getEmployeeNumber(
            reportee.employeeInfo?.rmEmployeeNumber || reportee.employeeInfo?.RmEmployeeNumber
          ) === employeeNumber
      ),
    [reportees, employeeNumber]
  );

  const hrReportees = useMemo(
    () =>
      reportees.filter(
        (reportee) =>
          getEmployeeNumber(
            reportee.employeeInfo?.hrEmployeeNumber || reportee.employeeInfo?.HrEmployeeNumber
          ) === employeeNumber
      ),
    [reportees, employeeNumber]
  );

  const isRm = rmReportees.length > 0;
  const isHr = hrReportees.length > 0;

  const refreshSession = useCallback(
    async (empNumber = employeeNumber) => {
      if (!empNumber) return;
      try {
        const data = await login(empNumber);
        setEmployeeNumber(empNumber);
        setResignationDetails(data.employeeResignationDetails || null);
        setReportees(data.resignedEmployeesList || []);
      } catch (err) {
        setAlertMessage(err.message || 'Unable to refresh session');
      }
    },
    [employeeNumber]
  );

  const loadChecklists = useCallback(async () => {
    if (!isHr) return;
    try {
      const [itList, loanList] = await Promise.all([
        fetchPendingChecklists('IT'),
        fetchPendingChecklists('LOAN')
      ]);
      setPendingIt(itList || []);
      setPendingLoan(loanList || []);
    } catch (err) {
      setAlertMessage(err.message || 'Unable to load checklists');
    }
  }, [isHr]);

  useEffect(() => {
    if (isHr) {
      loadChecklists();
    }
  }, [isHr, loadChecklists]);

  const handleLogin = async (empNumber) => {
    setLoading(true);
    setAlertMessage('');
    try {
      await refreshSession(empNumber);
    } catch (err) {
      setAlertMessage(err.message || 'Login failed');
    } finally {
      setLoading(false);
    }
  };

  const handleLogout = () => {
    setEmployeeNumber(null);
    setResignationDetails(null);
    setReportees([]);
    setPendingIt([]);
    setPendingLoan([]);
    setAlertMessage('');
  };

  const handleSubmitResignation = async (reason) => {
    if (!employeeNumber) return;
    setLoading(true);
    setAlertMessage('');
    try {
      await submitResignation({
        employeeNumber,
        actionStatus: RESIGNATION_STATUS.SUBMITTTED,
        reason
      });
      await refreshSession();
      setAlertMessage('Resignation submitted successfully');
    } catch (err) {
      setAlertMessage(err.message || 'Unable to submit resignation');
    } finally {
      setLoading(false);
    }
  };

  const handleWithdrawResignation = async () => {
    if (!employeeNumber) return;
    setLoading(true);
    setAlertMessage('');
    try {
      await submitResignation({
        employeeNumber,
        actionStatus: RESIGNATION_STATUS.VOLUNTARY_WITHDRAWAL
      });
      await refreshSession();
      setAlertMessage('Resignation withdrawn successfully');
    } catch (err) {
      setAlertMessage(err.message || 'Unable to withdraw resignation');
    } finally {
      setLoading(false);
    }
  };

  const handleRmAction = async (targetEmployeeNumber, actionStatus) => {
    const employeeId = Number(targetEmployeeNumber);
    if (Number.isNaN(employeeId)) return;
    setLoading(true);
    setAlertMessage('');
    try {
      await submitRmAction({
        employeeNumber: employeeId,
        actionStatus
      });
      await refreshSession();
      const actorLabel = isHr ? 'HR' : 'RM';
      setAlertMessage(`${actorLabel} action submitted successfully`);
    } catch (err) {
      setAlertMessage(err.message || 'Unable to update resignation');
    } finally {
      setLoading(false);
    }
  };

  const handleHrAction = async (targetEmployeeNumber, actionStatus) => {
    const employeeId = Number(targetEmployeeNumber);
    if (Number.isNaN(employeeId)) return;
    setLoading(true);
    setAlertMessage('');
    try {
      await submitHrAction({
        employeeNumber: employeeId,
        actionStatus
      });
      await Promise.all([refreshSession(), loadChecklists()]);
      setAlertMessage('HR action submitted successfully');
    } catch (err) {
      setAlertMessage(err.message || 'Unable to update HR decision');
    } finally {
      setLoading(false);
    }
  };

  const handleCloseChecklist = async (payload) => {
    setLoading(true);
    setAlertMessage('');
    try {
      await closeChecklist(payload);
      await loadChecklists();
      setAlertMessage('Checklist closed successfully');
    } catch (err) {
      setAlertMessage(err.message || 'Unable to close checklist');
    } finally {
      setLoading(false);
    }
  };

  const pendingByEmployee = useMemo(() => {
    const pending = new Map();
    pendingIt.forEach((emp) => {
      const empNumber = getEmployeeNumber(emp.employeeNumber || emp.EmployeeNumber);
      if (!pending.has(empNumber)) pending.set(empNumber, { it: false, loan: false });
      pending.get(empNumber).it = true;
    });
    pendingLoan.forEach((emp) => {
      const empNumber = getEmployeeNumber(emp.employeeNumber || emp.EmployeeNumber);
      if (!pending.has(empNumber)) pending.set(empNumber, { it: false, loan: false });
      pending.get(empNumber).loan = true;
    });
    return pending;
  }, [pendingIt, pendingLoan]);

  return (
    <div className="app-shell">
      <header className="app-header">
        <div>
          <h1>Exit Management System</h1>
          <p>Employee self-service with RM and HR workflows</p>
        </div>
        {isLoggedIn && (
          <div className="header-actions">
            <span className="badge">Logged in as #{employeeNumber}</span>
            <button className="ghost" onClick={handleLogout}>
              Log out
            </button>
          </div>
        )}
      </header>

      {!isLoggedIn ? (
        <Login onLogin={handleLogin} loading={loading} message={alertMessage} />
      ) : (
        <main className="dashboard">
          {alertMessage && <div className="alert">{alertMessage}</div>}

          <ResignationSection
            resignationDetails={resignationDetails}
            onSubmit={handleSubmitResignation}
            onWithdraw={handleWithdrawResignation}
            loading={loading}
          />

          {isRm && (
            <ReporteeActions
              reportees={rmReportees}
              currentUser={employeeNumber}
              onAction={handleRmAction}
              loading={loading}
            />
          )}

          {isHr && (
            <>
              <ChecklistTabs
                pendingIt={pendingIt}
                pendingLoan={pendingLoan}
                onCloseChecklist={handleCloseChecklist}
                loading={loading}
              />
              <HrApprovalSection
                reportees={hrReportees}
                currentUser={employeeNumber}
                onAction={handleHrAction}
                pendingByEmployee={pendingByEmployee}
                loading={loading}
              />
            </>
          )}
        </main>
      )}
    </div>
  );
}

function getEmployeeNumber(value) {
  if (value === null || value === undefined) return null;
  const parsed = Number(value);
  return Number.isNaN(parsed) ? null : parsed;
}

export default App;
