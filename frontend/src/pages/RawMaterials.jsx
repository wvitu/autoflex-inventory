import { useEffect, useMemo, useState } from "react";
import {
  listRawMaterials,
  createRawMaterial,
  updateRawMaterial,
  deleteRawMaterial,
} from "../services/rawMaterials";

const initialForm = { code: "", name: "", stockQuantity: "" };

export default function RawMaterials() {
  const [items, setItems] = useState([]);
  const [loading, setLoading] = useState(true);

  const [form, setForm] = useState(initialForm);
  const [editingId, setEditingId] = useState(null);

  const [error, setError] = useState("");

  async function refresh() {
    setLoading(true);
    setError("");
    try {
      const data = await listRawMaterials();
      data.sort((a, b) => String(a.code).localeCompare(String(b.code)));
      setItems(data);
    } catch (e) {
      setError("Failed to load raw materials.");
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

  function startEdit(rm) {
    setEditingId(rm.id);
    setForm({
      code: rm.code ?? "",
      name: rm.name ?? "",
      stockQuantity: rm.stockQuantity ?? "",
    });
    setError("");
  }

  function validate() {
    if (!form.code.trim()) return "Code is required.";
    if (!form.name.trim()) return "Name is required.";
    if (form.stockQuantity === "" || Number.isNaN(Number(form.stockQuantity)))
      return "Stock quantity must be a number.";
    if (Number(form.stockQuantity) < 0) return "Stock quantity must be >= 0.";
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
      stockQuantity: Number(form.stockQuantity),
    };

    try {
      if (!isEditing) {
        await createRawMaterial(payload);
      } else {
        await updateRawMaterial(editingId, payload);
      }
      await refresh();
      startCreate();
    } catch (e) {
      setError("Failed to save raw material. Check if code is unique.");
    }
  }

  async function onDelete(id) {
    const ok = confirm("Delete this raw material?");
    if (!ok) return;

    setError("");
    try {
      await deleteRawMaterial(id);
      await refresh();
      if (editingId === id) startCreate();
    } catch (e) {
      setError("Failed to delete raw material. It may be used in a BOM.");
    }
  }

  return (
    <div style={{ marginTop: 16 }}>
      <h2>Raw Materials</h2>

      <div style={{ display: "flex", gap: 24, flexWrap: "wrap" }}>
        {/* FORM */}
        <div style={{ minWidth: 320, maxWidth: 420 }}>
          <h3 style={{ marginTop: 0 }}>
            {isEditing ? "Edit Raw Material" : "New Raw Material"}
          </h3>

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
                placeholder="e.g. RM010"
              />
            </label>

            <label style={{ display: "grid", gap: 6 }}>
              <span>Name</span>
              <input
                name="name"
                value={form.name}
                onChange={onChange}
                placeholder="e.g. Steel Tube"
              />
            </label>

            <label style={{ display: "grid", gap: 6 }}>
              <span>Stock Quantity</span>
              <input
                name="stockQuantity"
                value={form.stockQuantity}
                onChange={onChange}
                placeholder="e.g. 120"
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
                  <th style={{ border: "1px solid #555" }}>Stock</th>
                  <th style={{ border: "1px solid #555" }}>Actions</th>
                </tr>
              </thead>
              <tbody>
                {items.map((rm) => (
                  <tr key={rm.id}>
                    <td style={{ border: "1px solid #555" }}>{rm.code}</td>
                    <td style={{ border: "1px solid #555" }}>{rm.name}</td>
                    <td style={{ border: "1px solid #555" }}>
                      {Number(rm.stockQuantity).toFixed(3)}
                    </td>
                    <td style={{ border: "1px solid #555" }}>
                      <button onClick={() => startEdit(rm)}>Edit</button>{" "}
                      <button onClick={() => onDelete(rm.id)}>Delete</button>
                    </td>
                  </tr>
                ))}
                {items.length === 0 && (
                  <tr>
                    <td colSpan="4" style={{ border: "1px solid #555" }}>
                      No raw materials found.
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
