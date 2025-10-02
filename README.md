# PLP-Projeto

## Equipe
Athos Pugliese - amps3@cin.ufpe.br

Andrezza Bonfim - amb8@cin.upfe.br

Jordan Kalliure Souza Carvalho - jksc@cin.upfe.br

## Proposta/Objetivo do Projeto
Este projeto tem como objetivo desenvolver uma DSL que permitirá ao usuário carregar, analisar, filtrar e visualizar as características fundamentais de um conjunto de dados de forma rápida e intuitiva.

A DSL será uma extensão da linguagem imperativa 1 do JavaCC, de forma que sua utilização seja intuitiva, permitindo que pessoas não técnicas em dados escrevam scripts em uma linguagem de alto nível e expressiva, sem precisarem conhecer a fundo cada detalhe da API do Pandas.

### Gerenciamento de Dados

Carregar Dados: Ler e interpretar conjuntos de dados a partir de arquivos no formato .csv.

Identificar Tabelas: Atribuir nomes (aliases) aos conjuntos de dados carregados para fácil referência.

### Análise Estatística Univariada

Medidas de Tendência Central:
Calcular a média, mediana e moda de uma coluna numérica.

Medidas de Dispersão:
Calcular o desvio padrão, variância, valor mínimo, valor máximo e a amplitude (diferença entre máximo e mínimo).

Medidas de Posição:
Determinar os quartis (Q1, Q2, Q3) de uma coluna.

### Manipulação de Dados

Contagem: Obter o número total de registros (linhas) em uma tabela.

Filtragem: Criar novos subconjuntos de dados baseados em condições lógicas (ex: idade > 30, curso == "Computação").

### Visualização Baseada em Texto

Tabela de Frequência: Contar e exibir a ocorrência de cada valor em colunas categóricas.

Histograma: Gerar um histograma simples, baseado em caracteres de texto, para visualizar a distribuição de dados numéricos.
