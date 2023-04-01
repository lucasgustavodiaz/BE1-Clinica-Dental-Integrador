INSERT INTO DOMICILIOS(id, calle, numero, localidad, provincia)
VALUES(NEXTVAL('domicilio_seq'), 'SAN MARTIN','859','CABA','BS AS'), (NEXTVAL('domicilio_seq'), 'MITRE','3015','RIO CUARTO','CORDOBA');
INSERT INTO PACIENTES (id, nombre, apellido, cedula, fecha_de_ingreso, domicilio_id)
VALUES(NEXTVAL('paciente_seq'), 'DIEGO','MARADONA','123','2023-03-07','1'),(NEXTVAL('paciente_seq'), 'LEO', 'MESSI','456', '2023-03-07','2');
INSERT INTO ODONTOLOGOS (odontologo_id, numero_matricula, nombre, apellido)
VALUES  (NEXTVAL('odontologo_seq'), 'M007', 'LUKS', 'DIAZ'), (NEXTVAL('odontologo_seq'), 'M008','MARTIN','LOBOS');
INSERT INTO TURNOS (turno_id, fecha, odontologo_id, paciente_id)
VALUES(NEXTVAL('turno_seq'), '2023-03-07T16:00:00','1','2');