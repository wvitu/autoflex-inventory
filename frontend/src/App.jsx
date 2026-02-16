import { useState } from "react";
import Products from "./pages/Products";
import RawMaterials from "./pages/RawMaterials";
import ProductionSuggestion from "./pages/ProductionSuggestion";

function App() {
  const [page, setPage] = useState("products");

  return (
    <div style={{ padding: 20 }}>
      <h1>Autoflex Inventory</h1>

      <div style={{ marginBottom: 20 }}>
        <button onClick={() => setPage("products")}>Products</button>
        <button onClick={() => setPage("materials")}>Raw Materials</button>
        <button onClick={() => setPage("production")}>Production Suggestion</button>
      </div>

      {page === "products" && <Products />}
      {page === "materials" && <RawMaterials />}
      {page === "production" && <ProductionSuggestion />}
    </div>
  );
}

export default App;
