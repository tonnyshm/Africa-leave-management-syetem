# Leave Management System

A microservices-based web application to streamline leave application and approval processes, built with Spring Boot, Vue.js, and Google OAuth2 for authentication.

## Project Overview

The Leave Management System consists of two Spring Boot microservices:
- `auth-service`: Handles authentication and user management.
- `leave-service`: Manages leave requests, approvals, and reporting.

A Vue.js frontend provides a user-friendly interface. Google OAuth2 is integrated for authentication, with user Google profiles used as avatars.

⚠️ **The first user to log in is automatically assigned ADMIN rights.**

## Features

- Google OAuth2 authentication for users and admins/HR.
- Automatic Admin assignment for the first user.
- Admin can promote users to Admin or Manager.
- Leave requests with file uploads (stored on Cloudinary).
- Admin can approve/reject leaves with comments.
- Email notifications for users.
- Monthly leave accrual of 1.66 days.
- Carryover logic: Max 5 days, expires by Jan 31 (Kigali Time).
- Supports Rwandan labor leave types (PTO, Sick, Maternity, etc.).
- Staff dashboard with team leave visibility.
- Public holiday view via Time and Date API.
- Admin can manage leave types and balances.
- Export leave reports to CSV.

## Technology Stack

- **Backend**: Spring Boot (Java 17)
- **Frontend**: Vue 3 with TailwindCSS
- **Authentication**: Google OAuth 2.0
- **Database**: MySQL (separate DBs for each service)
- **File Storage**: Cloudinary
- **API Integration**: timeanddate.com for holidays
- **Deployment**: Docker Compose on AWS EC2

## Folder Structure

- `auth-service/`: Spring Boot authentication microservice
- `leave-service/`: Spring Boot leave management microservice
- `leave-management-frontend/`: Vue.js frontend application
  - `.env`: Configures backend URLs
- `docker-compose.yml`: Defines all services (frontend, backend, MySQL DBs)

## Setup Instructions

1. **Configure Environment**:
   - Edit `.env` in `leave-management-frontend/` and replace `localhost` with your EC2 public IP.

2. **Run Application**:
   ```bash
   docker-compose up --build
App will now be accessible from:
http://localhost:5173

## 🔗 Live Access (Development)

Visit: [http://localhost:5173](http://localhost:5173)

> ⚠️ The **first signed-in user** via Google will be assigned **ADMIN** role automatically.

---

## ⚙️ Setup Instructions

### 1. Login

- Open [http://localhost:5173](http://localhost:5173) in your browser.
- Log in using **two separate Google accounts**:
  - First account → **ADMIN**
  - Second account → **STAFF**

### 2. Admin Actions (First User)

- Open Admin Panel.
- Assign PTO balance to users by entering their email and balance.
- Create Leave Types (e.g. PTO, Sick Leave) — required before users can apply.
- Approve/Reject leave requests with comments.
- View and download CSV reports.
- Promote users to **ADMIN** or **MANAGER**.
- View Public Holidays and Team Leave Calendar.

### 3. Staff Actions

- Apply for leave with optional file attachment (uploaded to **Cloudinary**).
- View approved balances and leave status.
- Real-time dashboard updates.

---

## 📅 Cron Job: Monthly Leave Accrual

Implemented using `@Scheduled` in Spring Boot:

- Accrues **1.66 PTO days per month**.
- Max **5-day carryover** to the next year.
- Unused carryover discarded after **Jan 31st**.

> ⚠️ Admin can override these policies via Admin Panel — fallback values apply if not set.

---

## 📄 Leave Types (Rwandan Labor Law, 2023)

- Personal Time Off (PTO)
- Sick Leave
- Compassionate Leave
- Maternity Leave
- Additional types defined by **Admin**

> ⚠️ Leave Types must be created by the Admin before they appear for users.

---

## 🐳 Docker Compose Services

- `auth-service` – Java Spring Boot
- `leave-service` – Java Spring Boot
- `frontend` – Vue.js App
- `mysql-auth` – MySQL DB for `auth-service`
- `mysql-leave` – MySQL DB for `leave-service`

Each service is containerized for security and scalability.

---

## 🧪 Testing Guidelines

## 10. 🧪 Testing Guidelines

- Ensure you have **Docker** and **Docker Compose** installed.
- Test login using **two Google accounts**:
  - The first account becomes **ADMIN**
  - The second becomes **STAFF**
- Create a **Leave Type** as Admin.
- As Admin, **assign leave balance** to the other user.
- Try submitting a **leave request** with an attached file.
- Log in as Admin and **verify the request and attachment**.
- **Test monthly accrual** manually if needed by triggering the cron job.
- **Export leave data** as a CSV report and open it locally.
- Test **public holiday rendering** on the dashboard (uses external API).

---

## 11. 🤝 Contribution Guidelines

- **Clone the repository** to your local machine.
- Use **feature branches** for all development work.
- In both `auth-service` and `leave-service`, run:
  ```bash
  mvn clean package -DskipTests

  in both microservices

- Rebuild Docker containers for any backend/frontend changes
- Use clear commit messages- Submit Pull Requests with testing proof


 ## 12. ⚠️ Notes & Limitations

- The application may **lag slightly when uploading large files**, depending on network speed and file size.
- Ensure your **EC2 instance or host machine has at least 2GB of RAM** for smooth performance.
- Use a correctly configured `.env` file with proper **service URLs and IP addresses** for cross-service communication in Docker.
- All external integrations must be functional:
  - **Cloudinary** (for file uploads)
  - **Google OAuth2** (for authentication)
  - **TimeAndDate API** (for public holiday display)

















