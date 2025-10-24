package li2.plp.imperative2.command;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import li2.plp.expressions2.expression.Id;
import li2.plp.expressions2.expression.Valor;
import li2.plp.expressions2.memory.VariavelNaoDeclaradaException;
import li2.plp.imperative1.command.Comando;
import li2.plp.imperative1.memory.AmbienteCompilacaoImperativa;
import li2.plp.imperative1.memory.AmbienteExecucaoImperativa;
import li2.plp.imperative2.memory.AmbienteExecucaoImperativa2;

public class Save implements Comando {

    private Id idVariavel;
    private String caminhoArquivo;

    public Save(Id idVariavel, String caminhoArquivo) {
        this.idVariavel = idVariavel;
        this.caminhoArquivo = caminhoArquivo;
    }

    @Override
    public AmbienteExecucaoImperativa executar(AmbienteExecucaoImperativa amb) throws VariavelNaoDeclaradaException {
        AmbienteExecucaoImperativa2 ambiente = (AmbienteExecucaoImperativa2) amb;

        Valor valor = ambiente.get(idVariavel);
        String conteudo = valor.toString();

        try {
            Files.write(Paths.get(caminhoArquivo), conteudo.getBytes());
            System.out.println("Dataset '" + idVariavel + "' salvo em '" + caminhoArquivo + "'.");
        } catch (IOException e) {
            throw new RuntimeException("Erro ao salvar arquivo: " + caminhoArquivo, e);
        }

        return ambiente;
    }

    @Override
    public boolean checaTipo(AmbienteCompilacaoImperativa amb) {
        return true;
    }
}
