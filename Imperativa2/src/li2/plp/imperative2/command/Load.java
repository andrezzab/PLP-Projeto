package li2.plp.imperative2.command;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

import li2.plp.expressions2.expression.Id;
import li2.plp.expressions2.expression.ValorString;
import li2.plp.expressions2.memory.IdentificadorJaDeclaradoException;
import li2.plp.expressions2.memory.IdentificadorNaoDeclaradoException;
import li2.plp.expressions2.memory.VariavelJaDeclaradaException;
import li2.plp.expressions2.memory.VariavelNaoDeclaradaException;
import li2.plp.imperative1.command.Comando;
import li2.plp.imperative1.memory.AmbienteCompilacaoImperativa;
import li2.plp.imperative1.memory.AmbienteExecucaoImperativa;
import li2.plp.imperative1.memory.EntradaVaziaException;
import li2.plp.imperative1.memory.ErroTipoEntradaException;
import li2.plp.imperative2.memory.AmbienteExecucaoImperativa2;

public class Load implements Comando {

    private String local;
    private String nomeVariavel;
    private ChamadaProcedimento procedimentoParaExecutar;

    public Load(String local, String nomeVariavel, ChamadaProcedimento procedimento) {
        this.local = local;
        this.nomeVariavel = nomeVariavel;
        this.procedimentoParaExecutar = procedimento;
    }

    public Load(String local, String nomeVariavel) {
        this(local, nomeVariavel, null);
    }
    
    public Load(String local) {
        this(local, null, null);
    }

    @Override
    public AmbienteExecucaoImperativa executar(AmbienteExecucaoImperativa amb) // A assinatura continua a mesma por causa da interface Comando
            throws VariavelJaDeclaradaException, VariavelNaoDeclaradaException,
            EntradaVaziaException, ErroTipoEntradaException, IdentificadorJaDeclaradoException,
            IdentificadorNaoDeclaradoException {

        // Fazemos o cast do ambiente genérico para o ambiente específico da Imperativa 2.
        AmbienteExecucaoImperativa2 ambiente = (AmbienteExecucaoImperativa2) amb;

        // A partir daqui usa a variável 'ambiente', que já é do tipo correto.
        try {
            List<String> linhas = Files.readAllLines(Paths.get(local.replace("\"", "")));
            if (!linhas.isEmpty()) {
                ValorString valor = new ValorString(String.join("\n", linhas));
                if (nomeVariavel != null) {
                    Id id = new Id(nomeVariavel);
                    try {
                        // pode usar métodos do ambiente base (changeValor)
                        ambiente.changeValor(id, valor);
                    } catch (VariavelNaoDeclaradaException e) {
                        ambiente.map(id, valor);
                    }
                    System.out.println("Dataset '" + nomeVariavel + "' carregado com sucesso.");
                }
            }
        } catch (IOException e) {
            throw new RuntimeException("Erro ao carregar arquivo: " + local, e);
        }

        if (this.procedimentoParaExecutar != null) {
            // E também pode passar o ambiente para outros objetos que esperam a versão 2
            this.procedimentoParaExecutar.executar(ambiente);
        }
        
        // O retorno continua compatível, pois todo ...2 é um ...1.
        return ambiente;
    }

    @Override
    public boolean checaTipo(AmbienteCompilacaoImperativa amb)
            throws VariavelNaoDeclaradaException, VariavelJaDeclaradaException, IdentificadorNaoDeclaradoException {
        
        boolean procedimentoOk = true;
        if (this.procedimentoParaExecutar != null) {
            procedimentoOk = this.procedimentoParaExecutar.checaTipo(amb);
        }
        return procedimentoOk;
    }
}