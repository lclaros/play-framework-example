row_id = location.pathname.split('/')[2]
$ ->
  $.get "/productRequests/" + row_id, (rows) ->
    $.each rows, (index, row) ->
      $("#date").html row.date
      $("#veterinarioName").html row.veterinarioName
      $("#storekeeperName").html row.storekeeperName
      $("#status").html row.status
