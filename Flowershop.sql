-- ==========================================
-- CREATE DATABASE AND TABLES FOR FLOWER SHOP
-- ==========================================

CREATE DATABASE FlowerShopDB;
GO

USE FlowerShopDB;
GO

-- 1️⃣ Roles
CREATE TABLE Roles (
    role_id INT IDENTITY(1,1) PRIMARY KEY,
    role_name NVARCHAR(50) NOT NULL,
    description NVARCHAR(255)
);
GO

-- 2️⃣ Users
CREATE TABLE Users (
    user_id INT IDENTITY(1,1) PRIMARY KEY,
    role_id INT NOT NULL,
    full_name NVARCHAR(100) NOT NULL,
    email NVARCHAR(100) UNIQUE NOT NULL,
    password_hash NVARCHAR(255) NOT NULL,
    phone NVARCHAR(20),
    address NVARCHAR(255),
    status NVARCHAR(20) CHECK (status IN ('Active','Inactive','Locked')) DEFAULT 'Active',
    created_at DATETIME DEFAULT GETDATE(),
    updated_at DATETIME NULL,
    FOREIGN KEY (role_id) REFERENCES Roles(role_id)
);
GO

-- 3️⃣ Suppliers
CREATE TABLE Suppliers (
    supplier_id INT IDENTITY(1,1) PRIMARY KEY,
    user_id INT NOT NULL,
    company_name NVARCHAR(100) NOT NULL,
    contact_name NVARCHAR(100),
    phone NVARCHAR(20),
    address NVARCHAR(255),
    status NVARCHAR(20) CHECK (status IN ('Pending','Approved','Rejected')) DEFAULT 'Pending',
    created_at DATETIME DEFAULT GETDATE(),
    FOREIGN KEY (user_id) REFERENCES Users(user_id)
);
GO

-- 4️⃣ Categories
CREATE TABLE Categories (
    category_id INT IDENTITY(1,1) PRIMARY KEY,
    category_name NVARCHAR(100) NOT NULL,
    description NVARCHAR(255)
);
GO

-- 5️⃣ Products
CREATE TABLE Products (
    product_id INT IDENTITY(1,1) PRIMARY KEY,
    category_id INT NOT NULL,
    supplier_id INT NOT NULL,
    product_name NVARCHAR(150) NOT NULL,
    description NVARCHAR(MAX),
    price DECIMAL(10,2) NOT NULL,
    stock_quantity INT DEFAULT 0,
    image_url NVARCHAR(255),
    status NVARCHAR(20) CHECK (status IN ('Active','Inactive','Out of Stock')) DEFAULT 'Active',
    created_at DATETIME DEFAULT GETDATE(),
    FOREIGN KEY (category_id) REFERENCES Categories(category_id),
    FOREIGN KEY (supplier_id) REFERENCES Suppliers(supplier_id)
);
GO

-- 6️⃣ Shippers
CREATE TABLE Shippers (
    shipper_id INT IDENTITY(1,1) PRIMARY KEY,
    user_id INT NOT NULL,
    vehicle_number NVARCHAR(50),
    phone NVARCHAR(20),
    status NVARCHAR(20) CHECK (status IN ('Available','Delivering','Inactive')) DEFAULT 'Available',
    FOREIGN KEY (user_id) REFERENCES Users(user_id)
);
GO

-- 7️⃣ Orders Status Enum
CREATE TABLE Orders_Status_enum (
    status_id INT IDENTITY(1,1) PRIMARY KEY,
    status_name NVARCHAR(50) NOT NULL
);
GO

-- 8️⃣ Orders Payment Status Enum
CREATE TABLE Orders_PaymentStatus_enum (
    pay_status_id INT IDENTITY(1,1) PRIMARY KEY,
    pay_status_name NVARCHAR(50) NOT NULL
);
GO

