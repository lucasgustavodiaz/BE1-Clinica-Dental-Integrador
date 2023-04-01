/* eslint-disable no-unused-vars */
function findBy(id) {
  const url = 'http://localhost:8080/turnos/' + id

  fetch(url)
    .then(response => response.json())
    .then(data => cargarDatosEnFormulario(data))
}

function cargarDatosEnFormulario(turno) {
  const id = document.getElementById('idUpdate')
  const fecha = document.getElementById('fechaUpdate')
  const hora = document.getElementById('horaUpdate')
  const id_paciente = document.getElementById('id_pacienteUpdate')
  const id_odontologo = document.getElementById('id_odontolotoUpdate')

  const [year, month, day, hour, minute] = turno.fecha
  const fechaFormateada = new Date(Date.UTC(year, month - 1, day, hour, minute))
    .toISOString()
    .slice(0, 19)
  const date = fechaFormateada.split('T')

  id.value = turno.id
  fecha.value = date[0]
  hora.value = date[1]
  id_paciente.value = turno.paciente.id
  id_odontologo.value = turno.odontologo.id
}
