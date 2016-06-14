$ ->
  $.get "/users", (rows) ->
    $.each rows, (index, row) ->
      nombre = $("<td>").text row.nombre
      carnet = $("<td>").text row.carnet
      telefono = $("<td>").text row.telefono
      direccion = $("<td>").text row.direccion
      sueldo = $("<td>").text row.sueldo
      type_1 = $("<td>").text row.type_1
      links = $("<td>").html '<a href="/user_update/' + row.id + '" class="btn btn-primary">Editar</a>' + '<a href="/user_remove/' + row.id + '" class="btn btn-danger">Eliminar</a>' + '<a href="/user_show/' + row.id + '" class="btn btn-info">Mostrar</a>' + '<a href="/profileById/' + row.id + '" class="btn btn-info">Profile</a>'
      $("#rows").append $("<tr>").append(nombre).append(carnet).append(telefono).append(direccion).append(sueldo).append(type_1).append(links)