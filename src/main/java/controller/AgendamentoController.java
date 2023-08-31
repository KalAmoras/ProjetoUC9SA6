package controller;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import dao.AgendamentoDAO;
import models.Agendamento;
import models.Usuario;
import java.io.IOException;
import java.util.ArrayList;

@WebServlet(urlPatterns = { "/controller", "/main", "/insert", "/select", "/update", "/delete", "/login" })
public class AgendamentoController extends HttpServlet {
	private static final long serialVersionUID = 1L;
	AgendamentoDAO dao = new AgendamentoDAO();
	Agendamento agendamento = new Agendamento();

	public AgendamentoController() {
		super();
	}
	
	@Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
		String action = request.getServletPath();

	    if (action.equals("/update")) {
	        UpdateSchedule(request, response);
	    }
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		
		String action = request.getServletPath();

		if(action.equals("/login")) {
			Login(request, response);
		}else if (action.equals("/main")) {
			Schedule(request, response);
		} else if (action.equals("/insert")) {
			Create(request, response);
		} else if (action.equals("/select")) {
			List(request, response);
		} else if (action.equals("/update")) {
			UpdateSchedule(request, response);
		} else if (action.equals("/delete")) {
			DeleteSchedule(request, response);
		} else {
			response.sendRedirect("index.jsp");
		}
	}

	protected void Schedule(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		ArrayList<Agendamento> list = dao.listSchedule();
		request.setAttribute("Schedules", list);
		RequestDispatcher rd = request.getRequestDispatcher("painel/list_clients.jsp");
		rd.forward(request, response);
	}

	protected void Create(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		try {
			agendamento.setDate(request.getParameter("date"));
			agendamento.setTime(request.getParameter("time"));

			System.out.print(agendamento);
			dao.insertSchedule(agendamento);

			response.sendRedirect("main");
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	protected void List(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
			int id;
		try {
			String idParam = request.getParameter("id");
			id = Integer.parseInt(idParam);
			agendamento.setId(id);
			dao.getScheduleById(agendamento);

			request.setAttribute("id", agendamento.getId());
			request.setAttribute("date", agendamento.getDate());
			request.setAttribute("time", agendamento.getTime());
			RequestDispatcher rd = request.getRequestDispatcher("painel/edit_client.jsp");
			rd.forward(request, response);
		} catch (NumberFormatException e) {
			id = 0;
		}
	}

	protected void UpdateSchedule(HttpServletRequest request, HttpServletResponse response)
	        throws ServletException, IOException {
	    int id;
	    try {
	        String idParam = request.getParameter("id");
	        id = Integer.parseInt(idParam);
	        agendamento.setId(id);
	        agendamento.setDate(request.getParameter("date"));
	        agendamento.setTime(request.getParameter("time"));
	        
	        dao.updateSchedule(agendamento);
	        response.sendRedirect("main");
	    } catch (NumberFormatException e) {
	        e.printStackTrace();
	        response.sendRedirect("main"); // Redirecione mesmo em caso de erro
	    }
	}
	
	protected void DeleteSchedule(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		int id;
		try {
			String idParam = request.getParameter("id");
			id = Integer.parseInt(idParam);
			agendamento.setId(id);
			dao.deleteSchedule(agendamento);
			response.sendRedirect("main");
		} catch (NumberFormatException e) {
			id = 0;
		}
	}

	protected void Login(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String user = request.getParameter("user");
		String password = request.getParameter("password");
		
		if (user != null && !user.isEmpty() && password != null && !password.isEmpty()) {
			Usuario usuario1 = new Usuario();
			usuario1.setUsuario(user);
			usuario1.setSenha(password);
			
			request.getSession().setAttribute(user, usuario1.getUsuario());
			RequestDispatcher redirecionar = request.getRequestDispatcher("schedule/index.html");
			redirecionar.forward(request, response);
		}else {
			RequestDispatcher redirecionar = request.getRequestDispatcher("index.jsp");
			request.setAttribute("mensagem", "Informe o Usu�rio e Senha corretamenet!!!");
			redirecionar.forward(request, response);
		}
	}
	
}