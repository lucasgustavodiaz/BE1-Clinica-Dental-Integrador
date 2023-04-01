/* eslint-disable no-unused-vars */
/* eslint-disable no-undef */

function agregarPaciente() {
  const cedula = document.getElementById('cedula')
  const nombre = document.getElementById('nombre')
  const apellido = document.getElementById('apellido')
  const ingreso = document.getElementById('ingreso')
  const calle = document.getElementById('calle')
  const numero = document.getElementById('numero')
  const localidad = document.getElementById('localidad')
  const provincia = document.getElementById('provincia')

  const formData = {
    cedula: cedula.value,
    nombre: nombre.value,
    apellido: apellido.value,
    fechaDeIngreso: ingreso.value,
    domicilio: {
      calle: calle.value,
      numero: numero.value,
      localidad: localidad.value,
      provincia: provincia.value
    }
  }

  const settings = {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json'
    },
    body: JSON.stringify(formData)
  }

  fetch('http://localhost:8080/pacientes', settings)
    .then(response => {
      if (!response.ok) {
        throw response.status
      }
      return response.json()
    })
    .then(() => {
      Swal.fire('Agregado!', 'Paciente agregado correctamente.', 'success')
    })
    .catch(error => {
      let errorMessage = ''
      if (error === 401) {
        errorMessage = 'No está autorizado para realizar esta acción'
      } else if (error === 403) {
        errorMessage = 'Acceso prohibido'
      } else {
        errorMessage = 'Algo salió mal al guardar el Paciente!'
      }
      Swal.fire({
        icon: 'error',
        title: 'Oops...',
        text: errorMessage
      })
    })
    .finally(() => {
      cedula.value = ''
      nombre.value = ''
      apellido.value = ''
      ingreso.value = ''
      calle.value = ''
      numero.value = ''
      localidad.value = ''
      provincia.value = ''
      initDataTable()
    })
}
