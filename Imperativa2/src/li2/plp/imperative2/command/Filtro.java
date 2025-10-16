package li2.plp.imperative2.command;

import java.util.ArrayList;
import java.util.List;
import li2.plp.expressions2.expression.Expressao;
import li2.plp.expressions2.expression.Id;
import li2.plp.expressions2.expression.Valor;
import li2.plp.expressions2.expression.ValorBooleano;
import li2.plp.expressions2.expression.ValorInteiro;
import li2.plp.expressions2.expression.ValorString;
import li2.plp.expressions2.memory.VariavelNaoDeclaradaException;
import li2.plp.expressions2.memory.IdentificadorJaDeclaradoException;
import li2.plp.expressions2.memory.IdentificadorNaoDeclaradoException;
import li2.plp.imperative1.memory.EntradaVaziaException;
import li2.plp.imperative1.memory.ErroTipoEntradaException;
import li2.plp.imperative1.command.Comando;
import li2.plp.imperative1.memory.AmbienteCompilacaoImperativa;
import li2.plp.imperative1.memory.AmbienteExecucaoImperativa;
import li2.plp.imperative2.memory.AmbienteExecucaoImperativa2;

public class Filtro implements Comando {

    private Id idCsvOrigem;
    private Id idCsvDestino;
    private Expressao condicao;

    public Filtro(Id idCsvOrigem, Id idCsvDestino, Expressao condicao) {
        this.idCsvOrigem = idCsvOrigem;
        this.idCsvDestino = idCsvDestino;
        this.condicao = condicao;
    }

    @Override
    public AmbienteExecucaoImperativa executar(AmbienteExecucaoImperativa amb)
        throws VariavelNaoDeclaradaException, IdentificadorJaDeclaradoException,
        IdentificadorNaoDeclaradoException, EntradaVaziaException, ErroTipoEntradaException {
        AmbienteExecucaoImperativa2 ambiente = (AmbienteExecucaoImperativa2) amb;

        // 1. Obter os dados do CSV de origem.
        Valor valorCsv = ambiente.get(idCsvOrigem);
        if (!(valorCsv instanceof ValorString)) {
            throw new RuntimeException("Erro: A variável '" + idCsvOrigem + "' não é um CSV.");
        }
        String[] linhas = ((ValorString) valorCsv).valor().split("\n");
        if (linhas.length < 2) {
            System.out.println("Aviso: CSV de origem '" + idCsvOrigem + "' está vazio ou não tem dados.");
            return ambiente;
        }

        // 2. Preparar o novo CSV e encontrar os nomes das colunas.
        String cabecalho = linhas[0];
        String[] nomesColunas = cabecalho.split(",");
        List<String> linhasFiltradas = new ArrayList<>();
        linhasFiltradas.add(cabecalho); // O cabeçalho é sempre mantido.

        // 3. Iterar sobre cada linha de dados para avaliar a condição.
        for (int i = 1; i < linhas.length; i++) {
            String[] celulas = linhas[i].split(",");
            
            // Cria um novo escopo temporário para a linha atual.
            ambiente.incrementa(); 

            // Mapeia cada coluna como uma variável local neste escopo.
            for (int j = 0; j < nomesColunas.length; j++) {
                if (j < celulas.length) {
                    String valorCelula = celulas[j].trim();
                    Id idColuna = new Id(nomesColunas[j].trim());
                    
                    // Tenta converter para Inteiro, senão, mantém como String.
                    try {
                        ambiente.map(idColuna, new ValorInteiro(Integer.parseInt(valorCelula)));
                    } catch (NumberFormatException e) {
                        ambiente.map(idColuna, new ValorString(valorCelula));
                    }
                }
            }

            // AVALIA a expressão de filtro no contexto desta linha.
            Valor resultado = condicao.avaliar(ambiente);

            // Se a condição for verdadeira, adiciona a linha ao resultado.
            if (resultado instanceof ValorBooleano && ((ValorBooleano) resultado).valor()) {
                linhasFiltradas.add(linhas[i]);
            }

            // Descarta o escopo temporário da linha.
            ambiente.restaura();
        }

        // 4. Salvar o novo CSV filtrado na variável de destino.
        String novoConteudoCsv = String.join("\n", linhasFiltradas);
        ambiente.map(idCsvDestino, new ValorString(novoConteudoCsv));
        
        System.out.println("\n--- Filtro Aplicado ---");
        System.out.println("Dados de '" + idCsvOrigem + "' filtrados para '" + idCsvDestino + "'.");
        System.out.println((linhasFiltradas.size() - 1) + " registros correspondem à condição.");
        System.out.println("-----------------------\n");

        return ambiente;
    }

    @Override
    public boolean checaTipo(AmbienteCompilacaoImperativa amb)
        throws IdentificadorJaDeclaradoException, IdentificadorNaoDeclaradoException,
        EntradaVaziaException {
        // Uma checagem de tipo ideal verificaria se a expressão de condição resulta em um booleano.
        return true;
    }
}