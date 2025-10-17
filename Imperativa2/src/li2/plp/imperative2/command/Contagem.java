package li2.plp.imperative2.command;

import li2.plp.expressions2.expression.Id;
import li2.plp.expressions2.memory.VariavelNaoDeclaradaException;
import li2.plp.imperative1.memory.AmbienteCompilacaoImperativa;
import li2.plp.imperative1.memory.AmbienteExecucaoImperativa;
import li2.plp.imperative2.memory.AmbienteExecucaoImperativa2;

public class Contagem extends AnaliseLinhas {

    public Contagem(Id idVariavelCsv) {
        // Chama o construtor da classe mãe
        super(idVariavelCsv);
    }

    @Override
    public AmbienteExecucaoImperativa executar(AmbienteExecucaoImperativa amb) throws VariavelNaoDeclaradaException {
        AmbienteExecucaoImperativa2 ambiente = (AmbienteExecucaoImperativa2) amb;

        // Usa o método herdado para obter as linhas
        String[] linhas = getLinhasDoCsv(ambiente);

        int numeroDeRegistros = 0;
        if (linhas.length > 0 && !linhas[0].isEmpty()) {
            numeroDeRegistros = linhas.length - 1;
        }

        System.out.println("\n--- Contagem de Registros ---");
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