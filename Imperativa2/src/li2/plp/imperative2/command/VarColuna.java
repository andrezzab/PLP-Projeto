package li2.plp.imperative2.command;

import li2.plp.expressions2.expression.Id;

/**
 * Classe auxiliar (container) para carregar o resultado do parsing de
 * uma expressao no formato 'variavel.coluna'.
 * Uma regra do parser só pode retornar um objeto, então esta classe
 * serve para "empacotar" os dois Ids (variavel e coluna) juntos.
 */
public class VarColuna {

    public final Id var;
    public final Id col;

    public VarColuna(Id variavel, Id coluna) {
        this.var = variavel;
        this.col = coluna;
    }
}