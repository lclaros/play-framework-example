Workflow



Product request
	Enviar
		Enables the finalizar button
	Finalizar
		Update request row a finalizar
			Each request row updates the inventory

Request row
	Fill(Entregado) if borrador
		udpate Inventory(-amount)
	delete
		udpate Inventory(+amount) if finalizado

Request Row Productor
	Entregar
		Updated a request row value
