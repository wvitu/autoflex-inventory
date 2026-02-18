import { useEffect, useMemo, useState } from "react";
import {
  listProducts,
  createProduct,
  updateProduct,
  deleteProduct,
} from "../services/products";

const initialForm = { code: "", name: "", price: "" };

export default function Products() {
  const [items, setItems] = useState([]);
  const [loading, setLoading] = useState(true);

  const [form, setForm] = useState(initialForm);
  const [editingId, setEditingId] = useState(null);

  const [error, setError] = useState("");

  async function refresh() {
    setLoading(true);
    setError("");
    try {
      const data = await listProducts();
      // opcional: ordenar por code
      data.sort((a, b) => String(a.code).localeCompare(String(b.code)));
      setItems(data);
    } catch (e) {
      setError("Failed to load products.");
    } finally {
      setLoading(false);
    }
  }

  useEffect(() => {
    refresh();
  }, []);

  const isEditing = useMemo(() => editingId !== null, [editingId]);

  function onChange(e) {
    const { name, value } = e.target;
    setForm((prev) => ({ ...prev, [name]: value }));
  }

  function startCreate() {
    setEditingId(null);
    setForm(initialForm);
    setError("");
  }

  function startEdit(p) {
    setEditingId(p.id);
    setForm({
      code: p.code ?? "",
      name: p.name ?? "",
      price: p.price ?? "",
    });
    setError("");
  }

  function validate() {
    if (!form.code.trim()) return "Code is required.";
    if (!form.name.trim()) return "Name is required.";
    if (form.price === "" || Number.isNaN(Number(form.price)))
      return "Price must be a number.";
    if (Number(form.price) < 0) return "Price must be >= 0.";
    return "";
  }

  async function onSubmit(e) {
    e.preventDefault();
    const msg = validate();
    if (msg) {
      setError(msg);
      return;
    }

    setError("");
    const payload = {
      code: form.code.trim(),
      name: form.name.trim(),
      price: Number(form.price),
    };

    try {
      if (!isEditing) {
        await createProduct(payload);
      } else {
        await updateProduct(editingId, payload);
      }
      await refresh();
      startCreate();
    } catch (e) {
      setError("Failed to save product. Check if code is unique.");
    }
  }

  async function onDelete(id) {
    const ok = confirm("Delete this product?");
    if (!ok) return;

    setError("");
    try {
      await deleteProduct(id);
      await refresh();
      if (editingId === id) startCreate();
    } catch (e) {
      setError("Failed to delete product. It may have BOM linked.");
    }
  }

  return (
    <div style={{ marginTop: 16 }}>
      <h2>Products</h2>

      <div style={{ display: "flex", gap: 24, flexWrap: "wrap" }}>
        {/* FORM */}
        <div style={{ minWidth: 320, maxWidth: 420 }}>
          <h3 style={{ marginTop: 0 }}>{isEditing ? "Edit Product" : "New Product"}</h3>

          {error && (
            <div style={{ padding: 10, border: "1px solid #a33", marginBottom: 12 }}>
              {error}
            </div>
          )}

          <form onSubmit={onSubmit} style={{ display: "grid", gap: 10 }}>
            <label style={{ display: "grid", gap: 6 }}>
              <span>Code</span>
              <input
                name="code"
                value={form.code}
                onChange={onChange}
                placeholder="e.g. P010"
              />
            </label>

            <label style={{ display: "grid", gap: 6 }}>
              <span>Name</span>
              <input
                name="name"
                value={form.name}
                onChange={onChange}
                placeholder="e.g. Folding Chair"
              />
            </label>

            <label style={{ display: "grid", gap: 6 }}>
              <span>Price</span>
              <input
                name="price"
                value={form.price}
                onChange={onChange}
                placeholder="e.g. 99.90"
                inputMode="decimal"
              />
            </label>

            <div style={{ display: "flex", gap: 10 }}>
              <button type="submit">{isEditing ? "Save" : "Create"}</button>
              <button type="button" onClick={startCreate}>
                Clear
              </button>
            </div>
          </form>
        </div>

        {/* TABLE */}
        <div style={{ flex: 1, minWidth: 320 }}>
          <h3 style={{ marginTop: 0 }}>List</h3>

          {loading ? (
            <p>Loading...</p>
          ) : (
            <table cellPadding="6" style={{ borderCollapse: "collapse" }}>
              <thead>
                <tr>
                  <th style={{ border: "1px solid #555" }}>Code</th>
                  <th style={{ border: "1px solid #555" }}>Name</th>
                  <th style={{ border: "1px solid #555" }}>Price</th>
                  <th style={{ border: "1px solid #555" }}>Actions</th>
                </tr>
              </thead>
              <tbody>
                {items.map((p) => (
                  <tr key={p.id}>
                    <td style={{ border: "1px solid #555" }}>{p.code}</td>
                    <td style={{ border: "1px solid #555" }}>{p.name}</td>
                    <td style={{ border: "1px solid #555" }}>
                      {Number(p.price).toFixed(2)}
                    </td>
                    <td style={{ border: "1px solid #555" }}>
                      <button onClick={() => startEdit(p)}>Edit</button>{" "}
                      <button onClick={() => onDelete(p.id)}>Delete</button>
                    </td>
                  </tr>
                ))}
                {items.length === 0 && (
                  <tr>
                    <td colSpan="4" style={{ border: "1px solid #555" }}>
                      No products found.
                    </td>
                  </tr>
                )}
              </tbody>
            </table>
          )}
        </div>
      </div>
    </div>
  );
}