-- 9️⃣ Orders
CREATE TABLE Orders (
    order_id INT IDENTITY(1,1) PRIMARY KEY,
    user_id INT NOT NULL,
    shipper_id INT NULL,
    order_status INT NOT NULL,
    payment_status INT NOT NULL,
    total_amount DECIMAL(10,2) NOT NULL,
    shipping_address NVARCHAR(255),
    order_date DATETIME DEFAULT GETDATE(),
    updated_at DATETIME NULL,
    note NVARCHAR(255),
    FOREIGN KEY (user_id) REFERENCES Users(user_id),
    FOREIGN KEY (shipper_id) REFERENCES Shippers(shipper_id),
    FOREIGN KEY (order_status) REFERENCES Orders_Status_enum(status_id),
    FOREIGN KEY (payment_status) REFERENCES Orders_PaymentStatus_enum(pay_status_id)
);
GO

-- 🔟 Payments
CREATE TABLE Payments (
    payment_id INT IDENTITY(1,1) PRIMARY KEY,
    order_id INT NOT NULL,
    payment_method NVARCHAR(20) CHECK (payment_method IN ('VNPay','Cash')) NOT NULL,
    payment_status INT NOT NULL,
    transaction_id NVARCHAR(100),
    amount DECIMAL(10,2) NOT NULL,
    payment_date DATETIME DEFAULT GETDATE(),
    FOREIGN KEY (order_id) REFERENCES Orders(order_id),
    FOREIGN KEY (payment_status) REFERENCES Orders_PaymentStatus_enum(pay_status_id)
);
GO

-- 11️⃣ Order Details
CREATE TABLE OrderDetails (
    order_detail_id INT IDENTITY(1,1) PRIMARY KEY,
    order_id INT NOT NULL,
    product_id INT NOT NULL,
    quantity INT NOT NULL,
    price DECIMAL(10,2) NOT NULL,
    FOREIGN KEY (order_id) REFERENCES Orders(order_id),
    FOREIGN KEY (product_id) REFERENCES Products(product_id)
);
GO

-- ==========================================
-- INSERT SAMPLE DATA
-- ==========================================

-- Roles
INSERT INTO Roles (role_name, description)
VALUES
('Admin', 'System Administrator'),
('Manager', 'Store Manager'),
('Staff', 'Sales Staff'),
('Shipper', 'Delivery Staff'),
('Customer', 'Flower Buyer'),
('Supplier', 'Flower Supplier'),
('Accountant', 'Payment Accountant'),
('Marketing', 'Promotion Manager'),
('Support', 'Customer Support'),
('Viewer', 'Statistics Viewer');
GO

-- Users
INSERT INTO Users (role_id, full_name, email, password_hash, phone, address, status, created_at)
VALUES
(1, 'Admin System', 'admin@flowerweb.vn', 'hash_admin', '0900000000', 'Ha Noi', 'Active', GETDATE()),
(2, 'Nguyen Quan Ly', 'manager@flowerweb.vn', 'hash_manager', '0901111111', 'Ho Chi Minh', 'Active', GETDATE()),
(3, 'Le Nhan Vien', 'staff1@flowerweb.vn', 'hash_staff1', '0902222222', 'Da Nang', 'Active', GETDATE()),
(3, 'Phan Nhan Vien', 'staff2@flowerweb.vn', 'hash_staff2', '0902222333', 'Ha Noi', 'Active', GETDATE()),
(4, 'Tran Shipper', 'shipper1@flowerweb.vn', 'hash_shipper1', '0903333333', 'Can Tho', 'Active', GETDATE()),
(4, 'Vu Giao Hang', 'shipper2@flowerweb.vn', 'hash_shipper2', '0903333444', 'Hue', 'Active', GETDATE()),
(5, 'Pham Khach A', 'customer1@flowerweb.vn', 'hash_cus1', '0904444444', 'Hue', 'Active', GETDATE()),
(5, 'Ly Khach B', 'customer2@flowerweb.vn', 'hash_cus2', '0905555555', 'Ho Chi Minh', 'Active', GETDATE()),
(6, 'Cong Ty Hoa Sen', 'supplier1@flowerweb.vn', 'hash_sup1', '0906666666', 'Da Lat', 'Active', GETDATE()),
(6, 'Cong Ty Hoa Tuoi Mai', 'supplier2@flowerweb.vn', 'hash_sup2', '0907777777', 'Bao Loc', 'Active', GETDATE());
GO

