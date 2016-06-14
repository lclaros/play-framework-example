# --- !Downs
drop table asociacion;
drop table module;
drop table account;
drop table transaction;
drop table transactionDetail;
drop table productor;
drop table proveedor;
drop table reportes;
drop table bancos;
drop table product;
drop table user;
drop table productInv;
drop table discountReport;
drop table discountDetail;
drop table requestRow;
drop table productRequest;
drop table requestRowProductor;
drop table logEntry;
drop table unitMeasure;

# --- !Ups

create table asociacion (
  id INT(6) UNSIGNED AUTO_INCREMENT PRIMARY KEY,
  name VARCHAR(30) not null
);

create table module (
  id INT(6) UNSIGNED AUTO_INCREMENT PRIMARY KEY,
  name VARCHAR(30) not null,
  president INT,
  description VARCHAR(30),
  asociacion INT(6),
  asociacionName VARCHAR(30)

);

create table unitMeasure (
  id INT(6) UNSIGNED AUTO_INCREMENT PRIMARY KEY,
  name VARCHAR(30),
  quantity INT(6),
  description VARCHAR(30)
);

create table account (
  id INT(6) UNSIGNED AUTO_INCREMENT PRIMARY KEY,
  code VARCHAR(30),
  name VARCHAR(30),
  type VARCHAR(30),
  parent INT(6),
  negativo VARCHAR(30),
  description VARCHAR(30),
  child boolean,
  debit double,
  credit double,
  createdAt TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

create table transaction (
  id INT(6) UNSIGNED AUTO_INCREMENT PRIMARY KEY,
  date VARCHAR(30),
  type VARCHAR(30),
  description VARCHAR(30),
  createdBy INT,
  createdByName VARCHAR(30),
  autorizedBy INT,
  autorizedByName VARCHAR(30), 
  receivedBy INT,
  receivedByName VARCHAR(30), 
  updatedBy INT(6),
  updatedByName VARCHAR(30),
  createdAt TIMESTAMP DEFAULT CURRENT_TIMESTAMP
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
  accountName VARCHAR(30),
  createdAt TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  createdBy INT,
  createdByName VARCHAR(30)
);

create table bancos (
  id INT(6) UNSIGNED AUTO_INCREMENT PRIMARY KEY,
  nombre VARCHAR(30) not null,
  tipo VARCHAR(30) not null,
  currentMoney VARCHAR(30),
  typeMoney VARCHAR(30)
);

create table product (
  id INT(6) UNSIGNED AUTO_INCREMENT PRIMARY KEY,
  nombre VARCHAR(30) not null,
  cost double,
  percent double,
  price double,
  descripcion VARCHAR(30),
  unitMeasure INT,
  unitMeasureName VARCHAR(30),
  currentAmount int,
  createdAt TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

create table productor (
  id INT(6) UNSIGNED AUTO_INCREMENT PRIMARY KEY,
  nombre VARCHAR(30) not null,
  carnet int not null,
  telefono int,
  direccion VARCHAR(30),
  account VARCHAR(30),
  module INT,
  moduleName VARCHAR(30),
  asociacionName VARCHAR(30),
  totalDebt double,
  numberPayment int,
  position VARCHAR(30),
  createdAt TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

create table proveedor (
  id INT(6) UNSIGNED AUTO_INCREMENT PRIMARY KEY,
  nombre VARCHAR(30) not null,
  telefono int,
  direccion VARCHAR(30),
  contacto VARCHAR(30),
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
  nombre VARCHAR(30) not null,
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
  veterinarioname VARCHAR(30),
  storekeeper INT,
  storekeeperName VARCHAR(30),
  user INT,
  userName VARCHAR(30),
  module INT,
  moduleName VARCHAR(30),
  status VARCHAR(30),
  detail VARCHAR(30),
  type VARCHAR(30),
  createdAt TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  createdBy INT,
  createdByName VARCHAR(30),
  acceptedBy INT,
  acceptedByName VARCHAR(30),
  acceptedAt Date
);

create table requestRow (
  id INT(6) UNSIGNED AUTO_INCREMENT PRIMARY KEY,
  requestId INT,
  productId INT,
  productName VARCHAR(30),
  productorId INT,
  productorName VARCHAR(30),
  quantity INT,
  precio double,
  unitMeasure INT,
  unitMeasureName VARCHAR(30),
  paid INT,
  debt INT,
  cuota INT,
  status VARCHAR(30),
  createdAt TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  createdBy INT,
  createdByName VARCHAR(30)
);

create table requestRowProductor (
  id INT(6) UNSIGNED AUTO_INCREMENT PRIMARY KEY,
  requestRowId INT,
  productId INT,
  productName VARCHAR(30),
  productorId INT,
  productorName VARCHAR(30),
  quantity int,
  precio int,
  paid int,
  debt int,
  cuota int,
  status VARCHAR(30),
  type VARCHAR(30),
  createdAt TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  createdBy INT,
  createdByName VARCHAR(30)
);

create table productInv (
  id INT(6) UNSIGNED AUTO_INCREMENT PRIMARY KEY,
  productId INT,
  proveedorId INT,
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
  productorName VARCHAR(30),
  status VARCHAR(30),
  discount double
);

INSERT INTO user(id, nombre, carnet, telefono, direccion, sueldo, type, login, password)
VALUES (1,'admin',222,333,'direccion',3000,'Admin','admin','admin'),
(2,'admin2',7878,78798,'dir',78,'Admin','admin2','admin2'),
(3,'insumo',789789,789789,'direccion',7878,'Insumo','insumo','insumo'),
(4,'Veterinario 1',789789,789789,'direccion',890890,'Veterinario','veterinario','veterinario'),
(5,'Almacen',789789,789789,'Direccion',678678,'Almacen','almacen','almacen');
INSERT INTO account(id, code, name, type, parent, negativo, description, child, debit, credit) VALUES (1,'1','Activo','ACTIVO',0,'NO','des',0,12500,1500),(2,'2','Pasivo','PASIVO',0,'NO','',0,0,0),(3,'1.1','Activo Disponible','ACTIVO',1,'NO','ss',0,12500,1500),(4,'2.1','Pasivo disponible','PASIVO',2,'NO','ss',1,0,0),(5,'3','Patrimonio','PATRIMONIO',0,'NO','ss',0,0,10000),(6,'31','Capital social','PATRIMONIO',5,'NO','22',1,0,10000),(7,'410','Egreso 1','EGRESO',0,'NO','ss',1,1500,0),(8,'510','Ingreso 1','INGRESO',0,'NO','ww',1,2500,5000),(9,'540','Ingreso 2','INGRESO',0,'NO','ss',1,0,0),(10,'440','Egreso 2','EGRESO',0,'NO','ss',1,0,0),(11,'530','Ingreso 3','INGRESO',0,'NO','sss',1,0,0),(12,'430','Egreso 3','EGRESO',0,'NO','ss',1,0,0),(13,'450','Egreso 4','EGRESO',0,'NO','sss',1,0,0),(14,'570','Ingreso 5','INGRESO',0,'NO','',1,0,0),(15,'470','Egreso 5','EGRESO',0,'NO','sss',1,0,0),(16,'580','Ingreso 6','EGRESO',0,'NO','sss',1,0,0),(17,'480','Egreso 6','EGRESO',0,'NO','sss',1,0,0),(18,'ADADC','Egreso ADADC','EGRESO',0,'NO','sss',1,0,0),(19,'ACEI','Egreso ACEI','EGRESO',0,'NO','sss',1,0,0),(20,'460','Egreso 7','EGRESO',0,'NO','ss',1,0,0),(21,'1.1.1','Caja','ACTIVO',3,'NO','Activo disponible',1,12500,1500);


