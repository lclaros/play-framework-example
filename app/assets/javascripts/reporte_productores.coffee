$ ->
  $.get "/productor_report", (rows) ->
    $.each rows, (index, row) ->
      name = $("<td>").text row.name
      cuenta = $("<td>").text row.cuenta
      $("#rows").append $("<tr>").append(name).append(cuenta)
