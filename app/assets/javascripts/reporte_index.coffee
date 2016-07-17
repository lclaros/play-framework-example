$ ->
  $.get "/reportes", (reportes) ->
    $.each reportes, (index, reporte) ->
      monto = $("<td>").text reporte.monto
      cuenta = $("<td>").text reporte.cuenta
      cliente = $("<td>").text reporte.cliente
      $("#reportes").append $("<tr>").append(monto).append(cuenta).append(cliente)