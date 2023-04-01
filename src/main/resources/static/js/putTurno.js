/* eslint-disable no-unused-vars */
/* eslint-disable no-undef */

function updateTurno() {
  const id = document.getElementById('idUpdate')
  const fecha = document.getElementById('fechaUpdate')
  const hora = document.getElementById('horaUpdate')
  const id_paciente = document.getElementById('id_pacienteUpdate')
  const id_odontologo = document.getElementById('id_odontolotoUpdate')

  const formData = {
    id: id.value,
    fecha: `${fecha.value}T${hora.value}`,
    paciente: {
      id: id_paciente.value
    },
    odontologo: {
      id: id_odontologo.value
    }
  }

  const settings = {
    method: 'PUT',
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
      Swal.fire(
        'Actualizado!',
        'Paciente actualizado correctamente.',
        'success'
      )
    )
    .catch(error =>
      Swal.fire({
        icon: 'error',
        title: 'Oops...',
        text: `Algo salió mal al guardar el Turno: ${error.message}`
      })
    )
    .finally(() => {
      id.value = ''
      fecha.value.value = ''
      id_paciente.value = ''
      id_odontologo.value = ''
      initDataTable()
    })
}
