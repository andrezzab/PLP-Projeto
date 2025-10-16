package li2.plp.imperative2.command;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import li2.plp.expressions2.expression.Id;
import li2.plp.expressions2.expression.Valor;
import li2.plp.expressions2.expression.ValorString;
import li2.plp.expressions2.memory.VariavelNaoDeclaradaException;
import li2.plp.imperative1.command.Comando;
import li2.plp.imperative1.memory.AmbienteCompilacaoImperativa;
import li2.plp.imperative1.memory.AmbienteExecucaoImperativa;
import li2.plp.imperative2.memory.AmbienteExecucaoImperativa2;

public class Estatisticas implements Comando {

    private Id idVariavelCsv;
    private ValorString nomeColuna;

    public Estatisticas(Id idVariavelCsv, ValorString nomeColuna) {
        this.idVariavelCsv = idVariavelCsv;
        this.nomeColuna = nomeColuna;
    }

    @Override
    public AmbienteExecucaoImperativa executar(AmbienteExecucaoImperativa amb) throws VariavelNaoDeclaradaException {
        AmbienteExecucaoImperativa2 ambiente = (AmbienteExecucaoImperativa2) amb;

        Valor valorCsv = ambiente.get(idVariavelCsv);
        if (!(valorCsv instanceof ValorString)) {
            throw new RuntimeException("Erro: A variável '" + idVariavelCsv + "' não contém um CSV válido.");
        }

        String conteudoCsv = ((ValorString) valorCsv).valor();
        String[] linhas = conteudoCsv.split("\n");

        if (linhas.length < 2) {
            System.out.println("O CSV não tem dados suficientes para cálculos.");
            return ambiente;
        }

        String[] cabecalho = linhas[0].split(",");
        int indiceColuna = -1;
        for (int i = 0; i < cabecalho.length; i++) {
            if (cabecalho[i].trim().equalsIgnoreCase(nomeColuna.valor())) {
                indiceColuna = i;
                break;
            }
        }

        if (indiceColuna == -1) {
            System.out.println("Erro: Coluna '" + nomeColuna.valor() + "' não encontrada no CSV.");
            return ambiente;
        }

        List<Double> numeros = new ArrayList<>();
        for (int i = 1; i < linhas.length; i++) {
            String[] celulas = linhas[i].split(",");
            if (celulas.length > indiceColuna) {
                try {
                    numeros.add(Double.parseDouble(celulas[indiceColuna].trim()));
                } catch (NumberFormatException e) {
                    System.out.println("Aviso: Valor não numérico '" + celulas[indiceColuna] + "' ignorado na linha " + (i + 1));
                }
            }
        }

        if (numeros.isEmpty()) {
            System.out.println("Nenhum dado numérico válido encontrado na coluna '" + nomeColuna.valor() + "'.");
            return ambiente;
        }
        
        // Ordena a lista de números uma vez, pois é necessário para mediana e quartis
        Collections.sort(numeros);

        // --- Início dos Cálculos e Impressão ---
        System.out.println("\n--- Estatísticas para a coluna '" + nomeColuna.valor() + "' ---");
        
        // Medidas de Tendência Central
        double media = calcularMedia(numeros);
        double q2_mediana = calcularMediana(numeros); // Q2 é a mediana
        System.out.println("\n  -- Medidas de Tendência Central --");
        System.out.printf("  Média: %.2f\n", media);
        System.out.printf("  Mediana (Q2): %.2f\n", q2_mediana);
        System.out.println("  Moda: " + calcularModa(numeros));

        // Medidas de Dispersão
        double minimo = numeros.get(0); // Lista já está ordenada
        double maximo = numeros.get(numeros.size() - 1); // Lista já está ordenada
        double variancia = calcularVariancia(numeros, media);
        double desvioPadrao = Math.sqrt(variancia);
        
        System.out.println("\n  -- Medidas de Dispersão --");
        System.out.printf("  Valor Mínimo: %.2f\n", minimo);
        System.out.printf("  Valor Máximo: %.2f\n", maximo);
        System.out.printf("  Amplitude: %.2f\n", maximo - minimo);
        System.out.printf("  Variância: %.2f\n", variancia);
        System.out.printf("  Desvio Padrão: %.2f\n", desvioPadrao);

        // NOVO: Medidas de Posição (Quartis)
        int meio = numeros.size() / 2;
        // Q1 é a mediana da primeira metade dos dados
        double q1 = calcularMediana(numeros.subList(0, meio));
        // Q3 é a mediana da segunda metade dos dados
        // Se o tamanho for ímpar, a segunda metade começa do meio, senão, começa do meio.
        double q3 = calcularMediana(numeros.subList(numeros.size() % 2 == 0 ? meio : meio + 1, numeros.size()));
        
        System.out.println("\n  -- Medidas de Posição --");
        System.out.printf("  Primeiro Quartil (Q1): %.2f\n", q1);
        System.out.printf("  Segundo Quartil (Q2): %.2f\n", q2_mediana);
        System.out.printf("  Terceiro Quartil (Q3): %.2f\n", q3);

        System.out.println("----------------------------------------\n");

        return ambiente;
    }

    // --- Métodos de Cálculo ---
    private double calcularMedia(List<Double> numeros) {
        double soma = 0.0;
        for (double num : numeros) {
            soma += num;
        }
        return soma / numeros.size();
    }

    private double calcularMediana(List<Double> numeros) {
        if (numeros.isEmpty()) return 0.0;
        // A lista já deve vir ordenada, mas por segurança, podemos criar uma cópia e ordenar
        List<Double> sortedList = new ArrayList<>(numeros);
        Collections.sort(sortedList);
        int meio = sortedList.size() / 2;
        if (sortedList.size() % 2 == 1) {
            return sortedList.get(meio);
        } else {
            return (sortedList.get(meio - 1) + sortedList.get(meio)) / 2.0;
        }
    }
    
    private String calcularModa(List<Double> numeros) {
        Map<Double, Integer> contagem = new HashMap<>();
        for (double num : numeros) {
            contagem.put(num, contagem.getOrDefault(num, 0) + 1);
        }
        int maxContagem = 0;
        for (int count : contagem.values()) {
            if (count > maxContagem) {
                maxContagem = count;
            }
        }
        if (maxContagem <= 1 && numeros.size() > 1) {
            return "Não há moda (valores não se repetem).";
        }
        List<Double> modas = new ArrayList<>();
        for (Map.Entry<Double, Integer> entry : contagem.entrySet()) {
            if (entry.getValue() == maxContagem) {
                modas.add(entry.getKey());
            }
        }
        return modas.toString();
    }

    private double calcularVariancia(List<Double> numeros, double media) {
        if (numeros.size() < 2) return 0.0;
        double somaDosQuadrados = 0.0;
        for (double num : numeros) {
            somaDosQuadrados += Math.pow(num - media, 2);
        }
        return somaDosQuadrados / (numeros.size() - 1);
    }
    
    @Override
    public boolean checaTipo(AmbienteCompilacaoImperativa amb) {
        return true;
    }
}