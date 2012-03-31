-- Obtenemos los anios
select distinct date_format(fecha,'%Y') from gf_tabla;

-- Obtenemos el horas segun el anio
select distinct hora from gf_tabla where date_format(fecha,'%Y')=2002 order by hora asc limit 2000;

-- Obtenemos la precipitacion maxima segun el anio y el lapso especificado
SELECT fecha, hora, MAX(lluvia) FROM gf_tabla WHERE DATE_FORMAT(fecha,'%Y')=2009 AND hora>='01:30:00' AND hora<='02:05:00';


describe gf_tabla;

DROP TABLE tablaPrecipitacion;

CREATE TABLE IF NOT EXISTS  tablaPrecipitacion (  
  anio           INT NOT NULL,
  mes            VARCHAR(20) NOT NULL,
  dia            INT NOT NULL,
  min5           FLOAT DEFAULT -1,
  min10          FLOAT DEFAULT -1,
  min20          FLOAT DEFAULT -1,
  min60          FLOAT DEFAULT -1,
  min90          FLOAT DEFAULT -1,
  min120         FLOAT DEFAULT -1,
  UNIQUE (anio, mes, dia)
);

TRUNCATE tablaPrecipitacion;

SELECT 
  DATE_FORMAT(fecha,'%Y'),
  DATE_FORMAT(fecha,'%M'),
  DATE_FORMAT(fecha,'%d'),
  SUM(lluvia)
FROM
  gf_tabla
WHERE
  fecha='2002-02-05'
AND
  hora>='01:00:00' 
AND 
  hora<='02:05:00';


SELECT 
  *
FROM 
  gf_tabla 
WHERE 
  lluvia = (
    SELECT 
      MAX(lluvia) 
    from 
      gf_tabla 
    WHERE 
      DATE_FORMAT(fecha,'%Y')=2002 
    AND 
      hora>='01:00:00' 
    AND 
      hora<='1:5:0'
  ) 
AND 
  DATE_FORMAT(fecha,'%Y')=2002 
AND 
  hora>='01:00:00' 
AND 
  hora<='1:5:0';


SELECT * FROM tablaPrecipitacion;

INSERT INTO tablaPrecipitacion (anio, mes, dia,min5) VALUES (2002,'July',21,20.200000762939453);

describe tablaPrecipitacion;


INSERT INTO calculoParametros (x2,x1,y,x1y,x2y,`x1^2`,`x2^2`,x1x2) values (log10(5),log10(14), log10(162),x1*y,x2*y,POW(x1,2),POW(x2,2),x1*x2 );

select * from periodosRetorno;

select * from calculoParametros;

truncate calculoParametros;

select * from intensidadPrecipitacionTr;