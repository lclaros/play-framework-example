INSERT INTO user(id, name, carnet, telefono, direccion, sueldo, type, login, password) VALUES 
(1,'admin',222,333,'direccion',3000,'Admin','admin','admin'),
(2,'admin2',7878,78798,'dir',78,'Admin','admin2','admin2'),
(3,'insumo',789789,789789,'direccion',7878,'Insumo','insumo','insumo'),
(4,'Veterinario 1',789789,789789,'direccion',890890,'Veterinario','veterinario','veterinario'),
(5,'Almacen',789789,789789,'Direccion',678678,'Almacen','alamcen','almacen');

INSERT INTO association(id, name, average, pleno, excedent, total) VALUES
('10001','APL',    '203,462','166,090',	'36,399',	'202,489'),
('10002','ADEPLEC','44,251','35,995','8,215','44,210'),
('10004','ACRHOBOL','10,348','8,389','1,807','10,196'),
('10005','APLI','49,184','40,144','8,811','48,955'),
('10006','ALVA','3,899','3,225','701','3,926'),
('10007','AMLECO','21,016','17,188','3,945','21,133'),
('10008','ALVICO','3,446','2,904','601','3,505'),
('10009','ALDEPA','4,004','3,281','712','3,993'),
('10010','ADEPLECTRA','1,587','1,312','278','1,590'),
('10011','APLEVACC','15,998','12,903','2,862','15,765'),
('10013','APLIM','11,419','9,321','1,995','11,316'),
('19999','INDEPENDIENTES','18,288','14,315','3,569','17,884'),
('10014','APLE MAICA','5,062','4,102','894','4,996'),
('10012','A.L.M.','6,405','5,265','1,188','6,453'),
('10015','ASO LATTE CLAKH','2,047','1,189','589','1,778');

INSERT INTO productor
  VALUES (1, 'MATILDE CONDORI NUÑEZ', 0, 22333, 'Direccion', '2225', 1, 'Modulo 1', 'Asociacion', 10001, 133, 'acc', 81, 69, 14, 0, 0, 'position', '2016-06-17 23:53:08'),
  (2, 'PEREZ M. LEOCADIO/PEREZ FLORA ', 0, 22333, 'Direccion', '2383', 1, 'Module Name', 'Asociacion', 10001, 133, 'acc', 60, 51, 10, 0, 0, 'position', '2016-06-17 23:53:08'),
  (3, 'SOLANO C.PAULINO/JOSE H.SALVAT', 0, 22333, 'Direccion', '2472', 1, 'Module Name', 'Asociacion', 10001, 133, 'acc', 131, 109, 23, 0, 0, 'position', '2016-06-17 23:53:08'),
  (4, 'JULIA CANAVIRI DE MARISCAL', 0, 22333, 'Direccion', '2952', 1, 'Module Name', 'Asociacion', 10001, 133, 'no acc', 125, 73, 36, 43, 0, 'position', '2016-06-17 23:53:08'), (5, 'LIDIA VERDUGUEZ ROCHA', 0, 22333, 'Direccion', '10289', 1, 'Module Name', 'Asociacion', 10001, 180, 'acc', 93, 78, 16, 0, 0, 'position', '2016-06-17 23:53:08'),
  (6, 'ZERNA CLAROS CRECENCIO', 0, 22333, 'Direccion', '3302', 1, 'Module Name', 'Asociacion', 10001, 206, 'acc', 54, 46, 9, 0, 0, 'position', '2016-06-17 23:53:08');

