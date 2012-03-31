CREATE TABLE IF NOT EXISTS gf_tabla
(
  fecha          DATE,
  hora           TIME,
  idx_temp_hum   FLOAT,
  temp_ext       FLOAT,
  factor_viento  FLOAT,
  temp_max       FLOAT,
  temp_min       FLOAT,
  hum_ext        INT,
  rocio          FLOAT,
  vel_viento     FLOAT,
  max            FLOAT,
  direccion      VARCHAR(5),
  lluvia         FLOAT,
  temp_int       FLOAT,
  hum_int        FLOAT,
  var_a          INT,
  var_b          INT,
  UNIQUE (fecha, hora)
);

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

CREATE TABLE IF NOT EXISTS intensidad_mm_h (
anio    INT PRIMARY KEY,
min5           FLOAT DEFAULT -1,
min10          FLOAT DEFAULT -1,
min20          FLOAT DEFAULT -1,
min60          FLOAT DEFAULT -1,
min90          FLOAT DEFAULT -1,
min120         FLOAT DEFAULT -1
);

CREATE TABLE IF NOT EXISTS periodosRetorno (
orden  INT PRIMARY KEY,
tr     FLOAT NOT NULL,
min5           FLOAT DEFAULT -1,
min10          FLOAT DEFAULT -1,
min20          FLOAT DEFAULT -1,
min60          FLOAT DEFAULT -1,
min90          FLOAT DEFAULT -1,
min120         FLOAT DEFAULT -1
);

CREATE TABLE IF NOT EXISTS intensidadPrecipitacionTr(
tr     INT PRIMARY KEY,
min5   FLOAT NOT NULL,
min10  FLOAT NOT NULL,
min20  FLOAT NOT NULL,
min60  FLOAT NOT NULL,
min90  FLOAT NOT NULL,
min120 FLOAT NOT NULL
);