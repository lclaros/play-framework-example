row_id = location.pathname.split('/')[2]
$ ->
  $.get "/transactionDetails/" + row_id, (rows) ->
    $.each rows, (index, row) ->
      $("#transactionId").html row.transactionId
      $("#accountId").html row.accountId
      $("#amount").html row.amount
