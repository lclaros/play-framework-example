row_id = location.pathname.split('/')[2]
$ ->
  $.get "/accountChildren/" + row_id, (rows) ->
    $.each rows, (index, row) ->
      id = $("<td>").text row.id
      code = $("<td>").text row.code
      name = $("<td>").text row.name
      links = $("<td>").html '<a href="/account_update/' + row.id + '" class="btn btn-primary">Editar</a>' + '<a href="/account_remove/' + row.id + '" class="btn btn-danger">Eliminar</a>' + '<a href="/account_show/' + row.id + '" class="btn btn-info">Mostrar</a>'
      $("#children").append $("<tr>").append(id).append(code).append(name).append(links)
