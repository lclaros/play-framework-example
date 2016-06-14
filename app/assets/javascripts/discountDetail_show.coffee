row_id = location.pathname.split('/')[2]
$ ->
  $.get "/discountDetails/" + row_id, (rows) ->
    $.each rows, (index, row) ->
      $("#discountReport").html row.discountReport
      $("#productorId").html row.productorId
      $("#status").html row.status
      $("#discount").html row.discount
