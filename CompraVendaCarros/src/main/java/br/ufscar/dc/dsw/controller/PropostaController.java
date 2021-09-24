package br.ufscar.dc.dsw.controller;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import br.ufscar.dc.dsw.dao.PropostaDAO;
import br.ufscar.dc.dsw.dao.VeiculoDAO;
import br.ufscar.dc.dsw.dao.LojaDAO;

import br.ufscar.dc.dsw.domain.Proposta;
import br.ufscar.dc.dsw.domain.Veiculo;
import br.ufscar.dc.dsw.domain.Cliente;
import br.ufscar.dc.dsw.domain.Loja;

//import br.ufscar.dc.dsw.util.Erro;

@WebServlet(urlPatterns = "/proposta/*")
public class PropostaController extends HttpServlet {

    private static final long serialVersionUID = 1L;

    private PropostaDAO dao;

    @Override
    public void init() {
        dao = new PropostaDAO();
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		Cliente cliente = (Cliente) request.getSession().getAttribute("usuarioLogado");
		//Erro erros = new Erro();

		if (cliente == null) {
			response.sendRedirect(request.getContextPath());
			return;
		} /*else if (!cliente.getPapel().equals("USER")) {
			erros.add("Acesso não autorizado!");
			erros.add("Apenas Papel [USER] tem acesso a essa página");
			request.setAttribute("mensagens", erros);
			RequestDispatcher rd = request.getRequestDispatcher("/noAuth.jsp");
			rd.forward(request, response);
			return;
		}*/
		
        String action = request.getPathInfo();
        if (action == null) {
            action = "";
        }

        try {
            switch (action) {
                case "/formProposta":
                    apresentaFormProposta(request, response);
                    break;
                case "/insercaoProposta":
                    insereProposta(request, response);
                    break;
                    
                case "/logado/loja/listarPropostas":
                	lista_por_loja(request,response);
                	break;
                case "/logado/cliente/listarPropostas":
                	lista_por_cliente(request,response);
                	break;
                default:
                    lista(request, response);
                    break;
            }
        } catch (RuntimeException | IOException | ServletException e) {
            throw new ServletException(e);
        }
    }

    private void lista_por_cliente(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    	Cliente cliente = (Cliente) request.getSession().getAttribute("usuarioLogado");
        List<Proposta> listaPropostas = dao.getAllbyCliente(cliente.getId());
        request.setAttribute("listaPropostas", listaPropostas);
        RequestDispatcher dispatcher = request.getRequestDispatcher("/logado/cliente/propostas.jsp");
        dispatcher.forward(request, response);
    }
    
    private void lista_por_loja(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    	Loja loja = (Loja) request.getSession().getAttribute("lojalogada");
        List<Proposta> listaPropostas = dao.getAllbyLoja(loja.getId());
        request.setAttribute("listaPropostas", listaPropostas);
        RequestDispatcher dispatcher = request.getRequestDispatcher("/logado/cliente/propostas.jsp");
        dispatcher.forward(request, response);
    }
    
    private void lista(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        List<Proposta> listaPropostas = dao.getAll();
        request.setAttribute("listaVeiculos", listaPropostas);
        RequestDispatcher dispatcher = request.getRequestDispatcher("/logado/admin/propostas.jsp");
        dispatcher.forward(request, response);
    }

    private Map<Long, Veiculo> getVeiculos() {
        Map<Long, Veiculo> veiculos = new HashMap<>();
        for (Veiculo veiculo: new VeiculoDAO().getAll()) {
            veiculos.put(veiculo.getId(), veiculo);
        }
        return veiculos;
    }

    private void apresentaFormProposta(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setAttribute("veiculos", getVeiculos());
        RequestDispatcher dispatcher = request.getRequestDispatcher("/logado/cliente/formularioProposta.jsp");
        dispatcher.forward(request, response);
    }

    private void insereProposta(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        
        Long id = Long.parseLong(request.getParameter("veiculo"));

        Veiculo veiculo = new VeiculoDAO().getById(id);
        Loja loja = new LojaDAO().getById(id);
        Cliente cliente = (Cliente) request.getSession().getAttribute("usuarioLogado");
        
        String data = new SimpleDateFormat("dd/MM/yyyy").format(Calendar.getInstance().getTime());
        Proposta proposta = new Proposta(data, veiculo.getValor(), veiculo, cliente, loja);
        dao.insert(proposta);
        
        response.sendRedirect("lista");
    }
}