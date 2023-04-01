/* eslint-disable no-unused-vars */
/* eslint-disable no-undef */

function updateOdontologo() {
  const id = document.getElementById('idUpdate')
  const nombre = document.getElementById('nombreUpdate')
  const apellido = document.getElementById('apellidoUpdate')
  const numeroMatricula = document.getElementById('numeroMatriculaUpdate')

  const formData = {
    id: id.value,
    numeroMatricula: numeroMatricula.value,
    nombre: nombre.value,
    apellido: apellido.value
  }

  const settings = {
    method: 'PUT',
    headers: {
      'Content-Type': 'application/json'
    },
    body: JSON.stringify(formData)
  }

  fetch('http://localhost:8080/odontologos', settings)
    .then(response => response.json())
    .then(() =>
      Swal.fire(
        'Actualizado!',
        'Ondotólogo actualizado correctamente.',
        'success'
      )
    )
    .catch(() =>
      Swal.fire({
        icon: 'error',
        title: 'Oops...',
        text: 'Algo salió mal al actualizar el Odontólogo!'
      })
    )
    .finally(() => {
      id.value = ''
      numeroMatricula.value = ''
      nombre.value = ''
      apellido.value = ''
      initDataTable()
    })
}
