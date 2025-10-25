package li2.plp.imperative2.command;

import li2.plp.expressions2.expression.Id;
import li2.plp.expressions2.expression.Valor;
import li2.plp.expressions2.expression.ValorInteiro;
import li2.plp.expressions2.memory.VariavelNaoDeclaradaException;
import li2.plp.imperative1.command.Comando;
import li2.plp.imperative1.memory.AmbienteCompilacaoImperativa;
import li2.plp.imperative1.memory.AmbienteExecucaoImperativa;
import li2.plp.imperative2.memory.AmbienteExecucaoImperativa2;

public class Show implements Comando {

    private Id idVariavel;
    private Integer limit;
    private boolean showStats;
    private Id nomeColuna;

    public Show(Id idVariavel, Integer limit) {
        this.idVariavel = idVariavel;
        this.limit = limit;
        this.showStats = false;
    }

    public Show(Id idVariavel, Id nomeColuna) {
        this.idVariavel = idVariavel;
        this.nomeColuna = nomeColuna;
        this.showStats = true;
    }

    @Override
    public AmbienteExecucaoImperativa executar(AmbienteExecucaoImperativa amb) throws VariavelNaoDeclaradaException {
        AmbienteExecucaoImperativa2 ambiente = (AmbienteExecucaoImperativa2) amb;

        Valor valor = ambiente.get(idVariavel);
        String conteudo = valor.toString();
        String[] linhas = conteudo.split("\n");

        if (showStats) {
            mostrarEstatisticas(linhas);
        } else {
            mostrarLinhas(linhas);
        }

        return ambiente;
    }

    private void mostrarLinhas(String[] linhas) {
        int linhasParaMostrar = (limit != null && limit > 0) ? Math.min(limit, linhas.length) : linhas.length;

        System.out.println("=== Mostrando " + linhasParaMostrar + " linha(s) de '" + idVariavel + "' ===");
        for (int i = 0; i < linhasParaMostrar; i++) {
            System.out.println(linhas[i]);
        }
    }

    private void mostrarEstatisticas(String[] linhas) {
        if (linhas.length < 2) {
            System.out.println("Erro: Dados insuficientes para exibir estatísticas.");
            return;
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
            System.out.println("Erro: Coluna '" + nomeColuna + "' não encontrada.");
            return;
        }

        java.util.List<Double> numeros = new java.util.ArrayList<>();
        for (int i = 1; i < linhas.length; i++) {
            String[] celulas = linhas[i].split(",");
            if (celulas.length > indiceColuna) {
                try {
                    numeros.add(Double.parseDouble(celulas[indiceColuna].trim()));
                } catch (NumberFormatException e) {
                }
            }
        }

        if (numeros.isEmpty()) {
            System.out.println("Erro: Nenhum dado numérico encontrado na coluna '" + nomeColuna + "'.");
            return;
        }

        System.out.println("=== Estatísticas de " + idVariavel + "." + nomeColuna + " ===");
        System.out.println("Média: " + CalculadoraEstatisticas.calcularMedia(numeros));
        System.out.println("Mediana: " + CalculadoraEstatisticas.calcularMediana(numeros));
        System.out.println("Mínimo: " + CalculadoraEstatisticas.calcularMinimo(numeros));
        System.out.println("Máximo: " + CalculadoraEstatisticas.calcularMaximo(numeros));
        System.out.println("Desvio Padrão: " + CalculadoraEstatisticas.calcularDesvioPadrao(numeros));
        System.out.println("Variância: " + CalculadoraEstatisticas.calcularVariancia(numeros));
    }

    @Override
    public boolean checaTipo(AmbienteCompilacaoImperativa amb) {
        return true;
    }
}
