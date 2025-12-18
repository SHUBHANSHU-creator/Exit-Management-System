# Exit Management System

A full-stack Exit Management System for handling employee resignations with manager and HR workflows. The backend is a Spring Boot service with PostgreSQL persistence, and the frontend is a Vite + React dashboard deployed on Netlify.

## Live application

- **Production:** https://ems-application.netlify.app

## Repository layout

- `EMS/` – Spring Boot backend that exposes REST endpoints for login, resignation submission, RM/HR approvals, and IT/loan checklist closures.
- `EMS - FE/` – React frontend that connects to the backend API and surfaces the resignation workflow for employees, reporting managers, and HR teams.

## Prerequisites

- Java 21 and Maven for the backend.
- React for the frontend.
- PostgreSQL database for the backend (connection configured via environment variables).

## Backend (Spring Boot)

1. Navigate to the backend folder:
   ```bash
   cd "EMS"
   ```
2. Configure environment variables (defaults are read from `application-dev.yml`):
   ```bash
   export DB_URL="jdbc:postgresql://<host>:<port>/<database>"
   export DB_USERNAME="<username>"
   export DB_PASSWORD="<password>"
   export SERVER_PORT=8000            # optional
   ```
3. Create the schema if needed (Spring will auto-create on start):
   ```bash
   psql "$DB_URL" -c 'CREATE SCHEMA IF NOT EXISTS ems;'
   ```
4. Start the service:
   ```bash
   ./mvnw spring-boot:run
   ```

> The backend uses profile `dev` by default and serves the API under `/ems` (for example `http://localhost:8000/ems`).

## Frontend (Vite + React)

1. Navigate to the frontend folder:
   ```bash
   cd "EMS - FE"
   ```
2. Install dependencies:
   ```bash
   npm install
   ```
3. Configure the API base URL (optional). The default points to the hosted backend:
   ```bash
   echo "VITE_API_BASE_URL=http://localhost:8000/ems" > .env.local
   ```
4. Run the development server:
   ```bash
   npm run dev
   ```

The app includes login, resignation submission/withdrawal, RM approvals, HR approvals, and IT/loan checklist closure workflows. A logged-in HR user can see pending checklists and close them; RM and HR users can act on their reportees.

## Testing

- Backend: `./mvnw test`
- Frontend: `npm run build`

## Deployment notes

- The frontend is currently deployed to Netlify at the link above.
- The frontend uses `VITE_API_BASE_URL` to target the backend; set this to your API when deploying.
- Ensure your backend is reachable over HTTPS by the Netlify app for production use.
