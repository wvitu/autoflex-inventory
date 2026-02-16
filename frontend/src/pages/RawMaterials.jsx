import { useEffect, useState } from "react";
import { api } from "../services/api";

export default function RawMaterials() {
  const [materials, setMaterials] = useState([]);

  useEffect(() => {
    loadMaterials();
  }, []);

  async function loadMaterials() {
    const response = await api.get("/raw-materials");
    setMaterials(response.data);
  }

  return (
    <div>
      <h2>Raw Materials</h2>
      <table border="1">
        <thead>
          <tr>
            <th>Code</th>
            <th>Name</th>
            <th>Stock</th>
          </tr>
        </thead>
        <tbody>
          {materials.map(material => (
            <tr key={material.id}>
              <td>{material.code}</td>
              <td>{material.name}</td>
              <td>{material.stockQuantity}</td>
            </tr>
          ))}
        </tbody>
      </table>
    </div>
  );
}
