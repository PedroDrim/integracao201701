/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.ufg.inf.horus.implementation.service;

import br.ufg.inf.horus.implementation.controller.XmlParser;
import br.ufg.inf.horus.implementation.model.Barramento;
import br.ufg.inf.horus.implementation.model.Connection;
import br.ufg.inf.horus.util.model.Log;
import br.ufg.inf.horus.util.validations.BsusException;
import br.ufg.inf.horus.util.validations.BsusValidator;

/**
 * Classe responsável por implementar e requerir uma execução remota além de
 * receber uma resposta XML, convertendo-a.
 *
 * @see Barramento
 * @author Vinicius
 */
public class Bsus implements Barramento {

    /**
     * Objeto responsável por realizar a conexão com os serviços.
     *
     * @see Connection
     */
    private Connection connection;

    /**
     * Objeto resposável por exibir Log's.
     *
     * @see Log
     */
    private Log log;

    /**
     * Define uma Connection.
     *
     * @see Bsus
     * @param connection Connection a ser definida.
     */
    @Override
    public void setConnection(Connection connection) {
        BsusValidator.verifyNull(connection, "connection", log);
        this.connection = connection;
    }

    /**
     * Define uma estrutura para exibição de Log's.
     *
     * @see Bsus
     * @param log Estrutura de log a ser definida.
     */
    @Override
    public void setLog(Log log) {
        this.log = log;
    }

    /**
     * Obtem as posições nos estoques pelo número de cnes.
     *
     * @see Bsus
     * @param cnes Número de cnes.
     * @return A posição no estoque do referido produto.
     */
    @Override
    public String obterEstoquePorCNES(int cnes) {
        verificaCnes(cnes);
        String xml = connection.consultarPosicaoEstoquePorCNES(cnes);
        XmlParser parser = new XmlParser(log);
        String retorno = parser.getMessage(xml);
        return retorno;

    }

    /**
     * Obtem as posições nos estoques pelo número de cnes e princípio ativo.
     *
     * @see Bsus
     * @param cnes Número de cnes.
     * @param principio Tipo de princípio ativo.
     * @return A posição do estoque do referido produto.
     */
    @Override
    public String obterEstoquePorCNESEPrincipio(int cnes, String principio) {
        verificaCnes(cnes);
        verificaPrincipio(principio);
        String xml
                = connection.consultarPosicaoEstoquePorCNESPrincipioAtivo(
                        cnes, principio);
        XmlParser parser = new XmlParser(log);
        String retorno = parser.getMessage(xml);
        return retorno;

    }

    /**
     * Obtem as posições nos estoques pelo número de cnes, princípio ativo e
     * dados de paginação.
     *
     * @see Bsus
     * @param cnes Número de cnes.
     * @param principio Tipo de princípio ativo.
     * @param posicaoInicio Posição inicial.
     * @param qtdRegistrosPagina Quantidade de registros por página.
     * @param qtdRegistros Quantidade de registros ao todo.
     * @return A posição no estoque do referido produto.
     */
    @Override
    public String obterEstoquePorCNESEPrincipioPaginado(int cnes,
            String principio, int posicaoInicio, int qtdRegistrosPagina,
            int qtdRegistros) {

        verificaCnes(cnes);
        verificaPrincipio(principio);
        verificaPaginado(posicaoInicio, qtdRegistrosPagina, qtdRegistros);
        String xml
                = connection.consultarPosicaoEstoquePorCNESPrincipioAtivoPaginado(
                        cnes, principio, posicaoInicio, qtdRegistrosPagina, qtdRegistros
                );
        XmlParser parser = new XmlParser(log);
        String retorno = parser.getMessage(xml);
        return retorno;

    }

    /**
     * Obtem as posições nos estoques, bem como os dados do produto referentes
     * ao número de cnes.
     *
     * @see Bsus
     * @param cnes Número de cnes.
     * @return A posição no estoque e os dados do referido produto.
     */
    @Override
    public String obterDadosEEstoquePorCNES(int cnes) {
        verificaCnes(cnes);
        String xml = connection.consultarProdutoPorCNESDispensacao(cnes);
        XmlParser parser = new XmlParser(log);
        String retorno = parser.getMessage(xml);
        return retorno;

    }

    /**
     * Obtem as posições nos estoques, bem como os dados do produto referentes
     * ao número de cnes e dados de paginação.
     *
     * @see Bsus
     * @param cnes Número de cnes.
     * @param posicaoInicio Posição inicial.
     * @param qtdRegistrosPagina Quantidade de registros por página.
     * @param qtdRegistros Quantidade de registros ao todo.
     * @return A posição no estoque e os dados do referido produto.
     */
    @Override
    public String obterDadosEEstoquePorCNESPaginado(int cnes, int posicaoInicio,
            int qtdRegistrosPagina, int qtdRegistros) {
        verificaCnes(cnes);
        verificaPaginado(posicaoInicio,
                qtdRegistrosPagina, qtdRegistros);
        String xml
                = connection.consultarProdutoPorCNESDispensacaoPaginado(
                        cnes, posicaoInicio, qtdRegistrosPagina, qtdRegistros
                );
        XmlParser parser = new XmlParser(log);
        String retorno = parser.getMessage(xml);
        return retorno;

    }

    /**
     * Método para verificar se parâmetro informado é válido.
     *
     * @param cnes Número Cnes para verificação
     * @return cnes Se válido, 0 se inválido
     */
    public int verificaCnes(int cnes) {
        if (cnes > 1000000 && cnes <= 9999999) {
            return cnes;
        } else {
            String message = "Número CNES " + cnes + " inválido. CNES deve ter"
                    + " 7 dígitos e conter somente números.";
            log.erro(message);
            throw new BsusException(message);
        }
    }

    /**
     * Método para verificação do parâmetro princípio ativo.
     *
     * @param principio String de principio ativo para verificação
     * @return principio se válido, null se inválido.
     */
    public String verificaPrincipio(String principio) {
        if (!principio.isEmpty() && principio != null) {
            return principio;
        } else {
            String message = "Principio Ativo " + principio + " é inválido.";
            log.erro(message);
            throw new BsusException(message);
        }
    }

    /**
     * Método para verificação dos parâmetros paginados.
     *
     * @param posicaoInicio Parametro para verificação.
     * @param qtdRegistrosPagina Parâmetro para verificação.
     * @param qtdRegistros Parâmetro para verificação.
     * @return true se válidos, false se inválidos.
     */
    public boolean verificaPaginado(int posicaoInicio, int qtdRegistrosPagina,
            int qtdRegistros) {
        if (posicaoInicio >= 0 && qtdRegistrosPagina >= 0 && qtdRegistros >= 0) {
            return true;
        } else {
            String message = "Parâmentros para paginação informados"
                    + " não são válidos.";
            log.erro(message);
            throw new BsusException(message);
        }
    }
}