UPDATE productor SET associationName = "APL" WHERE associationId = '10001';
UPDATE productor SET associationName = "ADEPLEC" WHERE associationId = '10002';
UPDATE productor SET associationName = "ACRHOBOL" WHERE associationId = '10004';
UPDATE productor SET associationName = "APLI" WHERE associationId = '10005';
UPDATE productor SET associationName = "ALVA" WHERE associationId = '10006';
UPDATE productor SET associationName = "AMLECO" WHERE associationId = '10007';
UPDATE productor SET associationName = "ALVICO" WHERE associationId = '10008';
UPDATE productor SET associationName = "ALDEPA" WHERE associationId = '10009';
UPDATE productor SET associationName = "ADEPLECTRA" WHERE associationId = '100010';
UPDATE productor SET associationName = "APLEVACC" WHERE associationId = '100011';
UPDATE productor SET associationName = "APLIM" WHERE associationId = '100013';
UPDATE productor SET associationName = "INDEPENDIENTES" WHERE associationId = '19999';
UPDATE productor SET associationName = "APLE MAICA" WHERE associationId = '10014';
UPDATE productor SET associationName = "A.L.M." WHERE associationId = '10012';
UPDATE productor SET associationName = "ASO LATTE CLAKH" WHERE associationId = '10015';

INSERT INTO product(proveedorName, name, type) VALUES
('AGP', 'APLICADOR DE ARETES CHINO', 'veterinaria'),
('AGP', 'ARGOLLAS NASALES BRONCE', 'veterinaria'),
('AGP', 'ARETES GRANDES C/NUMERO AMARILLO', 'veterinaria'),
('AGP', 'ARETES GRANDES S/NUMERO AMARILLO', 'veterinaria'),
('AGP', 'ARETES MEDIANOS C/NUMERO NARANJA', 'veterinaria'),
('AGP', 'BALANZA METAL 55LIBRAS /25kg', 'veterinaria'),
('AGP', 'CEFALEXINA 20% 250 ML', 'veterinaria'),
('AGP', 'CEFA - SEC X 100 ML', 'veterinaria'),
('AGP', 'CURABICHERA KERKUS X225ml', 'veterinaria'),
('AGP', 'DI ERITROMAST X10ml', 'veterinaria'),
('AGP', 'ESTRADIOL X100mL', 'veterinaria'),
('AGP', 'HEMATOFOS B12 X250ml', 'veterinaria'),
('AGP', 'IMAN ENTRARUMINAL ANILLADO', 'veterinaria'),
('AGP', 'IMAN ENTRARUMINAL C/PROTECT AMARILLO', 'veterinaria'),
('AGP', 'KERKUS X20ml', 'veterinaria'),
('AGP', 'LACTOMICIN SECADO X10gr', 'veterinaria'),
('AGP', 'LIDOCAINA X 100ML', 'veterinaria'),
('AGP', 'MOCHETA CREMADA', 'veterinaria'),
('AGP', 'MOCHETA LIVIANA', 'veterinaria'),
('AGP', 'OXISAN PLUS X250ml', 'veterinaria'),
('AGP', 'SONDA INTRAMAMARIA 80X2mm', 'veterinaria'),
('AGP', 'SYNEDEN X25ml', 'veterinaria'),
('AGP', 'TERMOMETRO CLINICO VETERINARIO MG', 'veterinaria'),
('AGP', 'TYLO COMBISONE X250cc', 'veterinaria'),
('AGP', 'VERRUSAN X 20ML', 'veterinaria'),
('AGP', 'VITAMINA K X20ml', 'veterinaria');

INSERT INTO product(proveedorName, name, currentAmount, cost, totalValue, type) VALUES
('BI', 'ACEITE ALCANFORADO X 100ML ', '1', '30.24', '30.24', 'veterinaria'),
('BI', 'AUMENTHA ATP X 100ML', '2', '50.24', '100.48', 'veterinaria'),
('BI', 'AUMENTHA ATP X 500ML', '0', '214.8', '0', 'veterinaria'),
('BI', 'AUMENTHA ATP X250ML', '12', '117.6', '1411.2', 'veterinaria'),
('BI', 'BIMECTIN PASTA X APLICADOR', '2', '39.6', '79.2', 'veterinaria'),
('BI', 'BIMECTIN POUR ON X 1LITRO', '0', '278.4', '0', 'veterinaria'),
('BI', 'BIMOXIL LA,FRASCO X 100ML', '3', '75.2', '225.6', 'veterinaria');

