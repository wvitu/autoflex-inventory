# Test Requirements (Summary)

## Entities
- Product: code, name, price
- RawMaterial: code, name, stockQuantity
- Bill of Materials: association between Product and RawMaterial with requiredQuantity

## Features
- CRUD products
- CRUD raw materials
- CRUD product composition (bill of materials)
- Production suggestions: list products and quantities that can be produced with current stock
  - Prioritize higher price products first
  - Provide total production value
