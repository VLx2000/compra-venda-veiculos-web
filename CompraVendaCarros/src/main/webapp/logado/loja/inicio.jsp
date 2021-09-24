<%@ page contentType="text/html" pageEncoding="UTF-8"%>
<%@ page isELIgnored="false"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <title>Compra e Venda de Veículos</title>
        <link rel="stylesheet" href="css/global.css">
    </head>
    <body>
        <%String contextPath = request.getContextPath().replace("/", ""); %>
        <div class="topbar">
            <ul id="links">
                <li class="linkTopoEsquerda">
                    <a href="${pageContext.request.contextPath}/lojas">
                        <span id="titulo">LataVelhaInc. </span><span id="corporativo">Corporativo</span>
                    </a>
                </li>
                <li class="nomeTopoDireita">
                    <span>Loja: ${sessionScope.lojaLogada.nome}</span>
                        <ul class="dropdown">
                            <li><a href="logout">Sair</a></li>
                        </ul>
                </li>
                <li class="linkTopoDireita">
                    <a href="proposta/listarPropostasLoja">Propostas</a>
                </li>
                <li class="linkTopoDireita">
                    <a href="lojas/adicionarVeiculo">Adicionar Veículo</a>
                </li>
            </ul>
        </div>
        <section id="carros">
            <div id="divFiltro">
                <input type="text" name="Filtro" placeholder="filtrar por modelo..." id="filtro">
                <p id="numero"></p>
            </div>
            <c:forEach var="veiculo" items="${requestScope.catalogo}">
                <section class="box" onclick="Clicavel('${veiculo.placa}')">
                    <div class="card">
                        <div class="container">
                            <img src="imagens/${veiculo.modelo}.jpg" alt="Carro">
                        </div>
                        <ul class="dadosCarro">
                            <li class="modelo">${veiculo.modelo}</li>
                            <li>Loja: ${veiculo.loja.nome}</li>
                            <li>${veiculo.quilometragem}km</li>
                            <li class="oferta">$${veiculo.valor}</li>                    
                        </ul>
                    </div>
                </section>
            </c:forEach>
        </section>
        <script src="js/script.js"></script>
    </body>
</html>