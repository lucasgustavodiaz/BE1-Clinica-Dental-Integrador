/* eslint-disable no-unused-vars */
/* eslint-disable no-undef */

function updatePaciente() {
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

  const formData = {
    id: id.value,
    cedula: cedula.value,
    nombre: nombre.value,
    apellido: apellido.value,
    fechaDeIngreso: ingreso.value,
    domicilio: {
      id: id_domicilio.value,
      calle: calle.value,
      numero: numero.value,
      localidad: localidad.value,
      provincia: provincia.value
    }
  }

  const settings = {
    method: 'PUT',
    headers: {
      'Content-Type': 'application/json'
    },
    body: JSON.stringify(formData)
  }

  fetch('http://localhost:8080/pacientes', settings)
    .then(response => response.json())
    .then(() =>
      Swal.fire(
        'Actualizado!',
        'Paciente actualizado correctamente.',
        'success'
      )
    )
    .catch(() =>
      Swal.fire({
        icon: 'error',
        title: 'Oops...',
        text: 'Algo salió mal al actualizar el Paciente!'
      })
    )
    .finally(() => {
      id.value = ''
      cedula.value = ''
      nombre.value = ''
      apellido.value = ''
      ingreso.value = ''
      id_domicilio.value = ''
      calle.value = ''
      numero.value = ''
      localidad.value = ''
      provincia.value = ''
      initDataTable()
    })
}
