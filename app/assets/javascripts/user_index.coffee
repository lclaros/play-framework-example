$ ->
  $.get "/users", (rows) ->
    $.each rows, (index, row) ->
      name = $("<td>").text row.name
      carnet = $("<td>").text row.carnet
      telefono = $("<td>").text row.telefono
      direccion = $("<td>").text row.direccion
      sueldo = $("<td>").text row.sueldo
      type_1 = $("<td>").text row.type_1
      links = $("<td>").html '<a href="/user_update/' + row.id + '" class="btn btn-primary btn-sm">Editar</a>' + '<a href="/user_remove/' + row.id + '" class="btn btn-danger btn-sm">Eliminar</a>' + '<a href="/user_show/' + row.id + '" class="btn btn-info btn-sm">Mostrar</a>' + '<a href="/profileById/' + row.id + '" class="btn btn-info btn-sm">Profile</a>'
      $("#rows").append $("<tr>").append(name).append(carnet).append(telefono).append(direccion).append(sueldo).append(type_1).append(links)