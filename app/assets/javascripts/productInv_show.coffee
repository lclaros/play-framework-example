row_id = location.pathname.split('/')[2]
$ ->
  $.get "/productInvs/" + row_id, (rows) ->
    $.each rows, (index, row) ->
      $("#productId").html row.productId
      $("#proveedorId").html row.proveedorId
      $("#amount").html row.amount
      $("#amountLeft").html row.amountLeft
