row_id = location.pathname.split('/')[2]
$ ->
  $.get "/requestRows/" + row_id, (rows) ->
    $.each rows, (index, row) ->
      $("#Id").html row.id
      $("#requestId").html row.requestId
      $("#productName").html row.productName
      $("#quantity").html row.quantity
      $("#status").html row.status