INSERT INTO product(proveedorName, name, currentAmount, cost, totalValue, type) VALUES
('AG',	'NUTRIKA',	'0',	'303',	'0', 'veterinaria'),
('AG',	'A-500 X5LITROS',	'3',	'119.3',	'357.9', 'veterinaria'),
('AG',	'CALFOS X 250 ML',	'2',	'48.72',	'97.44', 'veterinaria'),
('AG',	'CEFTIOMASTIN JERINGAS',	'60',	'15.59',	'935.4', 'veterinaria'),
('AG',	'CEFTIOZUR X 100 ML',	'6',	'137.74',	'826.44', 'veterinaria'),
('AG',	'DICLOFENACO X 50 ML',	'0',	'51.5',	'0', 'veterinaria'),
('AG',	'DOLFEN X 50 ML',	'4',	'45.2',	'180.8', 'veterinaria'),
('AG',	'DOXIFIN CURSO X50ML',	'1',	'61.32',	'61.32', 'veterinaria'),
('AG',	'ESTROGEST X 50 ML',	'8',	'152.95',	'1223.6', 'veterinaria');

INSERT INTO product(proveedorName, name, currentAmount, cost, totalValue, type) VALUES

('DI',	'A+D3+E X250CC',	'0',	'0',	'0', 'veterinaria'),
('DI',	'A+D3+E X500CC',	'0',	'0',	'0', 'veterinaria'),
('DI',	'AMPICILINA 20% X100ML',	'2',	'0',	'0', 'veterinaria'),
('DI',	'ANESTESICO BRAVET X50ML',	'0',	'0',	'0', 'veterinaria'),
('DI',	'ATROPINA SULFATO NEUT 1% 10ML',	'0',	'0',	'0', 'veterinaria'),
('DI',	'BENZOATO DE ESTRAD.ZOO X100M',	'0',	'0',	'0', 'veterinaria'),
('DI',	'B-PLEX X 250ML',	'0',	'0',	'0', 'veterinaria'),
('DI',	'BUSERELINA X 50 ML',	'0',	'0',	'0', 'veterinaria'),
('DI',	'CALFODEM  X 500 ML',	'0',	'0',	'0', 'veterinaria'),
('DI',	'CARAVANA LF GRANDE AMARILLO',	'0',	'0',	'0', 'veterinaria');

INSERT INTO product(proveedorName, name, currentAmount, cost, totalValue, type) VALUES
('CLA BELLA',	'TACHO DE 40 LITROS',	'1',	'1250',	'1250', 'veterinaria'),
('CLA BELLA',	'TACHO DE 30 LITROS',	'0',	'1150',	'0', 'veterinaria'),
('CLA BELLA',	'TACHO DE 20 LITROS',	'5',	'900',	'4500', 'veterinaria'),
('CLA BELLA',	'TACHO DE 40 LITROS HOVALADOS',	'0',	'0',	'0', 'veterinaria'),
('CLA BELLA',	'VALDE DE ACERO INOXIDABLE',	'4',	'300',	'1200', 'veterinaria'),
('CLA BELLA',	'VALDE DE ALUMINIO',	'3',	'450',	'1350', 'veterinaria'),
('CLA BELLA',	'COLADERAS CON FILTROS',	'4',	'190',	'760', 'veterinaria'),
('CLA BELLA',	'PILTOLA',	'2',	'510',	'1020', 'veterinaria'),
('CLA BELLA',	'AGITADOR DE LECHE',	'2',	'450',	'900', 'veterinaria');

INSERT INTO roles(roleName, roleCode) VALUES 
('Unidad de Medida', "measure"),
('Crear Unidaded de MCrear edida', "measureCreate"),
('Listar Unidades MListar edida', "measureList"),
('Mostrar Unidad de Medida', "measureShow"),
('Editar Unidad de Medida', "measureEdit"),
('Eliminar Unidad de Medida', "measureDelete"),

('Productos', "product"),
('Crear Producto', "productCreate"),
('Listar Productos', "productList"),
('Mostrar Producto', "productShow"),
('Editar Producto', "productEdit"),
('Eliminar Producto', "productDelete"),

('Proveedores', "proveedor"),
('Crear Proveedor', "proveedorCreate"),
('Listar Proveedor', "proveedorList"),
('Mostrar Proveedor', "proveedorShow"),
('Editar Proveedor', "proveedorEdit"),
('Eliminar Proveedor', "proveedorDelete"),

