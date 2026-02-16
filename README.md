# Autoflex Inventory System

Full Stack web application developed as a technical challenge.

The system manages products, raw materials and calculates intelligent production suggestions based on available stock.

---

## 🏗 Architecture

The application follows a separated architecture (API + Frontend):

- Backend: Spring Boot (Java)
- Frontend: React (Vite)
- Database: PostgreSQL
- API Style: RESTful

---

## 📌 Features

### ✅ Product Management
- Create product
- Update product
- Delete product
- List products

Each product contains:
- Code
- Name
- Price

---

### ✅ Raw Material Management
- Create raw material
- Update raw material
- Delete raw material
- List raw materials

Each raw material contains:
- Code
- Name
- Stock quantity

---

### ✅ Bill of Materials (BOM)
- Associate raw materials to products
- Define required quantity per raw material
- Replace BOM for a product
- Retrieve BOM by product

---

### ✅ Production Suggestion Algorithm

The system calculates:

- Which products can be produced
- The quantity that can be produced
- Total production value
- Remaining stock after simulation

### Algorithm rules:

1. Products are sorted by price (descending)
2. Higher-value products are prioritized
3. Stock is consumed virtually during simulation
4. Result includes:
   - Products to produce
   - Total value
   - Remaining raw material stock

---

## 🧠 Example Response

```json
{
  "items": [
    {
      "productId": 3,
      "productCode": "P003",
      "productName": "Sun Lounger",
      "unitPrice": 349.90,
      "producibleQuantity": 20,
      "totalValue": 6998.00
    }
  ],
  "totalValue": 6998.00,
  "remainingStock": [...]
}
```

---

## 🗄 Database Structure

Main tables:

- products
- raw_materials
- product_raw_materials

Relationships:
- One Product → Many Raw Materials
- One Raw Material → Many Products

---

## 🚀 Running the Application

### Backend

Navigate to backend folder:

```bash
cd backend
```

Run application:

```bash
mvnw.cmd spring-boot:run
```

API runs at:

```
http://localhost:8080
```

---

### Frontend

Navigate to frontend folder:

```bash
cd frontend
```

Install dependencies:

```bash
npm install
```

Run development server:

```bash
npm run dev
```

Frontend runs at:

```
http://localhost:5173
```

---

## 📡 Main Endpoints

### Products
- GET /api/products
- POST /api/products
- PUT /api/products/{id}
- DELETE /api/products/{id}

### Raw Materials
- GET /api/raw-materials
- POST /api/raw-materials
- PUT /api/raw-materials/{id}
- DELETE /api/raw-materials/{id}

### Bill of Materials
- GET /api/products/{productId}/bill-of-materials
- PUT /api/products/{productId}/bill-of-materials

### Production Suggestion
- GET /api/production/suggestion

---

## 🛠 Technologies Used

Backend:
- Java 17+
- Spring Boot
- Spring Data JPA
- Hibernate
- PostgreSQL
- Maven

Frontend:
- React
- Vite
- Axios

---

## 🔮 Possible Improvements (developing)

- Unit tests
- Integration tests
- Docker containerization
- Authentication
- Pagination
- UI enhancements

---

## 👨‍💻 Author

Developed by:

Wanderson Vitor Almeida
