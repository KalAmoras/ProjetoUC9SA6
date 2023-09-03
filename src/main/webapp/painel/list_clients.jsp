<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ page isELIgnored="false"%>
<%@ page import="java.util.List"%>
<%@ page import="java.sql.Connection"%>
<%@ page import="java.util.ArrayList"%>
<%@ page import="models.Agendamento"%>
<%@ page import="dao.AgendamentoDAO"%>
<%
@SuppressWarnings("unchecked")
ArrayList<Agendamento> list = (ArrayList<Agendamento>) request.getAttribute("Schedules");
%>

<%@ page import="connection.ConnectionMySQL"%>

<!DOCTYPE html>

<html>
<head>
<meta charset="ISO-8859-1">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<link rel="stylesheet" href="styles/lista.css">
<link rel="stylesheet" href="styles/table.css">
<link
	href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.1/dist/css/bootstrap.min.css"
	rel="stylesheet"
	integrity="sha384-4bw+/aepP/YC94hEpVNVgiZdgIC5+VKNBQNGCHeKRQN+PtmoHDEXuppvnDJzQIu9"
	crossorigin="anonymous">
<link rel="stylesheet"
	href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0-beta3/css/all.min.css">

<title>Lista</title>
</head>
<body>
	<nav class="navbar navbar-expand-lg navbar-dark bg-custom">
		<div class="container">
			<button class="navbar-toggler" type="button"
				data-bs-toggle="collapse" data-bs-target="#navbarNav"
				aria-controls="navbarNav" aria-expanded="false"
				aria-label="Toggle navigation">
				<span class="navbar-toggler-icon"></span>
			</button>
			<div
				class="collapse navbar-collapse align-items-center justify-content-center text-center"
				id="navbarNav">
				<ul class="navbar-nav">
					<li class="nav-item"><a class="nav-link" href="index.jsp">Home</a>
					</li>
					<li class="nav-item"><%
						String usuarioAtivo = (String) session.getAttribute("usuario");
						if (usuarioAtivo != null) {
						%>
						<label class="nav-link">User: <%= usuarioAtivo %></label>
						<%
						}
						%>
					</li>
					<li class="nav-item"><a class="nav-link" href="logout">Logout</a>
					</li>
				</ul>
			</div>
		</div>
	</nav>
	<main class="main-container">
	      <div class="btn-add-position" style="padding: 20px;">
				<button type="submit" class="btn-add" id="btnMostrarForm">
				<i class="fas fa-user-plus" style="font-size: 48px;"></i>
			</button>
		  </div>
		<div class="add-cliente" id="formulario-adicionar">
			<h1 class="main-heading">Adicionar Novo Agendamento</h1>
			<form action="insert" class="adicionar-form">
				<div class="form-group">
					<label for="date">Data:</label> <input type="date"
						id="nome" name="date" required>
				</div>
				<div class="form-group">
					<label for="time">Horário:</label> <input type="time"
						id="endereco" name="time" required>
				</div>
				<div class="justify-content-center d-flex">
					<button type="submit" class="btn-add mt-3 mb-3">
						<i class="fas fa-user-plus" style="font-size: 48px;"></i>
					</button>
				</div>
			</form>
		</div>
		<div class="lista-clientes">
		   <h1 class="main-heading">Lista de Agendamentos</h1>
			<div class="table-div">
					<table class="custom-table">
						<thead class="custom-thead">
						<tr>
							<th>Id</th>
							<th>Data</th>
							<th>Horário</th>
							<th>Editar</th>
							<th>Deletar</th>
						</tr>
						</thead>
						<tbody class="custom-tbody">
						<%
							if (list != null) {
							    for (Agendamento agendamento : list) {
							%>
						<tr>
							<td data-label="Id"><span><%=agendamento.getId()%></span></td>
							<td data-label="Data"><span class="editable" data-field="date" data-input-type="date"><%=agendamento.getDate()%></span></td>
							<td data-label="Horario"><span class="editable" data-field="time" data-input-type="time"><%=agendamento.getTime()%></span></td>
							<td data-label="Editar">
								<button class="btn btn-warning editar-cliente"
									data-id="<%=agendamento.getId()%>">
									<i class="fas fa-edit"></i>
								</button>
							</td>
							<td data-label="Deletar">
								<form action="delete" >
    								<input type="hidden" name="id" value="<%= agendamento.getId() %>">
									    <button type="submit" class="btn btn-danger">
									        <i class="fas fa-trash-alt"></i>
									    </button>
									</form>
							</td>
						</tr>		
						<%
						}
							}
						%>
						</tbody>
					</table>
				</div>
			</div>
	</main>
	<script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
	<script>
		document.addEventListener("DOMContentLoaded", function() {
			const btnMostrarForm = document.getElementById("btnMostrarForm");
			const formularioAdicionar = document
					.getElementById("formulario-adicionar");

			btnMostrarForm.addEventListener("click", function() {
				formularioAdicionar.classList.toggle("visible");
				formularioAdicionar.classList.toggle("fade-in");
				btnMostrarForm.classList.toggle("active");
			});
		});
	</script>

	<script>
    $(document).ready(function () {
        $(".editar-cliente").on("click", function () {
            var clienteId = $(this).data("id");
            var tr = $(this).closest("tr");

            tr.find(".editable").each(function () {
                var valor = $(this).text();
                var campo = $(this).data("field");
                var tipoInput = $(this).data("input-type");
                $(this).html("<input style='width: 300px;' type='" + tipoInput + "' name='" + campo + "' value='" + valor + "'>");
            });

            tr.find(".editar-cliente").replaceWith("<button class='btn btn-success salvar-cliente' data-id='" + clienteId + "'><i class='fas fa-save'></i></button>");
        });

        $(document).on("click", ".salvar-cliente", function () {
            var clienteId = $(this).data("id");
            var tr = $(this).closest("tr");
            var date = tr.find("[data-field='date'] input").val();
            var time = tr.find("[data-field='time'] input").val();

            $.ajax({
                type: "POST",
                url: "update", // Corrija o URL para o servlet de atualização
                data: {
                    id: clienteId, // Use 'id' em vez de 'matricula'
                    date: date,
                    time: time,
                },
                success: function () {
                	var formattedDate = new Date(date).toLocaleDateString();
                    var formattedTime = new Date(time).toLocaleTimeString();
                	
                    tr.find(".editable[data-field='date']").text(date); // Atualize o texto do campo 'date'
                    tr.find(".editable[data-field='time']").text(time); // Atualize o texto do campo 'time'
                    tr.find(".salvar-cliente").replaceWith("<button class='btn btn-warning editar-cliente' data-id='" + clienteId + "'><i class='fas fa-edit'></i></button>");
                    tr.find(".btn-danger").show();
                    location.reload();
                }
            });
        });
    });
</script>
	<script
		src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.1/dist/js/bootstrap.bundle.min.js"
		integrity="sha384-HwwvtgBNo3bZJJLYd8oVXjrBZt8cqVSpeBNS5n7C8IVInixGAoxmnlMuBnhbgrkm"
		crossorigin="anonymous"></script>
</body>
</html>