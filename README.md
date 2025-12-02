# PLP-Projeto: Uma DSL para Análise de Dados
Este projeto, desenvolvido para a disciplina de Princípios de Linguagens de Programação (PLP), implementa uma Linguagem de Domínio Específico (DSL) focada em análise de dados.

## 🎯 Proposta e Objetivo
O objetivo principal é criar uma DSL que permita a um usuário carregar, analisar, filtrar e visualizar características fundamentais de um conjunto de dados de forma rápida e intuitiva.

A DSL é uma **extensão da linguagem imperativa 2 do JavaCC**, permitindo que pessoas não técnicas em dados escrevam scripts em uma linguagem de alto nível e expressiva, sem precisarem conhecer a fundo os detalhes de bibliotecas como o Pandas.

## 👩‍💻 Equipe
* **Andrezza Bonfim** - `amb8@cin.upfe.br`
* **Athos Pugliese** - `amps3@cin.ufpe.br`
* **Jordan Carvalho** - `jksc@cin.ufpe.br`

---

## 🚀 Funcionalidades Principais

### 📊 Gerenciamento de Dados
* **Carregar Dados:** Ler e interpretar conjuntos de dados a partir de arquivos `.csv`.
* **Identificar Tabelas:** Atribuir nomes (aliases) aos conjuntos de dados carregados para fácil referência.

### 📈 Análise Estatística Univariada
* **Medidas de Tendência Central:** Calcular `média`, `mediana` e `moda` de uma coluna numérica.
* **Medidas de Dispersão:** Calcular `desvio padrão`, `variância`, valor `mínimo`, valor `máximo` e a `amplitude` (diferença entre máximo e mínimo).
* **Medidas de Posição:** Determinar os `quartis` (Q1, Q2, Q3) de uma coluna.

### 🛠️ Manipulação de Dados
* **Contagem:** Obter o `número total` de registros (linhas) em uma tabela.
* **Filtragem:** Criar novos subconjuntos de dados (`FILTER`) baseados em condições lógicas (ex: `idade > 30`, `curso == "Computação"`).

---

## 💡 Exemplos de Uso
A sintaxe da DSL foi projetada para ser simples e declarativa:

