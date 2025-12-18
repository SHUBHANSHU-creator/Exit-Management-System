const API_BASE_URL = import.meta.env.VITE_API_BASE_URL || 'https://ems-application-1ttt.onrender.com/ems';

const defaultHeaders = {
  'Content-Type': 'application/json'
};

const buildUrl = (path) => `${API_BASE_URL}${path}`;

async function handleResponse(response) {
  const contentType = response.headers.get('content-type');
  const isJson = contentType && contentType.includes('application/json');
  const payload = isJson ? await response.json() : await response.text();

  if (!response.ok) {
    const message = typeof payload === 'string' && payload ? payload : response.statusText;
    throw new Error(message || 'Request failed');
  }
  return payload;
}

export async function login(employeeNumber) {
  const response = await fetch(buildUrl('/login'), {
    method: 'POST',
    headers: defaultHeaders,
    body: JSON.stringify({ employeeNumber: Number(employeeNumber) })
  });
  return handleResponse(response);
}

export async function submitResignation(payload) {
  const response = await fetch(buildUrl('/employeeSubmission'), {
    method: 'POST',
    headers: defaultHeaders,
    body: JSON.stringify(payload)
  });
  return handleResponse(response);
}

export async function submitRmAction(payload) {
  const response = await fetch(buildUrl('/rmApproval'), {
    method: 'POST',
    headers: defaultHeaders,
    body: JSON.stringify(payload)
  });
  return handleResponse(response);
}

export async function submitHrAction(payload) {
  const response = await fetch(buildUrl('/hrApproval'), {
    method: 'POST',
    headers: defaultHeaders,
    body: JSON.stringify(payload)
  });
  return handleResponse(response);
}

export async function fetchPendingChecklists(checklistType) {
  const response = await fetch(buildUrl('/checklists/pending'), {
    method: 'POST',
    headers: defaultHeaders,
    body: JSON.stringify({ checklistType })
  });
  return handleResponse(response);
}

export async function closeChecklist(payload) {
  const response = await fetch(buildUrl('/checklists/close'), {
    method: 'POST',
    headers: defaultHeaders,
    body: JSON.stringify(payload)
  });
  return handleResponse(response);
}
