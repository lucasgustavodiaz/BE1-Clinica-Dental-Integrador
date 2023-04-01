/* eslint-disable no-undef */
/* eslint-disable no-unused-vars */
function deleteBy(id, fila) {
  const url = 'http://localhost:8080/pacientes/' + id
  const settings = {
    method: 'DELETE'
  }

  Swal.fire({
    title: 'Estas seguro?',
    text: 'No podrás revertir este cambio!',
    icon: 'warning',
    showCancelButton: true,
    confirmButtonColor: '#3085d6',
    cancelButtonColor: '#d33',
    confirmButtonText: 'Si, borrar!'
  }).then(result => {
    if (result.isConfirmed) {
      fetch(url, settings).then(response => {
        if (response.status === 200) {
          Swal.fire(
            'Borrado!',
            'Paciente con id: ' + id + ' ha sido borrado.',
            'success'
          )
          dataTable.row(fila).remove().draw()
        } else
          Swal.fire({
            icon: 'error',
            title: 'Oops...',
            text: 'Algo salió mal!'
          })
      })
    }
  })
}
