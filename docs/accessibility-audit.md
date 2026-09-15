# Accessibility & Architecture Audit — Indeed (in.indeed.com)

**Site audited:** https://in.indeed.com/
**Date:** September 11, 2026
**Tools used:** Chrome DevTools Lighthouse (Performance, Accessibility, Best Practices)
**Note on scope:** Lighthouse's automated Accessibility score came back at 100/100 (see
`screenshots/lighthouse-scores-overview.png`). Automated tools only catch roughly a third
of real WCAG issues — things like logical tab order, focus traps, and meaningful
heading structure require a manual pass to confirm. Since the automated scan didn't
surface accessibility failures, this audit documents five **architecture/performance**
issues instead (explicitly allowed by the assignment brief), all with direct evidence
from Lighthouse's Insights panel. A follow-up manual keyboard-only pass is recommended
before calling accessibility fully verified — see "Recommended next check" at the bottom.

---

## Summary

| # | Issue | Severity | Category | Owner |
|---|-------|----------|----------|-------|
| 1 | High Total Blocking Time (780ms) | Critical | Performance/Architecture | Frontend |
| 2 | Render-blocking requests delay first paint | Serious | Architecture | Frontend |
| 3 | Forced synchronous layout reflow | Serious | Architecture | Frontend |
| 4 | Inefficient cache lifetimes (149 KiB re-downloaded) | Moderate | Architecture | Frontend/DevOps |
| 5 | Excessive `preconnect` origins (network dependency bloat) | Moderate | Architecture | Frontend |

Overall Lighthouse scores: **Performance 58**, **Accessibility 100**, **Best Practices 73**.

---

## Issue #1: High Total Blocking Time (780ms)

- **Where found:** Homepage load, `https://in.indeed.com/`
- **Tool/method:** Lighthouse → Metrics panel
- **Evidence:** `screenshots/lighthouse-performance-insights.png` — "Total Blocking Time: 780 ms" flagged red
- **Reference:** Core Web Vitals / Lighthouse performance metric (not a WCAG rule, but directly affects users relying on assistive tech, since a blocked main thread delays keyboard/screen-reader responsiveness)
- **Severity:** Critical
- **User impact:** The main thread is busy for 780ms during load, so any input (click, tab, keypress) during that window is queued and feels unresponsive. This disproportionately affects users on slower devices, including many assistive-technology users.
- **Remediation:** Break up long JavaScript tasks, defer non-critical scripts, and code-split the initial bundle so less JS executes before the page is interactive.
- **Owner:** Frontend

---

## Issue #2: Render-blocking requests delay first paint

- **Where found:** Homepage load, `https://in.indeed.com/`
- **Tool/method:** Lighthouse → Insights → "Render-blocking requests"
- **Evidence:** `screenshots/lighthouse-performance-insights.png` — "Render-blocking requests — Est savings of 60ms"
- **Reference:** Lighthouse performance insight
- **Severity:** Serious
- **User impact:** CSS/JS loaded synchronously in `<head>` delays First Contentful Paint (currently 1.4s) and Largest Contentful Paint (1.7s), pushing back when any user — including keyboard-only users trying to start tabbing — can begin interacting with the page.
- **Remediation:** Inline critical CSS, defer or async non-critical scripts, and preload only what's needed for above-the-fold content.
- **Owner:** Frontend

---

## Issue #3: Forced synchronous layout reflow

- **Where found:** Homepage load, `https://in.indeed.com/`
- **Tool/method:** Lighthouse → Insights → "Forced reflow"
- **Evidence:** `screenshots/lighthouse-performance-insights.png` — "Forced reflow" flagged
- **Reference:** Lighthouse performance insight
- **Severity:** Serious
- **User impact:** JavaScript reading layout properties (e.g. `offsetHeight`) immediately after writing to the DOM forces the browser to recalculate layout synchronously, contributing to the high Total Blocking Time above and jank during scrolling/interaction.
- **Remediation:** Batch DOM reads and writes separately (read-then-write pattern), or use `requestAnimationFrame` to schedule layout-dependent code.
- **Owner:** Frontend

---

## Issue #4: Inefficient cache lifetimes

- **Where found:** Homepage load, `https://in.indeed.com/`
- **Tool/method:** Lighthouse → Insights → "Use efficient cache lifetimes"
- **Evidence:** `screenshots/lighthouse-performance-insights.png` — "Est savings of 149 KiB"
- **Reference:** Lighthouse performance insight
- **Severity:** Moderate
- **User impact:** Static assets are being re-fetched more often than necessary, which is especially costly for users on limited or metered mobile data plans returning to the site.
- **Remediation:** Set longer `Cache-Control max-age` / `immutable` headers on hashed static assets (JS, CSS, images, fonts).
- **Owner:** DevOps/Backend (server response headers)

---

## Issue #5: Excessive preconnect origins (network dependency bloat)

- **Where found:** Homepage load, `https://in.indeed.com/`
- **Tool/method:** Lighthouse → Insights → "Network dependency tree"
- **Evidence:** `screenshots/lighthouse-performance-insights.png` — warning: "More than 4 `preconnect` connections were found. These should be used sparingly and only to the most important origins."
- **Reference:** Lighthouse performance insight
- **Severity:** Moderate
- **User impact:** Over-using `preconnect` wastes early bandwidth and CPU opening connections that may never be used, delaying the connections that actually matter for rendering the page fast.
- **Remediation:** Audit third-party origins and keep `preconnect` hints only for the 2–4 origins critical to above-the-fold rendering; drop the rest or switch to `dns-prefetch` for lower-priority origins.
- **Owner:** Frontend

---

## Severity guide

- **Critical** — blocks a task entirely for some users
- **Serious** — major friction but a workaround exists
- **Moderate** — noticeable but not blocking
- **Minor** — cosmetic/best-practice

---

## Recommended next check (not yet performed)

Automated Accessibility score was 100, but that does **not** guarantee full WCAG
conformance. To close the gap, run a manual keyboard-only pass on `in.indeed.com`:
Tab through the search bar, job cards, and the "Apply with Indeed" button, checking for
a visible focus indicator at every stop, a logical tab order, and no keyboard traps in
any dropdown or modal. Screenshot any failures and add them as Issues #6+ if found.
