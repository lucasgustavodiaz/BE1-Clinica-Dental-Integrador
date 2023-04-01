/* eslint-disable no-unused-vars */
/* eslint-disable no-undef */

function agregarTurno() {
  const fecha = document.getElementById('fecha')
  const hora = document.getElementById('hora')
  const id_paciente = document.getElementById('id_paciente')
  const id_odontologo = document.getElementById('id_odontoloto')

  const formData = {
    fecha: `${fecha.value}T${hora.value}`,
    paciente: {
      id: id_paciente.value
    },
    odontologo: {
      id: id_odontologo.value
    }
  }

  const settings = {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json'
    },
    body: JSON.stringify(formData)
  }

  fetch('http://localhost:8080/turnos', settings)
    .then(response => {
      if (response.headers.get('content-type') === 'application/json') {
        return response.json()
      } else {
        return response.text().then(text => {
          throw new Error(text)
        })
      }
    })
    .then(() =>
      Swal.fire('Agregado!', 'Turno agregado correctamente.', 'success')
    )
    .catch(error =>
      Swal.fire({
        icon: 'error',
        title: 'Oops...',
        text: `Algo salió mal al guardar el Turno: ${error.message}`
      })
    )
    .finally(() => {
      fecha.value.value = ''
      id_paciente.value = ''
      id_odontologo.value = ''
      initDataTable()
    })
}
