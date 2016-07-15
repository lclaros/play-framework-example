row_id = location.pathname.split('/')[2]

$ ->
  $.get "/productRequestsByInsumoUser/" + row_id, (rows) ->
    $.each rows, (index, row) ->
      date = $("<td>").text row.date
      veterinario = $("<td>").text row.veterinario
      storekeeper = $("<td>").text row.storekeeper
      status = $("<td>").text row.status
      detail = $("<td>").text row.detail
      links = $("<td>").html '<a href="/productRequest_send/' + row.id + '" class="btn btn-primary btn-sm">Enviar</a>' + '<a href="/productRequest_accept/' + row.id + '" class="btn btn-primary btn-sm">Aceptar</a>' + '<a href="/productRequest_finish/' + row.id + '" class="btn btn-primary btn-sm">Finalizar</a>' + '<a href="/productRequest_update/' + row.id + '" class="btn btn-primary btn-sm">Editar</a>' + '<a href="/productRequest_remove/' + row.id + '" class="btn btn-danger btn-sm">Eliminar</a>' + '<a href="/productRequest_show/' + row.id + '" class="btn btn-info btn-sm">Mostrar</a>'
      $("#rows").append $("<tr>").append(date).append(veterinario).append(storekeeper).append(status).append(detail).append(links)
