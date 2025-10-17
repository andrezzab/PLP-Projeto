package li2.plp.expressions2.expression;

import li2.plp.expressions1.util.Tipo;
import li2.plp.expressions1.util.TipoPrimitivo;
import li2.plp.expressions2.memory.AmbienteCompilacao;

/**
 * Objetos desta classe encapsulam valor double.
 */
public class ValorDouble extends ValorConcreto<Double> {

	/**
	 * Cria <code>ValorInteiro</code> contendo o valor fornecido.
	 */
	public ValorDouble(Double valor) {
		super(valor);
	}

	/**
	 * Retorna os tipos possiveis desta expressao.
	 * 
	 * @param amb
	 *            o ambiente de compila��o.
	 * @return os tipos possiveis desta expressao.
	 */
	public Tipo getTipo(AmbienteCompilacao amb) {
		return TipoPrimitivo.DOUBLE;
	}

	public ValorDouble clone(){
		return new ValorDouble(this.valor());
	}
}
