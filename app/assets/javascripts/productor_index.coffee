$ ->
  $.get "/productores", (rows) ->
    $.each rows, (index, row) ->
      nombre = $("<td>").text row.nombre
      carnet = $("<td>").text row.carnet
      telefono = $("<td>").text row.telefono
      account = $("<td>").text row.account
      moduleName = $("<td>").text row.moduleName
      link = $("<td>").html '<a href="/productor_update/' + row.id + '" class="btn btn-primary">Editar</a>' + '<a href="/productor_remove/' + row.id + '" class="btn btn-danger">Eliminar</a>' + '<a href="/productor_show/' + row.id + '" class="btn btn-info">Mostrar</a>'
      $("#rows").append $("<tr>").append(nombre).append(carnet).append(telefono).append(account).append(moduleName).append(link)