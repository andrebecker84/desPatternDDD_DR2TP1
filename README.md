<div align="center">

[![Instituto Infnet](https://img.shields.io/badge/Instituto-Infnet-red?style=for-the-badge)](https://www.infnet.edu.br)
[![Curso](https://img.shields.io/badge/Curso-Engenharia_de_Software-blue?style=for-the-badge)](https://www.infnet.edu.br)
[![Disciplina](https://img.shields.io/badge/Disciplina-Design_Patterns_%26_DDD_(DR2)-green?style=for-the-badge)](https://www.infnet.edu.br)

[![Java](https://img.shields.io/badge/Java-25-orange?logo=openjdk)](https://openjdk.org)
[![Spring Boot](https://img.shields.io/badge/Spring_Boot-4.0.6-6DB33F?logo=springboot&logoColor=white)](https://spring.io/projects/spring-boot)
[![Gradle](https://img.shields.io/badge/Gradle-9.5.0-02303A?logo=gradle&logoColor=white)](https://gradle.org)
[![JUnit](https://img.shields.io/badge/JUnit-5-green?logo=junit5&logoColor=white)](https://junit.org/junit5)
[![JaCoCo](https://img.shields.io/badge/Coverage-JaCoCo-red?logo=codecov&logoColor=white)](https://www.jacoco.org)
[![Status](https://img.shields.io/badge/Status-Completo-success)](https://github.com/becker84/desPatternDDD_DR2TP1)

# Sistema de Gerência de Biblioteca — DR2-TP1

> **Refatoração de sistema de biblioteca com cinco padrões de projeto GoF — Factory Method, Singleton, Facade, Observer e Strategy — organizados segundo os princípios de Domain-Driven Design.**

[![LinkedIn](https://img.shields.io/badge/LinkedIn-@becker84-0077B5?logo=linkedin)](https://linkedin.com/in/becker84)
[![GitHub](https://img.shields.io/badge/GitHub-@becker84-181717?logo=github&logoColor=white)](https://github.com/becker84)

</div>

---

## Índice

- [Sobre o Projeto](#sobre-o-projeto)
- [Padrões de Projeto Implementados](#padrões-de-projeto-implementados)
- [Arquitetura e DDD](#arquitetura-e-ddd)
- [Estrutura do Projeto](#estrutura-do-projeto)
- [Como Executar](#como-executar)
- [Testes](#testes)
- [API REST](#api-rest)
- [Relatório Técnico](#relatório-técnico)
- [Referências](#referências)

---

## Sobre o Projeto

O sistema de gerência de biblioteca original apresentava alto acoplamento, repetição de lógica de busca e dificuldade de manutenção — adicionar um novo tipo de item (DVD) exigia modificações em cascata em múltiplas classes.

Esta entrega aplica cinco padrões GoF para resolver cada problema identificado, mantendo aderência aos princípios SOLID, Clean Code e Domain-Driven Design.

---

## Padrões de Projeto Implementados

| Padrão             | Categoria GoF  | Problema resolvido                                                                                                   |
|--------------------|----------------|----------------------------------------------------------------------------------------------------------------------|
| **Singleton**      | Criacional     | Garante instância única de `Library` com estado consistente em toda a aplicação                                      |
| **Factory Method** | Criacional     | Cria `Book` e `Dvd` sem acoplar o cliente ao tipo concreto; extensível sem modificar código existente                |
| **Facade**         | Estrutural     | Interface única `LibraryFacade` substitui acesso direto a Singleton, Factories, Observer e Strategy                  |
| **Observer**       | Comportamental | Notificação proativa de empréstimos vencidos sem polling; novos observers adicionados sem alterar classes existentes |
| **Strategy**       | Comportamental | Algoritmos de busca (título, autor, ISBN, nome) intercambiáveis em tempo de execução                                 |

---

## Arquitetura e DDD

```
                          ┌──────────────────────────────────┐
     Cliente (Controller) │      LibraryFacade  (Facade)     │
       ↕ REST / Test      │  addBook · borrowItem · search   │
                          └──────────┬───────────────────────┘
                                     │ coordena
          ┌──────────────────────────┼───────────────────────────┐
          │                          │                           │
          ↓                          ↓                           ↓
   Library (Singleton)       LibraryItemFactory           SearchContext<T>
   Aggregate Root            BookFactory / DvdFactory     + Strategy
   items / users / loans     cria Book ou Dvd             title / author / isbn / name
          │
          │
          └──→ LoanEventDispatcher (Observer Subject)
                   → OverdueNotificationObserver
```

**Conceitos DDD aplicados:**

| Conceito            | Implementação                                                          |
|---------------------|------------------------------------------------------------------------|
| Entity              | `LibraryItem` (abstract), `Book`, `Dvd`, `User`, `Loan`                |
| Value Object        | `LoanStatus` (enum — sem identidade, imutável)                         |
| Aggregate Root      | `Library` (Singleton — controla todas as coleções)                     |
| Domain Service      | `LibraryFacade` (orquestra operações que envolvem múltiplas entidades) |
| Ubiquitous Language | `borrowItem`, `returnItem`, `findOverdueLoans`, `checkOut`, `checkIn`  |

---

## Estrutura do Projeto

```
src/
├── main/java/com/becker/biblioteca/
│   ├── LibraryApplication.java
│   ├── model/
│   │   ├── LibraryItem.java               # entidade abstrata — base do acervo
│   │   ├── Book.java                      # livro: author + isbn
│   │   ├── Dvd.java                       # DVD: director + durationMinutes
│   │   ├── User.java                      # usuário com identidade
│   │   ├── Loan.java                      # empréstimo com ciclo de vida
│   │   └── LoanStatus.java               # ACTIVE / RETURNED / OVERDUE
│   ├── pattern/
│   │   ├── singleton/
│   │   │   └── Library.java              # Singleton — DCL thread-safe
│   │   ├── factory/
│   │   │   ├── LibraryItemFactory.java   # Factory Method abstrata
│   │   │   ├── BookFactory.java          # cria Book
│   │   │   ├── DvdFactory.java           # cria Dvd
│   │   │   └── ItemCreationRequest.java  # Builder para parâmetros de criação
│   │   ├── observer/
│   │   │   ├── LoanObserver.java         # interface Observer
│   │   │   ├── LoanSubject.java          # interface Subject
│   │   │   ├── LoanEventDispatcher.java  # Subject concreto
│   │   │   └── OverdueNotificationObserver.java  # Observer concreto
│   │   └── strategy/
│   │       ├── SearchStrategy.java       # interface genérica Strategy<T>
│   │       ├── TitleSearchStrategy.java
│   │       ├── AuthorSearchStrategy.java
│   │       ├── IsbnSearchStrategy.java
│   │       ├── UserNameSearchStrategy.java
│   │       └── SearchContext.java        # Context — troca de estratégia em runtime
│   ├── service/
│   │   └── LibraryFacade.java            # Facade — único ponto de entrada
│   ├── controller/
│   │   └── LibraryController.java        # REST controller
│   ├── dto/                              # request/response records
│   ├── exception/
│   │   └── GlobalExceptionHandler.java
│   └── config/
│       └── LibraryObserverConfig.java    # registro de observers no boot
└── test/java/com/becker/biblioteca/
    ├── LibraryApplicationTest.java
    └── pattern/
        ├── singleton/LibrarySingletonTest.java   # unicidade + thread-safety
        ├── factory/  BookFactoryTest.java         # criação + validações
        │             DvdFactoryTest.java
        ├── observer/ LoanEventDispatcherTest.java # subscribe/notify
        ├── strategy/ SearchStrategyTest.java      # todos os algoritmos
        └── facade/   LibraryFacadeTest.java       # integração end-to-end
```

---

## Como Executar

**Pré-requisitos:** JDK 25 (Temurin), Gradle 9.5.0

```bash
# Executar aplicação (porta 8080)
./gradlew bootRun

# Rodar todos os testes
./gradlew test

# Relatório de cobertura (build/reports/jacoco/test/html/index.html)
./gradlew jacocoTestReport

# Build completo
./gradlew build
```

---

## Testes

**35+ testes — 0 falhas — `./gradlew test`**

| Classe de Teste           | Testes | Comportamentos validados                                                                          |
|---------------------------|--------|---------------------------------------------------------------------------------------------------|
| `LibrarySingletonTest`    | 4      | Unicidade de instância, thread-safety (20 threads), coleções imutáveis                            |
| `BookFactoryTest`         | 5      | Criação válida, IDs únicos, rejeição de autor/isbn nulo ou vazio                                  |
| `DvdFactoryTest`          | 3      | Criação válida, rejeição de diretor vazio, duração zero ou negativa                               |
| `SearchStrategyTest`      | 9      | Busca por título, autor, ISBN, nome de usuário; case-insensitive; troca de estratégia em runtime  |
| `LoanEventDispatcherTest` | 5      | subscribe/unsubscribe, criação, devolução, múltiplos observers, sem falso positivo em vencimentos |
| `LibraryFacadeTest`       | 13     | Factory Method via facade, ciclo completo de empréstimo, buscas, exceções de domínio              |
| `LibraryApplicationTest`  | 1      | Contexto Spring carrega sem erros                                                                 |

---

## API REST

**Base URL:** `http://localhost:8080/api/biblioteca`

```bash
# Adicionar livro
curl -X POST /api/biblioteca/livros \
  -H "Content-Type: application/json" \
  -d '{"title":"Código Limpo","author":"Robert C. Martin","isbn":"978-85-7608-437-4"}'

# Adicionar DVD
curl -X POST /api/biblioteca/dvds \
  -H "Content-Type: application/json" \
  -d '{"title":"Matrix","director":"Lana Wachowski","durationMinutes":136}'

# Adicionar usuário
curl -X POST /api/biblioteca/usuarios \
  -H "Content-Type: application/json" \
  -d '{"name":"Ana Becker","email":"ana@email.com"}'

# Realizar empréstimo
curl -X POST /api/biblioteca/emprestimos \
  -H "Content-Type: application/json" \
  -d '{"itemId":"<id>","userId":"<id>"}'

# Devolver item
curl -X PUT /api/biblioteca/emprestimos/<loanId>/devolucao

# Buscar por título (Strategy)
curl "/api/biblioteca/itens/busca/titulo?q=código"

# Buscar por autor (Strategy)
curl "/api/biblioteca/itens/busca/autor?q=martin"

# Consultar empréstimos vencidos (Observer + Strategy)
curl /api/biblioteca/emprestimos/vencidos
```

| Método | Endpoint                      | Descrição                        |
|--------|-------------------------------|----------------------------------|
| POST   | `/livros`                     | Adicionar livro (Factory Method) |
| POST   | `/dvds`                       | Adicionar DVD (Factory Method)   |
| DELETE | `/itens/{id}`                 | Remover item do acervo           |
| GET    | `/itens`                      | Listar todo o acervo             |
| GET    | `/itens/busca/titulo?q=`      | Busca por título (Strategy)      |
| GET    | `/itens/busca/autor?q=`       | Busca por autor (Strategy)       |
| GET    | `/itens/busca/isbn?q=`        | Busca por ISBN (Strategy)        |
| POST   | `/usuarios`                   | Adicionar usuário                |
| DELETE | `/usuarios/{id}`              | Remover usuário                  |
| GET    | `/usuarios`                   | Listar usuários                  |
| GET    | `/usuarios/busca?q=`          | Busca por nome (Strategy)        |
| POST   | `/emprestimos`                | Realizar empréstimo              |
| PUT    | `/emprestimos/{id}/devolucao` | Registrar devolução              |
| GET    | `/emprestimos`                | Listar empréstimos               |
| GET    | `/emprestimos/vencidos`       | Listar vencidos (Observer)       |

---

## Relatório Técnico

A justificativa completa de cada padrão — diagrama refatorado em PlantUML, código antes/depois, mapeamento problema→solução e princípios SOLID — está em [`doc/RELATORIO_TP1.md`](doc/RELATORIO_TP1.md).

---

## Referências

- FREEMAN, Eric; ROBSON, Elisabeth. *Use a Cabeça! Padrões de Projetos*. 2. ed. Rio de Janeiro: Alta Books, 2022. ISBN 978-65-5956-022-6.
- GAMMA, Erich et al. *Padrões de Projeto: Soluções Reutilizáveis de Software Orientado a Objetos*. Porto Alegre: Bookman, 2007. ISBN 978-85-363-0606-4.
- MARTIN, Robert C. *Código Limpo: Habilidades Práticas do Agile Software*. Rio de Janeiro: Alta Books, 2011. ISBN 978-85-7608-437-4.
- FOWLER, Martin. *Refatoração: Aperfeiçoando o Design de Códigos Existentes*. 2. ed. Porto Alegre: Bookman, 2020. ISBN 978-85-8055-391-3.
- EVANS, Eric. *Domain-Driven Design: Atacando as Complexidades no Coração do Software*. Rio de Janeiro: Alta Books, 2016. ISBN 978-85-7608-872-3.
- BLOCH, Joshua. *Java Efetivo*. 3. ed. Rio de Janeiro: Alta Books, 2019. ISBN 978-85-508-0036-7.

---

<div align="center">

<p><strong>Desenvolvido como Trabalho Prático da disciplina de Design Patterns e Domain-Driven Design.</strong></p>

<p>
  <a href="https://www.java.com/"><img src="https://img.shields.io/badge/Made%20with-Java_25-orange?logo=openjdk" alt="Java 25"></a>
  <a href="https://gradle.org/"><img src="https://img.shields.io/badge/Built%20with-Gradle-02303A?logo=gradle&logoColor=white" alt="Gradle"></a>
  <a href="https://spring.io/projects/spring-boot"><img src="https://img.shields.io/badge/Powered%20by-Spring_Boot-6DB33F?logo=springboot&logoColor=white" alt="Spring Boot"></a>
  <a href="https://junit.org/junit5/"><img src="https://img.shields.io/badge/Tested%20with-JUnit_5-green?logo=junit5&logoColor=white" alt="JUnit 5"></a>
</p>

<a href="doc/images/card.svg">
  <img src="doc/images/card.svg" width="360" alt="Becker - Software Engineer">
</a>

<p><em>Instituto Infnet — Engenharia de Software — 2026.</em></p>

</div>
