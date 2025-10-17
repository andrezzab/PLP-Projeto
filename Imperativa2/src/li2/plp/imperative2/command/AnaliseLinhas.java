package li2.plp.imperative2.command;

import li2.plp.expressions2.expression.Id;
import li2.plp.expressions2.expression.Valor;
import li2.plp.expressions2.expression.ValorString;
import li2.plp.expressions2.memory.VariavelNaoDeclaradaException;
import li2.plp.imperative1.command.Comando;
import li2.plp.imperative2.memory.AmbienteExecucaoImperativa2;

public abstract class AnaliseLinhas implements Comando {

    protected Id idVariavelCsv;

    public AnaliseLinhas(Id idVariavelCsv) {
        this.idVariavelCsv = idVariavelCsv;
    }

    protected final String[] getLinhasDoCsv(AmbienteExecucaoImperativa2 ambiente) throws VariavelNaoDeclaradaException {
        Valor valorCsv = ambiente.get(idVariavelCsv);
        if (!(valorCsv instanceof ValorString)) {
            throw new RuntimeException("Erro: A variável '" + idVariavelCsv + "' não é um CSV.");
        }
        String conteudoCsv = ((ValorString) valorCsv).valor();
        return conteudoCsv.split("\n");
    }
}