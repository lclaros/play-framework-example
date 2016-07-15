# --- !Downs
drop table IF EXISTS bancos;
drop table IF EXISTS user;
drop table IF EXISTS association;
drop table IF EXISTS module;
drop table IF EXISTS account;
drop table IF EXISTS productor;
drop table IF EXISTS transaction;
drop table IF EXISTS transactionDetail;
drop table IF EXISTS proveedor;
drop table IF EXISTS reportes;
drop table IF EXISTS product;
drop table IF EXISTS productInv;
drop table IF EXISTS discountReport;
drop table IF EXISTS discountDetail;
drop table IF EXISTS requestRow;
drop table IF EXISTS productRequest;
drop table IF EXISTS requestRowProductor;
drop table IF EXISTS logEntry;
drop table IF EXISTS measure;
drop table IF EXISTS company;

# --- !Ups
create table association (
  id INT(6) UNSIGNED AUTO_INCREMENT PRIMARY KEY,
  name VARCHAR(50) not null
);

create table module (
  id INT(6) UNSIGNED AUTO_INCREMENT PRIMARY KEY,
  name VARCHAR(50) not null,
  president INT,
  description VARCHAR(250),
  association INT(6),
  associationName VARCHAR(50)

);

create table measure (
  id INT(6) UNSIGNED AUTO_INCREMENT PRIMARY KEY,
  name VARCHAR(50),
  quantity INT(6),
  description VARCHAR(250)
);

create table account (
  id INT(6) UNSIGNED AUTO_INCREMENT PRIMARY KEY,
  code VARCHAR(30),
  name VARCHAR(50),
  type VARCHAR(30),
  parent INT(6),
  negativo VARCHAR(30),
  description VARCHAR(250),
  child boolean,
  debit double,
  credit double,
  createdAt TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

create table transaction (
  id INT(6) UNSIGNED AUTO_INCREMENT PRIMARY KEY,
  date VARCHAR(30),
  type VARCHAR(30),
  description VARCHAR(250),
  createdBy INT,
  createdByName VARCHAR(50),
  autorizedBy INT,
  autorizedByName VARCHAR(50), 
  receivedBy INT,
  receivedByName VARCHAR(50), 
  updatedBy INT(6),
  updatedByName VARCHAR(50),
  createdAt TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

create table company (
  id INT(6) UNSIGNED AUTO_INCREMENT PRIMARY KEY,
  name VARCHAR(50),
  president VARCHAR(50),
  description VARCHAR(2505)
);



create table logEntry (
  id INT(6) UNSIGNED AUTO_INCREMENT PRIMARY KEY,
  date VARCHAR(30),
  action VARCHAR(30),
  tableName_1 VARCHAR(30),
  userId INT(6)
);

create table transactionDetail (
  id INT(6) UNSIGNED AUTO_INCREMENT PRIMARY KEY,
  transaction INT,
  account INT,
  debit double,
  credit double,
  transactionDate VARCHAR(30),
  accountCode VARCHAR(30),
  accountName VARCHAR(50),
  createdAt TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  createdBy INT,
  createdByName VARCHAR(50)
);

create table bancos (
  id INT(6) UNSIGNED AUTO_INCREMENT PRIMARY KEY,
  name VARCHAR(100) not null,
  tipo VARCHAR(30) not null,
  currentMoney VARCHAR(30),
  typeMoney VARCHAR(30)
);

create table product (
  id INT(6) UNSIGNED AUTO_INCREMENT PRIMARY KEY,
  name VARCHAR(100) not null,
  cost double,
  percent double,
  price double,
  description VARCHAR(250),
  measureId INT,
  measureName VARCHAR(50),
  currentAmount int,
  createdAt TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

create table productor (
  id INT(6) UNSIGNED AUTO_INCREMENT PRIMARY KEY,
  name VARCHAR(100) not null,
  carnet int not null,
  telefono INT,
  direccion VARCHAR(100),
  account VARCHAR(30),
  module INT,
  moduleName VARCHAR(50),
  associationName VARCHAR(50),
  association INT,
  acopio INT,
  status VARCHAR(30),
  promedio INT,
  pleno INT,
  excedente INT,
  totalDebt double,
  numberPayment int,
  position VARCHAR(30),
  createdAt TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

create table proveedor (
  id INT(6) UNSIGNED AUTO_INCREMENT PRIMARY KEY,
  name VARCHAR(100) not null,
  telefono int,
  direccion VARCHAR(100),
  contacto VARCHAR(100),
  account INT,
  createdAt TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

create table reportes (
  id INT(6) UNSIGNED AUTO_INCREMENT PRIMARY KEY,
  monto int not null,
  account int not null,
  cliente int not null,
  createdAt TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

create table user (
  id INT(6) UNSIGNED AUTO_INCREMENT PRIMARY KEY,
  name VARCHAR(100) not null,
  carnet int not null,
  telefono int,
  direccion VARCHAR(30),
  sueldo int,
  type VARCHAR(30),
  login VARCHAR(30),
  password VARCHAR(30),
  createdAt TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

create table productRequest (
  id INT(6) UNSIGNED AUTO_INCREMENT PRIMARY KEY,
  date VARCHAR(30),
  veterinario INT,
  veterinarioname VARCHAR(50),
  storekeeper INT,
  storekeeperName VARCHAR(50),
  user INT,
  userName VARCHAR(50),
  module INT,
  moduleName VARCHAR(50),
  status VARCHAR(30),
  detail VARCHAR(250),
  type VARCHAR(30),
  createdAt TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  createdBy INT,
  createdByName VARCHAR(50),
  acceptedBy INT,
  acceptedByName VARCHAR(50),
  acceptedAt Date
);

create table requestRow (
  id INT(6) UNSIGNED AUTO_INCREMENT PRIMARY KEY,
  requestId INT,
  productId INT,
  productName VARCHAR(50),
  productorId INT,
  productorName VARCHAR(50),
  quantity INT,
  price double,
  measureId INT,
  measureName VARCHAR(50),
  paid INT,
  debt INT,
  cuota INT,
  status VARCHAR(30),
  createdAt TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  createdBy INT,
  createdByName VARCHAR(50)
);

create table requestRowProductor (
  id INT(6) UNSIGNED AUTO_INCREMENT PRIMARY KEY,
  requestRowId INT,
  productId INT,
  productName VARCHAR(50),
  productorId INT,
  productorName VARCHAR(50),
  measureId INT,
  measureName VARCHAR(50),
  quantity int,
  price double,
  paid double,
  debt double,
  cuota double,
  status VARCHAR(30),
  type VARCHAR(30),
  createdAt TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  createdBy INT,
  createdByName VARCHAR(50)
);

create table productInv (
  id INT(6) UNSIGNED AUTO_INCREMENT PRIMARY KEY,
  productId INT,
  proveedorId INT,
  measureId INT,
  productName VARCHAR(60),
  proveedorName VARCHAR(60),
  measureName VARCHAR(60),
  amount int,
  amountLeft int,
  cost_unit double,
  total_cost double,
  createdAt TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

create table discountReport (
  id INT(6) UNSIGNED AUTO_INCREMENT PRIMARY KEY,
  startDate VARCHAR(30),
  endDate VARCHAR(30),
  status VARCHAR(30),
  total double,
  user_id INT,
  createdAt TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

create table discountDetail (
  id INT(6) UNSIGNED AUTO_INCREMENT PRIMARY KEY,
  discountReport INT,
  requestRow INT,
  productorId INT,
  productorName VARCHAR(50),
  status VARCHAR(30),
  discount double
);