('Modulos', "module"),
('Crear Modulo ', "moduleCreate"),
('Listar Modulo ', "moduleList"),
('Mostrar Modulo ', "moduleShow"),
('Editar Modulo ', "moduleEdit"),
('Eliminar Modulo ', "moduleDelete"),

('Productor', "productor"),
('Crear Productor', "productorCreate"),
('Listar Productor', "productorList"),
('Mostrar Productor', "productorShow"),
('Editar Productor', "productorEdit"),
('Eliminar Productor', "productorDelete"),

('Usuarios', "user"),
('Crear Usuarios', "userCreate"),
('Listar Usuarios', "userList"),
('Mostrar Usuarios', "userShow"),
('Editar Usuarios', "userEdit"),
('Eliminar Usuarios', "userDelete"),

('Cuentas', "account"),
('Crear Cuentas', "accountCreate"),
('Listar Cuentas', "accountList"),
('Mostrar Cuentas', "accountShow"),
('Editar Cuentas', "accountEdit"),
('Eliminar Cuentas', "accountDelete"),

('Transacciones', "transaction"),
('Crear Transacciones', "transactionCreate"),
('Listar Transacciones', "transactionList"),
('Mostrar Transacciones', "transactionShow"),
('Editar Transacciones', "transactionEdit"),
('Eliminar Transacciones', "transactionDelete"),

('Detalle de Transaccion', "transactionDetail"),
('Crear Detalle de Transaccion', "transactionDetailCreate"),
('Listar Detalle de Transaccion', "transactionDetailList"),
('Mostrar Detalle de Transaccion', "transactionDetailShow"),
('Editar Detalle de Transaccion', "transactionDetailEdit"),
('Eliminar Detalle de Transaccion', "transactionDetailDelete"),

('Pedidos', "productRequest"),
('Crear Pedidos', "productRequestCreate"),
('Listar Pedidos', "productRequestList"),
('Mostrar Pedidos', "productRequestShow"),
('Editar Pedidos', "productRequestEdit"),
('Eliminar Pedidos', "productRequestDelete"),
('Enviar Pedidos', "productRequestSend"),
('Aceptar Pedidos', "productRequestAccept"),
('Finalizar Pedidos', "productRequestFinish"),

('Detalle de Pedido', "requestRow"),
('Crear Detalle de Pedido', "requestRowCreate"),
('Listar Detalle de Pedido', "requestRowList"),
('Mostrar Detalle de Pedido', "requestRowShow"),
('Editar Detalle de Pedido', "requestRowEdit"),
('Eliminar Detalle de Pedido', "requestRowDelete"),

('Asignacion de Producto a Productor', "requestRowProductor"),
('Crear Asignacion de Producto a Productor', "requestRowProductorCreate"),
('Listar Asignacion de Producto a Productor', "requestRowProductorList"),
('Mostrar Asignacion de Producto a Productor', "requestRowProductorShow"),
('Editar Asignacion de Producto a Productor', "requestRowProductorEdit"),
('Eliminar Asignacion de Producto a Productor', "requestRowProductorDelete"),

('Reporte de Descuentos', "discountReport"),
('Crear Reporte de Descuentos', "discountReportCreate"),
('Listar Reporte de Descuentos', "discountReportList"),
('Mostrar Reporte de Descuentos', "discountReportShow"),
('Editar Reporte de Descuentos', "discountReportEdit"),
('Eliminar Reporte de Descuentos', "discountReportDelete"),
('Finalizar Reporte de Descuentos', "discountReportFinalize"),

('Detalle de Descuento', "discountDetail"),
('Crear Detalle de Descuento', "discountDetailCreate"),
('Listar Detalle de Descuento', "discountDetailList"),
('Mostrar Detalle de Descuento', "discountDetailShow"),
('Editar Detalle de Descuento', "discountDetailEdit"),
('Eliminar Detalle de Descuento', "discountDetailDelete"),

