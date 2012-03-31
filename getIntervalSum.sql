DELIMITER $$

DROP FUNCTION IF EXISTS getIntervalSum;

CREATE FUNCTION getIntervalSum (pYear varchar(5), pIntervalo int) 
  RETURNS VARCHAR(100)

  BEGIN
    DECLARE done INT DEFAULT 0;
    DECLARE fechas VARCHAR(30);
    DECLARE dateTimeTemp VARCHAR(50);
    DECLARE lluviaTemp FLOAT;
    DECLARE dateTimeMax VARCHAR(50);
    DECLARE lluviaMax FLOAT;  
    DECLARE horas INT;
    DECLARE minutos INT;
    DECLARE intervalo VARCHAR(5);
    DECLARE fechasCursor CURSOR FOR SELECT CONCAT(fecha,' ',hora) FROM gf_tabla WHERE DATE_FORMAT(fecha,'%Y')=pYear and lluvia>0;
    DECLARE CONTINUE HANDLER FOR SQLSTATE '02000' SET done = 1;
    
    set lluviaMax =-1;
    set dateTimeMax='';
    set lluviaTemp=-1;
    set dateTimeTemp = '';
    
     set horas=0;
    set minutos=pIntervalo;
    if pIntervalo>=60 then
      SELECT TRUNCATE((pIntervalo/60),0) into horas;
      set minutos = pIntervalo - (horas*60);        
    end if;
    
    SELECT CONCAT(TRUNCATE(horas,0),':',minutos,':0') into intervalo;
    
    OPEN fechasCursor;
    REPEAT
      FETCH fechasCursor INTO fechas;      
      if not done then
        SELECT
          CONCAT(
            DATE_FORMAT(fecha,'%Y'),
            ',',
            DATE_FORMAT(fecha,'%M'),
            ',',
            DATE_FORMAT(fecha,'%d')
          ), 
          SUM(lluvia)
        INTO
          dateTimeTemp, 
          lluviaTemp
        FROM 
          gf_tabla 
        WHERE 
          CONCAT(fecha,' ',hora) 
          BETWEEN 
            fechas 
          AND 
            ADDTIME(fechas,intervalo);         

        if lluviaTemp>=lluviaMax then
          set lluviaMax=lluviaTemp;
          set dateTimeMax = dateTimeTemp;
        end if;   
             
      end if;
    UNTIL done END REPEAT;
    CLOSE fechasCursor;
    RETURN CONCAT(dateTimeMax,',',lluviaMax);
  END$$
  
DELIMITER ;