```sql
// --- Comandos de Dados (DSL) ---

// 1. Carregar dados
// Sintaxe: LOAD Expressao AS Id (Expressao pode ser um StringLiteral)
LOAD "Testes/csvs/funcionarios_completo.csv" AS func;
LOAD "Testes/csvs/vendas.csv" AS vendas;

// 2. Análise estatística com atribuição (todos devem ter "AS Id")
// Sintaxe: OpEstatistica ReferenciaColuna AS Id
MEAN func.salario AS media_salarial;
MEDIAN func.salario AS mediana_salarial;
MODE func.anos_experiencia AS experiencia_mais_comum;
STD func.idade AS desvio_idade;
VARIANCE func.salario AS variancia_salario; 
MIN func.salario AS menor_salario;
MAX func.salario AS maior_salario;
RANGE func.idade AS amplitude_idades;
QUARTILES func.salario AS quartis_salario;

// 3. Contagem de registros (Sintaxe: COUNT Id AS Id)
COUNT func AS total_funcionarios;
COUNT vendas AS total_vendas;

// 4. Filtragem de dados para criar novos subconjuntos
// Sintaxe CORRIGIDA: FILTER Id INTO Id WHERE Expressao
FILTER func INTO funcionarios_seniores WHERE idade > 30;
FILTER func INTO func_ti WHERE departamento == "TI";
FILTER vendas INTO vendas_grandes WHERE valor > 1000.5; // Usando ValorDouble

// 5. Análise nos dados filtrados
MEAN funcionarios_seniores.salario AS media_seniores;
COUNT funcionarios_seniores AS total_seniores;

// 6. Visualização e salvamento
// SHOW Expressao (sem LIMIT, conforme a nova BNF)
SHOW func;

// SHOW estatísticas diretas (substitui SHOW STATS)
// Sintaxe: SHOW OpEstatistica ReferenciaColuna
SHOW MEAN func.salario;
SHOW MIN func.idade;

// SAVE Expressao AS Expressao
SAVE funcionarios_seniores AS "seniores.csv";

//“{“ “proc” Id “(“ [ ListaDeclaracaoParametro ] “)” “{“ Comando “}” “;” “call” Id “(“ [ ListaExpressao ] “)” “}”

{
    // DECLARANDO O PROCEDIMENTO
    PROC analisarFuncionarios (STRING arquivo_csv, STRING nome_dataframe) {
        LOAD arquivo_csv AS temp_df;
        MEAN temp_df.salario AS media_salarial;
        MEDIAN temp_df.salario AS mediana_salarial;
        STD temp_df.idade AS desvio_idade;
        FILTER temp_df INTO seniores WHERE idade > 30;
        MEAN seniores.salario AS media_seniores;
        SHOW media_salarial;
        SHOW media_seniores
    };

    // CHAMANDO NO MESMO BLOCO (2 vezes)
    CALL analisarFuncionarios("Testes/csvs/funcionarios_completo.csv", "func");
    CALL analisarFuncionarios("Testes/csvs/funcionarios_completo.csv", "func2")
}
```
## BNF atualizada:
```sql
Programa ::= Comando

Comando ::= Atribuicao
          | ComandoDeclaracao
          | While
          | IfThenElse
          | IO
          | Comando ";" Comando
          | Skip
          | ChamadaProcedimento
          | ComandoEstatistico    // ---> ADICIONADO

Skip ::=

Atribuicao ::= Id ":=" Expressao

Expressao ::= Valor
            | ExpUnaria 
            | ExpBinaria 
            | Id

Valor ::= ValorConcreto

ValorConcreto ::= ValorInteiro
                | ValorBooleano
                | ValorString
                | ValorDouble           // ---> ADICIONADO
                | ValorDataFrame           // ---> ADICIONADO

ExpUnaria ::= "-" Expressao
            | "not" Expressao
            | "length" Expressao

ExpBinaria ::= Expressao "+" Expressao
             | Expressao "-" Expressao
             | Expressao "and" Expressao
             | Expressao "or" Expressao
             | Expressao "==" Expressao
             | Expressao "++" Expressao

ComandoDeclaracao ::= "{" Declaracao ";" Comando "}"

Declaracao ::= DeclaracaoVariavel
             | DeclaracaoProcedimento
             | DeclaracaoComposta

DeclaracaoVariavel ::= "var" Id "=" Expressao

DeclaracaoComposta ::= Declaracao "," Declaracao

DeclaracaoProcedimento ::= "proc" Id "(" [ ListaDeclaracaoParametro ] ")" "{" Comando "}"

ListaDeclaracaoParametro ::= Tipo Id
                           | Tipo Id "," ListaDeclaracaoParametro

Tipo ::= "string" | "int" | "boolean" | "double" | TipoDataFrame // ---> ADICIONADO double

While ::= "while" Expressao "do" Comando

IfThenElse ::= "if" Expressao "then" Comando "else" Comando

IO ::= "write" "(" Expressao ")"
     | "read" "(" Id ")"

ChamadaProcedimento ::= "call" Id "(" [ ListaExpressao ] ")"

ListaExpressao ::= Expressao | Expressao, ListaExpressao

// --- SEÇÃO DA DSL DE DADOS ---

ComandoEstatistico ::= ComandoLoad        
| ComandoFiltro        
| ComandoCalculo
| ComandoShow
| ComandoSave

ComandoLoad ::= "LOAD" StringLiteral "AS" Id

ComandoFiltro ::= "FILTER" Id INTO Id "WHERE" Expressao

ComandoCalculo ::= AnaliseColuna | ContagemTabela

Comando Show ::= "SHOW" Expressao    | "SHOW" OpEstatistica ReferenciaColuna

ComandoSave ::= "SAVE" Expressao "AS" StringLiteral

AnaliseColuna ::= OpEstatistica ReferenciaColuna "AS" Id

ContagemTabela ::= "COUNT" Expressao "AS" Id

ReferenciaColuna ::= Expressao "." Id

OpEstatistica ::= "MEAN" | "MEDIAN" | "MODE" | "STD" | “VARIANCE” | "MIN" | "MAX" | "RANGE" | "QUARTILES"

// --- Definições Auxiliares ---

StringLiteral ::= "\"" [^\"]* "\"" 
                | "'" [^']* "'"

Id ::= [a-zA-Z_][a-zA-Z0-9_]*
