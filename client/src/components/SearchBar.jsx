import { useState } from "react";

export default function SearchBar() {
  const [query, setQuery] = useState("");
  const [results, setResults] = useState([]);

  async function handleSubmit(e) {
    e.preventDefault();
    const base = import.meta.env.VITE_API_BASE || "http://localhost:8080";
    const res = await fetch(`${base}/api/jobs?q=${encodeURIComponent(query)}`);
    const data = await res.json();
    setResults(data);
  }

  return (
    <form onSubmit={handleSubmit}>
      <label htmlFor="job-search">Search jobs</label>
      <input
        id="job-search"
        name="q"
        type="text"
        value={query}
        onChange={(e) => setQuery(e.target.value)}
      />
      <button type="submit">Search</button>

      <ul aria-label="Search results">
        {results.map((job) => (
          <li key={job.id}>
            {job.title} — {job.location}
          </li>
        ))}
      </ul>
    </form>
  );
}
