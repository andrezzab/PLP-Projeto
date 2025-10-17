package li2.plp.imperative2.command;

import java.util.ArrayList;
import java.util.List;

import li2.plp.expressions2.expression.Id;
import li2.plp.expressions2.expression.Valor;
import li2.plp.expressions2.expression.ValorString;
import li2.plp.expressions2.memory.VariavelNaoDeclaradaException;
import li2.plp.imperative1.command.Comando;
import li2.plp.imperative1.memory.AmbienteCompilacaoImperativa;
import li2.plp.imperative1.memory.AmbienteExecucaoImperativa;
import li2.plp.imperative2.memory.AmbienteExecucaoImperativa2;

public abstract class ComandoEstatisticoAbstrato implements Comando {

    protected Id idVariavelCsv;
    protected Id nomeColuna;
    protected Id idVariavelDestino;

    public ComandoEstatisticoAbstrato(Id idVariavelCsv, Id nomeColuna, Id idVariavelDestino) {
        this.idVariavelCsv = idVariavelCsv;
        this.nomeColuna = nomeColuna;
        this.idVariavelDestino = idVariavelDestino;
    }

    // Método 'final' para que as subclasses não possam alterá-lo.
    @Override
    public final AmbienteExecucaoImperativa executar(AmbienteExecucaoImperativa amb) throws VariavelNaoDeclaradaException {
        AmbienteExecucaoImperativa2 ambiente = (AmbienteExecucaoImperativa2) amb;

        Valor valorCsv = ambiente.get(idVariavelCsv);
        if (!(valorCsv instanceof ValorString)) {
            throw new RuntimeException("Erro: A variável '" + idVariavelCsv + "' não é um CSV.");
        }
        String[] linhas = ((ValorString) valorCsv).valor().split("\n");
        if (linhas.length < 2) {
             throw new RuntimeException("Erro: CSV '" + idVariavelCsv + "' não contém dados.");
        }

        String[] cabecalho = linhas[0].split(",");
        int indiceColuna = -1;
        for (int i = 0; i < cabecalho.length; i++) {
            if (cabecalho[i].trim().equalsIgnoreCase(nomeColuna.toString())) {
                indiceColuna = i;
                break;
            }
        }

        if (indiceColuna == -1) {
            throw new RuntimeException("Erro: Coluna '" + nomeColuna + "' não encontrada no CSV.");
        }

        List<Double> numeros = new ArrayList<>();
        for (int i = 1; i < linhas.length; i++) {
            String[] celulas = linhas[i].split(",");
            if (celulas.length > indiceColuna) {
                try {
                    numeros.add(Double.parseDouble(celulas[indiceColuna].trim()));
                } catch (NumberFormatException e) {
                    // Ignora silenciosamente valores não numéricos.
                }
            }
        }
        
        // Chama o método abstrato que será implementado por cada comando concreto
        Valor resultado = calcular(numeros);

        // Salva o resultado na variável de destino
        ambiente.map(idVariavelDestino, resultado);
        System.out.println("Resultado salvo na variável '" + idVariavelDestino + "'.");
        System.out.println(">> " + this.getClass().getSimpleName() + " de " + nomeColuna + ": " + resultado.toString());

        return ambiente;
    }

    /**
     * Este é o único método que as subclasses (Mean, Median, etc.) precisarão implementar.
     * @param numeros A lista de dados numéricos da coluna.
     * @return O resultado do cálculo como um objeto 'Valor'.
     */
    protected abstract Valor calcular(List<Double> numeros);

    @Override
    public boolean checaTipo(AmbienteCompilacaoImperativa amb) {
        // Para simplificar, assumimos que está bem tipado.
        return true;
    }
}