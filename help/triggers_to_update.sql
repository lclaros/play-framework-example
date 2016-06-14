DELIMITER // 
CREATE 
TRIGGER update_transaction AFTER UPDATE|INSERT
ON transaction
FOR EACH ROW

BEGIN 
UPDATE transaction 


(account_name, product_name)
VALUES ();

END// 

DELIMITER ;


- TRIGGER PARA DELETE 
CREATE TRIGGER PEDIDO_BD_TRIGGER 
BEFORE DELETE ON PEDIDO 
FOR EACH ROW 
BEGIN 
UPDATE TOTAL_VENTAS 
SET total=total-OLD.total 
WHERE idcliente=OLD.idcliente; 
END// 




DROP TRIGGER IF EXISTS Transfer_Rights_to_Subgroup;
DELIMITER //
CREATE TRIGGER Transfer_Rights_to_Subgroup AFTER INSERT ON subgroup
FOR EACH ROW
BEGIN
    DECLARE done INT DEFAULT FALSE;
    DECLARE c1 INT;
    DECLARE c2 INT;
    DECLARE cur CURSOR FOR SELECT User_ID,Type FROM rel_group WHERE rel_group.Topic_ID =     NEW.Topic_ID;
    DECLARE CONTINUE HANDLER FOR NOT FOUND SET done = TRUE;

    OPEN cur;
        ins_loop: LOOP
            FETCH cur INTO c1,c2;
            IF done THEN
                LEAVE ins_loop;
            END IF;
            INSERT INTO rel_Subgroup VALUES (c1,NEW.Subtopic_ID,c2);
        END LOOP;
    CLOSE cur;
END; //
DELIMITER ;