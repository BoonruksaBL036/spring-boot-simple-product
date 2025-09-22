-- ลบตาราง products ถ้ามีอยู่แล้ว เพื่อให้เริ่มต้นใหม่ได้เสมอ
DROP TABLE IF EXISTS products;

-- สร้างตาราง products
CREATE TABLE products (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    name TEXT NOT NULL,
    price REAL NOT NULL,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP
);