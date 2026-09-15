# Indeed Accessibility & Architecture Audit — Project Foundation

A monorepo skeleton built off a real accessibility/architecture audit of
[in.indeed.com](https://in.indeed.com/), set up to demonstrate an accessible,
maintainable full-stack foundation (React client + Spring Boot server).

## Repository structure

```
project-root/
├── client/                    React app (Vite/CRA-style)
│   └── src/
│       ├── App.jsx
│       └── components/
│           └── SearchBar.jsx  First vertical slice: accessible job search input
├── server/                    Spring Boot app
│   └── src/main/java/com/example/indeedaudit/
│       ├── IndeedAuditApplication.java
│       ├── controller/
│       │   └── JobController.java
│       └── model/
│           └── Job.java
├── docs/
│   ├── accessibility-audit.md   Full audit report (5 documented issues)
│   └── screenshots/             Lighthouse evidence (scores + insights panel)
├── tests/
│   ├── client/
│   │   └── SearchBar.test.js
│   └── server/
│       └── JobControllerTest.java
├── .gitignore
└── README.md
```

## Boundaries

- **`client/`** owns everything the user sees and interacts with directly — markup,
  styling, keyboard/focus behavior, and calling the API. It never talks to a database
  or holds business logic.
- **`server/`** owns business logic, validation, and data access. It exposes a REST
  API over HTTP and knows nothing about how the UI renders results.
- The two only communicate over HTTP (JSON), so either side can be swapped,
  scaled, or deployed independently.
- **`docs/`** holds audit evidence and architecture notes — not code.
- **`tests/`** mirrors `client/` and `server/`, one test suite per layer.

## First vertical feature slice: accessible job search

To prove the skeleton works end-to-end (not just that folders exist), the first
slice is a minimal, **accessibility-first** search feature, directly informed by the
audit findings in `docs/accessibility-audit.md`:

1. **UI** — `client/src/components/SearchBar.jsx` renders a labeled `<input>`
   (fixing the kind of missing-label issue flagged in the audit worksheet template),
   with a visible focus outline and full keyboard operability.
2. **API call** — on submit, the component calls `GET /api/jobs?q=<query>`.
3. **Controller** — `server/.../controller/JobController.java` receives the request
   and returns a JSON list of `Job` objects.
4. **Response** — the client renders results as a semantic, keyboard-navigable list.

This slice is intentionally small: it exists to prove the client-server boundary,
routing, and accessible-markup conventions all work together before more features
are layered on.

## Audit findings driving this foundation

See `docs/accessibility-audit.md` for the full report. Five architecture/performance
issues were documented from a live Lighthouse audit of in.indeed.com, each with
severity, evidence, and remediation — used here as the baseline this skeleton is
designed to avoid repeating (e.g. deferred non-critical JS in `client/`, cache
headers to configure in `server/`).
