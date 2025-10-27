# Sistema de Contas a Pagar e Receber

Este projeto para controlar contas a pagar e receber do usuario que as criou.  

---

## Funcionalidades
- Criar usuário.
- Fazer login para se autenticar.
- Atualizar Usuario.
- Deletar o proprio usuario.
- Criar contas.
- Atualizar contas.
- Deletar contas.
- Exibição de todas as contas com valor total de contas a receber e a pagar com valor de diferença entre os dois valores.
- Exibição de todas contas a receber ou a pagar com valor total.
- Exibição de contas a receber ou a pagar com valor total que estão pendentes.

---

## Tecnologias Utilizadas
- Java
- Spring Boot
- PostgreSQL
- Docker

---

## Como rodar o projeto
- Clone o projeto do GitHub https://github.com/victorsena19/sistema_contas.git
- Start seu docker
- Abra o projeto em alguma IDE e vá no terminal
- Digite o comando docker-compose up --build

## Rotas da Aplicação
- Rota padrão para acessar localmente url = http://localhost:8080

# Account
- Na rota url/account se trata das contas que o usuario cadastrou.
- No GET url/account/all mostra todas as contas que o usuario registrou somando o valor total das contas a pagar e das contas a receber, mostrando a diferença entre os valores, ou seja se o usuario tem mais gastos do que recebimento o valor da diferença ficará negativo e o contrario positivo.
- Rota GET url/account/pending, mostra as contas pendentes.
- Rota GET url/account que recebe um @RequestParam AccountType type, mostra as contas pelo tipo da conta, se é a RECEBER = RECEIVE ou a PAGAR = PAY.
- As demais Rotas com as Anotações POST para registrar uma nova conta, PUT para editar uma conta existente e DELETE para deletar uma conta.

# User 
- Nas rotas do usuario url/user, tem as rotas para criar, atualizar e deletar, sendo que só consegue atualizar ou deletar o proprio usuario.

# Login
- Rota para fazer url/login.

Tem uma classe que dentro do Controller que serve para capturar as exceções e tratar elas e retornar os httpStatus e as mensagens mais adequadas. 
