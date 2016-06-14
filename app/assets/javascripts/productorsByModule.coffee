row_id = location.pathname.split('/')[2]
$ ->
  $.get "/productorsByModule/" + row_id, (rows) ->
    $.each rows, (index, row) ->
      nombre = $("<td>").text row.nombre
      carnet = $("<td>").text row.carnet
      links = $("<td>").html '<a href="/requestRow_show/' + row.id + '" class="btn btn-info">Mostrar</a>'
      $("#rows").append $("<tr>").append(nombre).append(carnet).append(links)
