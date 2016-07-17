row_id = location.pathname.split('/')[2]
$ ->
  $.get "/requestRowProductors/" + row_id, (rows) ->
    $.each rows, (index, row) ->
      $("#requestRowId").html row.requestRowId
      $("#productId").html row.productId
      $("#productorId").html row.productorId
      $("#quantity").html row.quantity
      $("#status").html row.status
