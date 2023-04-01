/* eslint-disable no-unused-vars */
function findBy(id) {
  const url = 'http://localhost:8080/odontologos/' + id

  fetch(url)
    .then(response => response.json())
    .then(data => cargarDatosEnFormulario(data))
}

function cargarDatosEnFormulario(odontologo) {
  const id = document.getElementById('idUpdate')
  const nombre = document.getElementById('nombreUpdate')
  const apellido = document.getElementById('apellidoUpdate')
  const numeroMatricula = document.getElementById('numeroMatriculaUpdate')

  id.value = odontologo.id
  nombre.value = odontologo.nombre
  apellido.value = odontologo.apellido
  numeroMatricula.value = odontologo.numeroMatricula
}
