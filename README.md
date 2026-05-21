# 金融商品喜好紀錄系統

## 前端
- Vue.js


## 後端
- Spring Boot
- RESTful API
- Maven

## 資料庫
- MySQL


## 啟動步驟

1. 啟動資料庫
```bash
docker-compose up -d
```
或手動執行 `DB/DDL.sql`、`DB/procedure.sql` 及 `DB/DML.sql`

2. 啟動後端
```bash
cd backend
./mvnw spring-boot:run
```

3. 啟動前端
```bash
cd frontend
npm install
npm run dev
```

## 預設測試帳號
- 管理員：`admin` / `admin`
- 使用者：`A1236456789` / `1234`
- 使用者：`B9876543210` / `1234`