/* eslint-disable no-undef */
let dataTable
let dataTableIsInitialized = false

const dataTableOptions = {
  columnDefs: [
    { targets: [0, 1, 2, 3, 4] },
    { orderable: false, targets: [3, 4] }
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

  await listTurnos()

  dataTable = $('#datatable_turnos').DataTable(dataTableOptions)

  dataTableIsInitialized = true
}

const listTurnos = async () => {
  try {
    const response = await fetch('http://localhost:8080/turnos')
    const turnos = await response.json()

    let content = ``
    turnos.forEach(turno => {
      const [year, month, day, hour, minute] = turno.fecha;
      const fechaFormateada = new Date(Date.UTC(year, month - 1, day, hour, minute)).toISOString().slice(0, 19);
      const [fecha, hora] = fechaFormateada.split('T');
      content += `
                <tr>
                    <td>${turno.id}</td>
                    <td>${fecha + '   ' + hora}</td>
                    <td>${turno.paciente.nombre}</td>
                    <td>${turno.odontologo.nombre}</td>
                    <td>
                        <button id="btn_edit_${
                          turno.id
                        }" data-bs-toggle="modal" data-bs-target="#exampleModal2" onclick="findBy('${
        turno.id
      }')"  class="btn btn-sm btn-primary"><i class="fa-solid fa-pencil"></i></button>
                        <button id="btn_delete_${
                          turno.id
                        }" class="btnBorrar btn btn-sm btn-danger"><i class="fa-solid fa-trash-can"></i></button>
                    </td>
                </tr>`
    })
    tableBody_turnos.innerHTML = content
  } catch (ex) {
    Swal.fire({
      icon: 'error',
      title: 'Oops...',
      text: 'Error al obetener los datos!'
    })
  }
}

window.addEventListener('load', async () => {
  const formulario = document.getElementById('formularioTurno')
  formulario.addEventListener('submit', event => {
    event.preventDefault()
    agregarTurno()
  })
  const formularioUpdate = document.getElementById('formularioTurnoUpdate')
  formularioUpdate.addEventListener('submit', event => {
    event.preventDefault()
    updateTurno()
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
