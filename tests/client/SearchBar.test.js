import { render, screen } from "@testing-library/react";
import SearchBar from "../../client/src/components/SearchBar";

test("search input has an accessible label", () => {
  render(<SearchBar />);
  expect(screen.getByLabelText("Search jobs")).toBeInTheDocument();
});
