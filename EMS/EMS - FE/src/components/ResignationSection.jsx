import { useMemo, useState } from 'react';
import { RESIGNATION_STATUS } from '../constants/resignation';
import ResignationModal from './ResignationModal';
import '../styles/resignation.css';

function ResignationSection({ resignationDetails, onSubmit, onWithdraw, loading }) {
  const [showModal, setShowModal] = useState(false);
  const normalized = useMemo(() => normalizeResignation(resignationDetails), [resignationDetails]);

  const statusLabel = useMemo(() => {
    if (!normalized.status) return 'No resignation on file';
    return normalized.status.replaceAll('_', ' ');
  }, [normalized]);

  const hasActiveResignation =
    normalized.status &&
    normalized.status !== RESIGNATION_STATUS.VOLUNTARY_WITHDRAWAL &&
    normalized.status !== RESIGNATION_STATUS.REVERSE_TERMINATION;

  return (
    <section className="card">
      <div className="card-header">
        <div>
          <p className="eyebrow">Employee workspace</p>
          <h2>Submit or manage your resignation</h2>
        </div>
        <div className="actions">
          <button onClick={() => setShowModal(true)} disabled={loading}>
            Submit resignation
          </button>
          {hasActiveResignation && (
            <button className="ghost" onClick={onWithdraw} disabled={loading}>
              Withdraw resignation
            </button>
          )}
        </div>
      </div>

      <div className="resignation-grid">
        <div className="resignation-status">
          <h3>Status</h3>
          <p className={`status-pill ${hasActiveResignation ? 'active' : 'inactive'}`}>
            {statusLabel}
          </p>
        </div>
        <div>
          <h3>Reason</h3>
          <p>{normalized.resignationReason || 'Not provided yet'}</p>
        </div>
        <div>
          <h3>Resignation date</h3>
          <p>{formatDate(normalized.resignationDate)}</p>
        </div>
        <div>
          <h3>Last working day</h3>
          <p>{formatDate(normalized.lwd)}</p>
        </div>
      </div>

      {showModal && (
        <ResignationModal
          onClose={() => setShowModal(false)}
          onSubmit={(reason) => {
            onSubmit(reason);
            setShowModal(false);
          }}
        />
      )}
    </section>
  );
}

function normalizeResignation(details) {
  if (!details) return {};
  return {
    status: details.status || details.Status,
    resignationReason: details.resignationReason || details.ResignationReason,
    resignationDate: details.resignationDate || details.ResignationDate,
    lwd: details.lwd || details.Lwd || details.lWd
  };
}

function formatDate(date) {
  if (!date) return '—';
  const parsed = new Date(date);
  if (Number.isNaN(parsed.getTime())) return '—';
  return parsed.toLocaleDateString();
}

export default ResignationSection;