('Productos al Inventario', "productInv"),
('Crear Productos al Inventario', "productInvCreate"),
('Listar Productos al Inventario', "productInvList"),
('Mostrar Productos al Inventario', "productInvShow"),
('Editar Productos al Inventario', "productInvEdit"),
('Eliminar Productos al Inventario', "productInvDelete"),

('Reporte', "report"),
('Mostrar Balance General', "balanceShow"),
('Mostrar Libro Diario', "diaryShow"),
('Mostrar Estado de Resultados', "financeShow"),
('Mostrar Libros del Mayor', "mayorShow"),
('Mostrar Sumas Y Saldos', "sumasSaldosShow"),

('Informacion de la compania', "company"),
('Mostrar Informacion de la compania', "companyShow"),
('Editar Informacion de la compania', "companyEdit");

INSERT INTO `measure`(id, name, quantity, description) VALUES 
(1,'250 ML','250','250 ML'),
(2,'100 ML','100','Descripcion'),
(3,'10gr','10','10 Gramos'),
(4,'80X2mm','2','Descripcion'),
(5,'250cc','250','250cc'),
(6,'20ML','20','Descripcion'),
(7,'500ML','500','500ML'),
(8,'1LITRO','1000','Descripcion'),
(9,'200GRS','200','200GRS'),
(10,'10GRS','10','10GRS'),
(11,'50SOB. X 10GRS.','50','50SOB. X 10GRS.'),
(12,'100 X 1KG','100','100 X 1KG'),
(13,'100 X 25KG','25','100 X 25KG'),
(14,'VALDE X 18KG','1','VALDE X 18KG'),
(15,'VALDE X 4KG','1','VALDE X 4KG'),
(16,'Unidad','1','Unidad'),
(17,'1 ML','1','1 ML'),
(18,'1 GR','1','1 GR');

UPDATE measure SET measureId = 0, measureName = 'Ninguno';

