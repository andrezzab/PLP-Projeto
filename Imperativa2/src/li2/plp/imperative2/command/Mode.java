package li2.plp.imperative2.command;

import java.util.List;
import li2.plp.expressions2.expression.Id;
import li2.plp.expressions2.expression.Valor;
import li2.plp.expressions2.expression.ValorString;

public class Mode extends ComandoEstatisticoAbstrato {

    public Mode(Id idVariavelCsv, Id nomeColuna, Id idVariavelDestino) {
        super(idVariavelCsv, nomeColuna, idVariavelDestino);
    }

    @Override
    protected Valor calcular(List<Double> numeros) {
        List<Double> modas = CalculadoraEstatisticas.calcularModa(numeros);
        return new ValorString(modas.toString());
    }

    @Override
    protected String getNomeEstatistica() {
        return "Moda";
    }
}
