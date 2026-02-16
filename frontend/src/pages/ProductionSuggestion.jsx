import { useEffect, useState } from "react";
import { api } from "../services/api";

export default function ProductionSuggestion() {
  const [data, setData] = useState(null);

  useEffect(() => {
    loadSuggestion();
  }, []);

  async function loadSuggestion() {
    const response = await api.get("/production/suggestion");
    setData(response.data);
  }

  if (!data) return <p>Loading...</p>;

  return (
    <div>
      <h2>Production Suggestion</h2>

      <h3>Products to Produce</h3>
      <table border="1">
        <thead>
          <tr>
            <th>Product</th>
            <th>Unit Price</th>
            <th>Quantity</th>
            <th>Total</th>
          </tr>
        </thead>
        <tbody>
          {data.items.map(item => (
            <tr key={item.productId}>
              <td>{item.productName}</td>
              <td>{item.unitPrice}</td>
              <td>{item.producibleQuantity}</td>
              <td>{item.totalValue}</td>
            </tr>
          ))}
        </tbody>
      </table>

      <h3>Total Value: {data.totalValue}</h3>

      <h3>Remaining Stock</h3>
      <table border="1">
        <thead>
          <tr>
            <th>Material</th>
            <th>Remaining</th>
          </tr>
        </thead>
        <tbody>
          {data.remainingStock.map(stock => (
            <tr key={stock.rawMaterialId}>
              <td>{stock.name}</td>
              <td>{stock.remainingQuantity}</td>
            </tr>
          ))}
        </tbody>
      </table>
    </div>
  );
}
