row_id = location.pathname.split('/')[2]

$ ->
  $.get "/productRequestsByInsumosByUser/" + row_id, (rows) ->
    $.each rows, (index, row) ->
      date = $("<td>").text row.date
      status = $("<td>").text row.status
      moduleName = $("<td>").text row.moduleName
      detail = $("<td>").text row.detail
      links = $("<td>").html '<a href="/productRequestByInsumo_send/' + row.id + '" class="btn btn-primary">Enviar</a>' + '<a href="/productRequestByInsumo_accept/' + row.id + '" class="btn btn-primary">Aceptar</a>' + '<a href="/productRequestByInsumo_finish/' + row.id + '" class="btn btn-primary">Finalizar</a>' + '<a href="/productRequestByInsumo_update/' + row.id + '" class="btn btn-primary">Editar</a>' + '<a href="/productRequestByInsumo_remove/' + row.id + '" class="btn btn-danger">Eliminar</a>' + '<a href="/productRequestByInsumo_show/' + row.id + '" class="btn btn-info">Mostrar</a>'
      $("#rows").append $("<tr>").append(date).append(status).append(detail).append(moduleName).append(links)
