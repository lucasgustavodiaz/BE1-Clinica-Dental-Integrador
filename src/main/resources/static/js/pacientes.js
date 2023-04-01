/* eslint-disable no-undef */
let dataTable
let dataTableIsInitialized = false

const dataTableOptions = {
  columnDefs: [
    { targets: [0, 1, 2, 3, 4, 5, 6, 7, 8] },
    { orderable: false, targets: [7, 8] }
  ],
  pageLength: 10,
  destroy: true,
  resposive: true,
  language: { url: '//cdn.datatables.net/plug-ins/1.10.21/i18n/Spanish.json' }
}

const initDataTable = async () => {
  if (dataTableIsInitialized) {
    dataTable.destroy()
  }

  await listUsers()

  dataTable = $('#datatable_pacientes').DataTable(dataTableOptions)

  dataTableIsInitialized = true
}

const listUsers = async () => {
  try {
    const response = await fetch('http://localhost:8080/pacientes')
    if (response.status === 403) {
      Swal.fire({
        icon: 'error',
        title: 'Oops...',
        text: 'No estás autorizado para realizar esta acción'
      })
      return
    }

    const users = await response.json()
    let content = ``
    users.forEach(user => {
      const [year, month, day] = user.fechaDeIngreso
      const fechaFormateada = new Date(Date.UTC(year, month - 1, day))
        .toISOString()
        .slice(0, 19)
      const [fecha, hora] = fechaFormateada.split('T')
      content += `
                <tr>
                    <td>${user.id}</td>
                    <td>${user.cedula}</td>
                    <td>${user.nombre}</td>
                    <td>${user.apellido}</td>
                    <td>${fecha}</td>
                    <td>${user.domicilio.calle}</td>
                    <td>${user.domicilio.numero}</td>
                    <td>${user.domicilio.localidad}</td>
                    <td>${user.domicilio.provincia}</td>
                    <td>
                        <button id="btn_edit_${
                          user.id
                        }" data-bs-toggle="modal" data-bs-target="#exampleModal2" onclick="findBy('${
        user.id
      }')"  class="btn btn-sm btn-primary"><i class="fa-solid fa-pencil"></i></button>
                        <button id="btn_delete_${
                          user.id
                        }" class="btnBorrar btn btn-sm btn-danger"><i class="fa-solid fa-trash-can"></i></button>
                    </td>
                </tr>`
    })
    tableBody_pacientes.innerHTML = content
  } catch (ex) {
    Swal.fire({
      icon: 'error',
      title: 'Oops...',
      text: 'Error al obetener los datos!'
    })
  }
}

window.addEventListener('load', async () => {
  const formulario = document.getElementById('formularioPaciente')
  formulario.addEventListener('submit', event => {
    event.preventDefault()
    agregarPaciente()
  })
  const formularioUpdate = document.getElementById('formularioPacienteUpdate')
  formularioUpdate.addEventListener('submit', event => {
    event.preventDefault()
    updatePaciente()
  })
  await initDataTable()
})

const on = (element, event, selector, handler) => {
  element.addEventListener(event, e => {
    if (e.target.closest(selector)) {
      handler(e)
    }
  })
}

on(document, 'click', '.btnBorrar', event => {
  const fila = event.target.closest('tr')
  const id = fila.querySelector('td:nth-child(1)').textContent
  deleteBy(id, fila)
})
