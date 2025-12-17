import { useState } from 'react';
import '../styles/modal.css';

function ResignationModal({ onClose, onSubmit }) {
  const [reason, setReason] = useState('');

  const handleSubmit = (e) => {
    e.preventDefault();
    if (!reason.trim()) return;
    onSubmit(reason.trim());
  };

  return (
    <div className="modal-backdrop">
      <div className="modal-card">
        <header className="modal-header">
          <h3>Submit Resignation</h3>
          <button className="icon-button" type="button" onClick={onClose} aria-label="Close">
            ×
          </button>
        </header>
        <form className="modal-body" onSubmit={handleSubmit}>
          <label htmlFor="resignationReason">Reason for leaving</label>
          <textarea
            id="resignationReason"
            value={reason}
            onChange={(e) => setReason(e.target.value)}
            placeholder="Share your reason for leaving"
            rows={4}
            required
          />
          <div className="modal-actions">
            <button type="button" onClick={onClose} className="ghost">
              Cancel
            </button>
            <button type="submit">Submit</button>
          </div>
        </form>
      </div>
    </div>
  );
}

export default ResignationModal;
