$ ->
  $.get "/productores_report", (rows) ->
    $.each rows, (index, row) ->
      nombre = $("<td>").text row.nombre
      cuenta = $("<td>").text row.cuenta
      $("#rows").append $("<tr>").append(nombre).append(cuenta)