-- Suppliers
INSERT INTO Suppliers (user_id, company_name, contact_name, phone, address, status, created_at)
VALUES
(9, 'Cong Ty Hoa Sen', 'Nguyen Hoa', '0906666666', 'Da Lat', 'Approved', GETDATE()),
(10, 'Hoa Tuoi Mai', 'Tran Mai', '0907777777', 'Bao Loc', 'Approved', GETDATE()),
(9, 'Cong Ty Hoa Viet', 'Le Huong', '0901234567', 'Ha Noi', 'Pending', GETDATE()),
(10, 'FlowerFarm', 'John Pham', '0908765432', 'Da Lat', 'Approved', GETDATE()),
(9, 'Hoa Nhap Nhat', 'Takeshi Tanaka', '0909988776', 'Tokyo', 'Approved', GETDATE()),
(10, 'Hoa Cuc Sai Gon', 'Nguyen Lan', '0903344556', 'TP HCM', 'Approved', GETDATE()),
(9, 'Hoa Binh Duong', 'Phan Hoa', '0907788995', 'Binh Duong', 'Approved', GETDATE()),
(10, 'Hoa Tuoi Ha Noi', 'Le Thanh', '0908899776', 'Ha Noi', 'Approved', GETDATE()),
(9, 'Fresh Bloom Co.', 'Anna Le', '0909090909', 'Da Nang', 'Approved', GETDATE()),
(10, 'FlowerLife', 'Bao Tram', '0904545454', 'Nha Trang', 'Approved', GETDATE());
GO

-- Categories
INSERT INTO Categories (category_name, description)
VALUES
('Hoa Hong', 'Hoa hong cac loai: do, trang, vang'),
('Hoa Cuc', 'Hoa cuc tuoi dung trong dip le'),
('Hoa Lan', 'Hoa lan ho diep cao cap'),
('Hoa Tulip', 'Hoa tulip nhap khau'),
('Gio Hoa', 'Gio hoa tong hop tang sinh nhat'),
('Hoa Cuoi', 'Hoa bo va gio hoa cuoi'),
('Hoa Tang Le', 'Hoa chia buon trang trong'),
('Hoa Sinh Nhat', 'Bo hoa cho ngay sinh nhat'),
('Hoa Valentine', 'Hoa tang nguoi yeu 14/2'),
('Hoa 8/3', 'Hoa tang ngay 8 thang 3');
GO

-- Products
INSERT INTO Products (category_id, supplier_id, product_name, description, price, stock_quantity, image_url, status, created_at)
VALUES
(1, 1, 'Bo Hoa Hong Do 10 Bong', 'Tuong trung cho tinh yeu nong nan', 350000, 30, 'rose_red.jpg', 'Active', GETDATE()),
(1, 1, 'Bo Hoa Hong Trang', 'Tinh khoi va thuan khiet', 320000, 25, 'rose_white.jpg', 'Active', GETDATE()),
(2, 6, 'Gio Hoa Cuc Vang', 'Mang den su tuoi moi may man', 280000, 40, 'chrysanthemum_yellow.jpg', 'Active', GETDATE()),
(3, 4, 'Lan Ho Diep Tim', 'Bieu tuong cho su sang trong', 750000, 20, 'orchid_purple.jpg', 'Active', GETDATE()),
(4, 5, 'Bo Hoa Tulip Do', 'Hoa tulip do cho ngay le tinh nhan', 550000, 15, 'tulip_red.jpg', 'Active', GETDATE()),
(5, 8, 'Gio Hoa Tong Hop', 'Nhieu loai hoa ket hop tinh te', 420000, 35, 'flower_basket_mix.jpg', 'Active', GETDATE()),
(6, 9, 'Hoa Cuoi Trang', 'Trang nha va thanh lich', 650000, 10, 'wedding_white.jpg', 'Active', GETDATE()),
(7, 10, 'Hoa Tang Le Trang', 'Hoa chia buon gian di', 300000, 20, 'funeral_white.jpg', 'Active', GETDATE()),
(8, 7, 'Hoa Sinh Nhat Ruc Ro', 'Tang nguoi than trong ngay sinh nhat', 400000, 25, 'birthday_colorful.jpg', 'Active', GETDATE()),
(9, 2, 'Hoa Valentine Do', 'Bieu tuong tinh yeu vinh cuu', 500000, 15, 'valentine_red.jpg', 'Active', GETDATE());
GO