INSERT INTO `account` VALUES (1,'1.0','ACTIVO','ACTIVO',0,'NO','',0,0,0,'2016-07-18 13:19:25'),(2,'1.1','ACTIVO CORRIENTE','ACTIVO',1,'NO','',0,0,0,'2016-07-18 13:20:01'),(3,'1.2','ACTIVO NO CORRIENTE','ACTIVO',1,'NO','',0,0,0,'2016-07-18 13:22:30'),(4,'1.1.1','ACTIVO DISPONIBLE','ACTIVO',2,'NO','',0,0,0,'2016-07-18 13:23:17'),(5,'1.1.2','ACTIVO EXIGIBLE','ACTIVO',2,'NO','',0,0,0,'2016-07-18 13:24:06'),(6,'1.1.1.1','CAJA MONEDA NACIONAL','ACTIVO',4,'NO','',0,0,0,'2016-07-18 13:25:19'),(7,'1.1.1.2','BANCO MONEDA NACIONAL','ACTIVO',4,'NO','',0,0,0,'2016-07-18 13:25:45'),(8,'1.1.1.1.1','Caja Moneda Nacional','ACTIVO',6,'NO','',1,0,0,'2016-07-18 13:26:28'),(9,'1.1.1.2.1','Banco Economico M/N','ACTIVO',7,'NO','',1,0,0,'2016-07-18 13:27:09'),(10,'1.1.2.1','IMPUESTOS POR RECUPERAR','ACTIVO',5,'NO','',0,0,0,'2016-07-18 13:28:52'),(11,'1.1.2.1.1','IVA Credito Fiscal','ACTIVO',10,'NO','',1,0,0,'2016-07-18 13:29:40'),(12,'1.2.1','ACTIVO FIJO','ACTIVO',3,'NO','',0,0,0,'2016-07-18 13:31:02'),(13,'1.2.1.1','MUEBLES Y ENSERES','ACTIVO',12,'NO','',0,0,0,'2016-07-18 13:31:53'),(14,'1.2.1.1.1','Muebles y Enseres','ACTIVO',13,'NO','',1,0,0,'2016-07-18 13:32:47'),(15,'1.2.1.1.2','Depreciacion Acum. Muebles Y Enseres','ACTIVO',13,'SI','',1,0,0,'2016-07-18 13:33:32'),(16,'1.2.1.2','EQUIPO DE COMPUTACION','ACTIVO',12,'NO','',0,0,0,'2016-07-18 13:34:32'),(17,'1.2.1.2.1','Equipo de Computacion','ACTIVO',16,'NO','',1,0,0,'2016-07-18 13:35:33'),(18,'1.2.1.2.2','Depreciacion Acumulada Equipo de Computacion','ACTIVO',16,'SI','',1,0,0,'2016-07-18 13:36:27'),(19,'1.2.1.3','EQUIPO E INSTALACION','ACTIVO',12,'NO','',0,0,0,'2016-07-18 13:37:41'),(20,'1.2.1.3.1','Equipo e Instalacion','ACTIVO',19,'NO','',1,0,0,'2016-07-18 13:38:49'),(21,'1.2.1.3.2','Depreciacion Acumulada Equipo e Instalacion','ACTIVO',19,'SI','',1,0,0,'2016-07-18 13:39:28'),(22,'3.0','PATRIMONIO','PATRIMONIO',0,'NO','',0,0,0,'2016-07-18 13:41:44'),(23,'3.1','PATRIMONIO','PATRIMONIO',22,'NO','',0,0,0,'2016-07-18 13:43:35'),(24,'3.2','PERDIDA DE LA GESTION','PATRIMONIO',22,'NO','',0,0,0,'2016-07-18 13:44:35'),(25,'3.1.1','Capital','PATRIMONIO',23,'NO','',1,0,0,'2016-07-18 13:45:56'),(26,'3.1.2','Ajuste de Capital','PATRIMONIO',23,'NO','',1,0,0,'2016-07-18 13:46:46'),(27,'3.1.3','Resultados Acumulados','PATRIMONIO',23,'NO','',1,0,0,'2016-07-18 13:47:19'),(28,'3.2.1','Perdida de la Gestion','PATRIMONIO',24,'NO','',1,0,0,'2016-07-18 13:47:57'),(29,'2.0','PASIVO','PASIVO',0,'NO','',1,0,0,'2016-07-18 13:49:22'),(30,'4.0','INGRESOS','INGRESO',0,'NO','',0,0,0,'2016-07-18 13:59:18'),(31,'4.1','INGRESOS OPERATIVOS','INGRESO',30,'NO','',0,0,0,'2016-07-18 14:04:49'),(32,'4.1.1','INGRESOS POR VENTAS','INGRESO',31,'NO','',1,0,0,'2016-07-18 14:07:29'),(33,'4.1.1.1','Ingreso por Aportes de Socios','INGRESO',33,'NO','',0,0,0,'2016-07-18 14:08:18'),(34,'4.2','OTROS INGRESOS','INGRESO',30,'NO','',0,0,0,'2016-07-18 14:08:50'),(35,'4.2.1','INGRESOS NO OPERATIVOS','INGRESO',34,'NO','',0,0,0,'2016-07-18 14:10:23'),(36,'4.2.1.1','Diferencia de Redondeos','INGRESO',35,'NO','',1,0,0,'2016-07-18 14:10:52'),(37,'4.2.1.2','Ingresos Reexpresados','INGRESO',33,'NO','',1,0,0,'2016-07-18 14:11:42'),(38,'4.0','GASTOS','EGRESO',0,'NO','',0,0,0,'2016-07-18 14:15:48'),(39,'4.1','GASTOS DE OPERATION','EGRESO',38,'NO','',0,0,0,'2016-07-18 14:16:12'),(40,'4.1.1','COMBUSTIBLE Y LUBRICANTES','EGRESO',39,'NO','',0,0,0,'2016-07-18 14:16:57'),(41,'4.1.1.1','Combustibles','EGRESO',40,'NO','',1,0,0,'2016-07-18 14:17:23'),(42,'4.1.2','CORREOS Y COURIER','EGRESO',39,'NO','',0,0,0,'2016-07-18 14:17:54'),(43,'4.1.2.1','Correos y Courier','EGRESO',42,'NO','',1,0,0,'2016-07-18 14:18:16'),(44,'4.1.3','DEPRECIACIONES DEL ACTIVO FIJO','EGRESO',39,'NO','',0,0,0,'2016-07-18 14:19:09'),(45,'4.1.3.1','Depreciacion Equipo de Computacion','EGRESO',44,'NO','',1,0,0,'2016-07-18 14:20:02'),(46,'4.1.3.2','Depreciacion Equipo de Instalacion','EGRESO',44,'NO','',1,0,0,'2016-07-18 14:20:35'),(47,'4.1.3.3','Depreciacion Muebles y Enseres','EGRESO',44,'NO','',1,0,0,'2016-07-18 14:21:02'),(48,'4.1.4','GASTOS GENERALES','EGRESO',39,'NO','',0,0,0,'2016-07-18 14:23:39'),(49,'4.1.4.1','Gastos de Representacion','EGRESO',48,'NO','',1,0,0,'2016-07-18 14:24:07'),(50,'4.1.4.2','Gastos Generales','EGRESO',48,'NO','',1,0,0,'2016-07-18 14:24:32'),(51,'4.1.4.3','Refrigerios al Personal','EGRESO',48,'NO','',1,0,0,'2016-07-18 14:25:03'),(52,'4.1.5','SERVICIOS PROFECIONALES Y COMERCIALES','EGRESO',39,'NO','',0,0,0,'2016-07-18 14:26:05'),(53,'4.1.5.1','Imprenta y Papelera','EGRESO',52,'NO','',1,0,0,'2016-07-18 14:26:35'),(54,'4.1.5.2','Propaganda y Publicidad','EGRESO',52,'NO','',1,0,0,'2016-07-18 14:27:09'),(55,'4.1.6','MANTENIMIENTO Y REPARACIONES','EGRESO',39,'NO','',0,0,0,'2016-07-18 14:28:34'),(56,'4.1.6.1','Accesorios y Repuestos','EGRESO',55,'NO','',1,0,0,'2016-07-18 14:29:26'),(57,'4.1.6.2','Mantenimiento Vehiculo','EGRESO',55,'NO','',1,0,0,'2016-07-18 14:29:46'),(58,'4.1.7','MATERIALES DE ESCRITORIO Y OTROS MATERIALES','EGRESO',39,'NO','',0,0,0,'2016-07-18 14:30:30'),(59,'4.1.7.1','Materiales de Escritorio y Oficina','EGRESO',58,'NO','',1,0,0,'2016-07-18 14:31:01'),(60,'4.1.7.2','Material Electrico','EGRESO',58,'NO','',1,0,0,'2016-07-18 14:31:40'),(61,'4.1.8','PASAJES Y TRANSPORTES','EGRESO',39,'NO','',0,0,0,'2016-07-18 14:32:16'),(62,'4.1.8.1','Pasajes','EGRESO',61,'NO','',1,0,0,'2016-07-18 14:32:34'),(63,'4.1.9','SERVICIOS BASICOS','EGRESO',39,'NO','',0,0,0,'2016-07-18 14:33:26'),(64,'4.1.9.1','Servicios Telefonicos','EGRESO',63,'NO','',1,0,0,'2016-07-18 14:33:54'),(65,'4.1.10','TRAMITES LEGALES','EGRESO',39,'NO','',0,0,0,'2016-07-18 14:34:23'),(66,'4.1.10.1','Gastos legales y de tramites','EGRESO',65,'NO','',1,0,0,'2016-07-18 14:34:46'),(67,'4.2','OTROS EGRESOS','EGRESO',38,'NO','',0,0,0,'2016-07-18 14:35:28'),(68,'4.2.1','Diferencia de Redondeos','EGRESO',67,'NO','',1,0,0,'2016-07-18 14:35:57'),(69,'4.2.2','AITB','EGRESO',67,'NO','',1,0,0,'2016-07-18 14:36:14'),(70,'4.2.3','Egresos Reexpresados','EGRESO',67,'NO','',1,0,0,'2016-07-18 14:36:39');

UPDATE product SET measureId = 16, measureName="Unidad" where measureId = 0;

UPDATE product SET cost = 10, percent = "0,1", price = 11 where cost = 0;
