/* eslint-disable no-unused-vars */
function findBy(id) {
  const url = 'http://localhost:8080/pacientes/' + id

  fetch(url)
    .then(response => response.json())
    .then(data => cargarDatosEnFormulario(data))
}

function cargarDatosEnFormulario(paciente) {
  const id = document.getElementById('idUpdate')
  const cedula = document.getElementById('cedulaUpdate')
  const nombre = document.getElementById('nombreUpdate')
  const apellido = document.getElementById('apellidoUpdate')
  const ingreso = document.getElementById('ingresoUpdate')
  const id_domicilio = document.getElementById('id_domicilioUpdate')
  const calle = document.getElementById('calleUpdate')
  const numero = document.getElementById('numeroUpdate')
  const localidad = document.getElementById('localidadUpdate')
  const provincia = document.getElementById('provinciaUpdate')

  const [year, month, day] = paciente.fechaDeIngreso
  const fechaFormateada = new Date(Date.UTC(year, month - 1, day))
    .toISOString()
    .slice(0, 19)
  const [fecha, hora] = fechaFormateada.split('T')

  id.value = paciente.id
  cedula.value = paciente.cedula
  nombre.value = paciente.nombre
  apellido.value = paciente.apellido
  ingreso.value = fecha
  id_domicilio.value = paciente.domicilio.id
  calle.value = paciente.domicilio.calle
  numero.value = paciente.domicilio.numero
  localidad.value = paciente.domicilio.localidad
  provincia.value = paciente.domicilio.provincia
}
