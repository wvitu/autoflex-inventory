import { useEffect, useState } from "react";
import { api } from "../services/api";

export default function Products() {
  const [products, setProducts] = useState([]);

  useEffect(() => {
    loadProducts();
  }, []);

  async function loadProducts() {
    const response = await api.get("/products");
    setProducts(response.data);
  }

  return (
    <div>
      <h2>Products</h2>
      <table border="1">
        <thead>
          <tr>
            <th>Code</th>
            <th>Name</th>
            <th>Price</th>
          </tr>
        </thead>
        <tbody>
          {products.map(product => (
            <tr key={product.id}>
              <td>{product.code}</td>
              <td>{product.name}</td>
              <td>{product.price}</td>
            </tr>
          ))}
        </tbody>
      </table>
    </div>
  );
}
