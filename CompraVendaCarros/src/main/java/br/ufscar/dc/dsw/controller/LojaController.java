package br.ufscar.dc.dsw.controller;

import br.ufscar.dc.dsw.dao.LojaDAO;
import br.ufscar.dc.dsw.domain.Loja;

import java.io.IOException;
//import java.util.HashMap;
import java.util.List;
//import java.util.Map;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet(urlPatterns = "/lojas/*")
public class LojaController extends HttpServlet {

    private static final long serialVersionUID = 1L;
    
    private LojaDAO dao;

    @Override
    public void init() {
        dao = new LojaDAO();
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException {
        doGet(request, response);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException {
                
        String action = request.getPathInfo();
        if (action == null) {
            action = "";
        }

        try {
            switch (action) {
            	case "/cadastro":
            		apresentaFormCadastroLojas(request, response);
            		break;
                case "/insercao":
                	insere(request, response);
                    break;
                case "/remocao":
                	remove(request, response);
                	break;
                default:
                    lista(request, response);
                    break;
            }
        } catch (RuntimeException | IOException | ServletException e) {
            throw new ServletException(e);
        }
    }
    
    private void lista(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        List<Loja> listaLojas = dao.getAll();
        request.setAttribute("listaLojas", listaLojas);
        RequestDispatcher dispatcher = request.getRequestDispatcher("/loja/listaLojas.jsp");
        dispatcher.forward(request, response);
    }

    private void apresentaFormCadastroLojas(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {	
        RequestDispatcher dispatcher = request.getRequestDispatcher("/loja/formularioLojas.jsp");
        dispatcher.forward(request, response);
    }
    
    private void insere(HttpServletRequest request, HttpServletResponse response) 
    		throws ServletException, IOException {
    	request.setCharacterEncoding("UTF-8");
    	
    	String email = request.getParameter("email");
    	String senha = request.getParameter("senha");
    	String cnpj = request.getParameter("cnpj");
    	String nome = request.getParameter("nome");
    	String descricao = request.getParameter("descricao");
    	
    	Loja loja = new Loja(email, senha, cnpj, nome, descricao);
    	dao.insert(loja);
    	
    	// Retorna para a página do CRUD:
    	response.sendRedirect("listaLojas");
    }
    
    private void remove(HttpServletRequest request, HttpServletResponse response)
    		throws IOException {
    	String cnpj = request.getParameter("cnpj");
    	
    	Loja loja = new Loja(cnpj);
    	dao.delete(loja);
    	
    	// Retorna para a página do CRUD:
    	response.sendRedirect("listaLojas");
    }
    
}