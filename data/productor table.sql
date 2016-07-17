create table productor (
  id INT(6) UNSIGNED AUTO_INCREMENT PRIMARY KEY,
  nombre VARCHAR(50) not null,
  carnet INT not null,
  telefono INT,
  direccion VARCHAR(100),
  account VARCHAR(50),
  module INT,
  moduleName VARCHAR(50),
  asociacionName VARCHAR(50),
  asociacion INT,
  acopio INT,
  status VARCHAR(30),
  promedio INT,
  pleno INT,
  excedente INT,
  totalDebt double,
  numberPayment INT,
  position VARCHAR(30),
  createdAt TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);