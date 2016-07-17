row_id = location.pathname.split('/')[2]
$ ->
  $.get "/productorsByModule/" + row_id, (rows) ->
    $.each rows, (index, row) ->
      name = $("<td>").text row.name
      carnet = $("<td>").text row.carnet
      links = $("<td>").html '<a href="/requestRow_show/' + row.id + '" class="btn btn-info btn-sm">Mostrar</a>'
      $("#rows").append $("<tr>").append(name).append(carnet).append(links)
