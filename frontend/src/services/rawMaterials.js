import { api } from "./api";

export async function listRawMaterials() {
  const { data } = await api.get("/raw-materials");
  return data;
}

export async function createRawMaterial(payload) {
  const { data } = await api.post("/raw-materials", payload);
  return data;
}

export async function updateRawMaterial(id, payload) {
  const { data } = await api.put(`/raw-materials/${id}`, payload);
  return data;
}

export async function deleteRawMaterial(id) {
  await api.delete(`/raw-materials/${id}`);
}
