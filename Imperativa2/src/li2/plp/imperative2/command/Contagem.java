package li2.plp.imperative2.command;

import li2.plp.expressions2.expression.Id;
import li2.plp.expressions2.expression.Valor;
import li2.plp.expressions2.expression.ValorString;
import li2.plp.expressions2.memory.VariavelNaoDeclaradaException;
import li2.plp.imperative1.command.Comando;
import li2.plp.imperative1.memory.AmbienteCompilacaoImperativa;
import li2.plp.imperative1.memory.AmbienteExecucaoImperativa;
import li2.plp.imperative2.memory.AmbienteExecucaoImperativa2;

public class Contagem implements Comando {

    private Id idVariavelCsv;

    /**
     * Construtor do comando de contagem.
     * @param idVariavelCsv O identificador da variável que armazena o CSV.
     */
    public Contagem(Id idVariavelCsv) {
        this.idVariavelCsv = idVariavelCsv;
    }

    @Override
    public AmbienteExecucaoImperativa executar(AmbienteExecucaoImperativa amb) throws VariavelNaoDeclaradaException {
        AmbienteExecucaoImperativa2 ambiente = (AmbienteExecucaoImperativa2) amb;

        // 1. Obter o conteúdo do CSV a partir da variável no ambiente.
        Valor valorCsv = ambiente.get(idVariavelCsv);
        if (!(valorCsv instanceof ValorString)) {
            throw new RuntimeException("Erro: A variável '" + idVariavelCsv + "' não contém um CSV válido.");
        }

        String conteudoCsv = ((ValorString) valorCsv).valor();
        String[] linhas = conteudoCsv.split("\n");

        // 2. Calcular o número de registros (linhas de dados).
        // Se houver linhas, o número de registros é o total de linhas menos 1 (o cabeçalho).
        int numeroDeRegistros = 0;
        if (linhas.length > 0 && !linhas[0].isEmpty()) {
            numeroDeRegistros = linhas.length - 1;
        }

        // 3. Imprimir o resultado.
        System.out.println("\n--- Contagem de Registros ---");
        System.out.println("Variável: '" + idVariavelCsv + "'");
        System.out.println("Número total de registros (linhas de dados): " + numeroDeRegistros);
        System.out.println("-----------------------------\n");
        
        return ambiente;
    }

    @Override
    public boolean checaTipo(AmbienteCompilacaoImperativa amb) {
        // Para simplificar, retornamos true.
        return true;
    }
}