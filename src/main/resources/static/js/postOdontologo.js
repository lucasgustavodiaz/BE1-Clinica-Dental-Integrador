/* eslint-disable no-unused-vars */
/* eslint-disable no-undef */

function agregarOdontologo() {
  const nombre = document.getElementById('nombre')
  const apellido = document.getElementById('apellido')
  const numeroMatricula = document.getElementById('numeroMatricula')

  const formData = {
    numeroMatricula: numeroMatricula.value,
    nombre: nombre.value,
    apellido: apellido.value
  }

  const settings = {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json'
    },
    body: JSON.stringify(formData)
  }

  fetch('http://localhost:8080/odontologos', settings)
    .then(response => {
      if (!response.ok) {
        throw response.status
      }
      return response.json()
    })
    .then(() => {
      Swal.fire('Agregado!', 'Ondotólogo agregado correctamente.', 'success')
    })
    .catch(error => {
      let errorMessage = ''
      if (error === 401) {
        errorMessage = 'No está autorizado para realizar esta acción'
      } else if (error === 403) {
        errorMessage = 'Acceso prohibido'
      } else {
        errorMessage = 'Algo salió mal al guardar el Odontólogo!'
      }
      Swal.fire({
        icon: 'error',
        title: 'Oops...',
        text: errorMessage
      })
    })
    .finally(() => {
      numeroMatricula.value = ''
      nombre.value = ''
      apellido.value = ''
      initDataTable()
    })
}