-- Shippers
INSERT INTO Shippers (user_id, vehicle_number, phone, status)
VALUES
(5, '51A-12345', '0903333333', 'Available'),
(6, '51B-67890', '0903333444', 'Delivering'),
(5, '51C-55555', '0903333555', 'Available'),
(6, '60A-88888', '0903333666', 'Inactive'),
(5, '51A-22222', '0903333777', 'Available'),
(6, '51D-99999', '0903333888', 'Delivering'),
(5, '51E-11111', '0903333999', 'Available'),
(6, '60B-33333', '0903334000', 'Available'),
(5, '51F-44444', '0903334111', 'Delivering'),
(6, '51G-55555', '0903334222', 'Available');
GO

-- Order Status Enum
INSERT INTO Orders_Status_enum (status_name)
VALUES
('Pending'),('Confirmed'),('Preparing'),('Delivering'),('Completed'),
('Failed'),('Cancelled'),('Returned'),('Refunded'),('Awaiting Payment');
GO

-- Payment Status Enum
INSERT INTO Orders_PaymentStatus_enum (pay_status_name)
VALUES
('Unpaid'),('Paid'),('Refunded'),('Failed'),('Partial'),
('Processing'),('Cancelled'),('Chargeback'),('Pending Confirmation'),('Success');
GO

-- Orders
INSERT INTO Orders (user_id, shipper_id, order_status, payment_status, total_amount, shipping_address, order_date, note)
VALUES
(7, 1, 1, 1, 350000, 'Hue, 123 Le Loi', GETDATE(), 'Giao buoi sang'),
(7, 2, 4, 2, 280000, 'Hue, 45 Tran Hung Dao', GETDATE(), 'Giao nhanh'),
(8, 1, 5, 2, 550000, 'TP HCM, 89 Nguyen Trai', GETDATE(), 'Tang nguoi yeu'),
(7, 3, 3, 1, 400000, 'Hue, 11 Hai Ba Trung', GETDATE(), 'Thanh toan khi nhan'),
(8, 4, 2, 2, 750000, 'Ho Chi Minh, Quan 3', GETDATE(), 'Can than khi giao'),
(7, 5, 1, 1, 300000, 'Hue, 22 Nguyen Hue', GETDATE(), 'Giao toi nay'),
(8, 6, 5, 2, 420000, 'TP HCM, Quan 5', GETDATE(), 'Tang sinh nhat ban gai'),
(7, 7, 6, 4, 280000, 'Hue, 67 Ly Thuong Kiet', GETDATE(), 'Giao ngay mai'),
(8, 8, 4, 2, 650000, 'TP HCM, Binh Thanh', GETDATE(), 'Giao tan tay'),
(7, 9, 5, 2, 500000, 'Hue, 45 Pham Van Dong', GETDATE(), 'Hoa tuoi dep');
GO

-- Payments
INSERT INTO Payments (order_id, payment_method, payment_status, transaction_id, amount, payment_date)
VALUES
(1, 'VNPay', 2, 'VNP001', 350000, GETDATE()),
(2, 'Cash', 2, NULL, 280000, GETDATE()),
(3, 'VNPay', 2, 'VNP002', 550000, GETDATE()),
(4, 'Cash', 1, NULL, 400000, GETDATE()),
(5, 'VNPay', 2, 'VNP003', 750000, GETDATE()),
(6, 'Cash', 1, NULL, 300000, GETDATE()),
(7, 'VNPay', 2, 'VNP004', 420000, GETDATE()),
(8, 'VNPay', 4, 'VNP005', 280000, GETDATE()),
(9, 'Cash', 2, NULL, 650000, GETDATE()),
(10, 'VNPay', 2, 'VNP006', 500000, GETDATE());
GO
