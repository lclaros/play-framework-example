row_id = location.pathname.split('/')[2]
$ ->
  $.get "/requestRowProductors/" + row_id, (rows) ->
    $.each rows, (index, row) ->
      $("#requestRowId").html row.requestRowId
      $("#productName").html row.productName
      $("#productorName").html row.productorName
      $("#quantity").html row.quantity
      $("#status").html row.status
