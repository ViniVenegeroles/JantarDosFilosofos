# 🍽️ Jantar dos Filósofos

Implementação do clássico problema de concorrência **Jantar dos Filósofos** (Dining Philosophers Problem), proposto por Edsger Dijkstra. Desenvolvido para a disciplina de **Sistemas Operacionais** — UCSAL.

---

## 📖 Sobre o Problema

Cinco filósofos sentam à mesa circular. Entre cada par de filósofos há um único garfo, totalizando cinco garfos. Para comer, um filósofo precisa segurar os **dois garfos adjacentes** (esquerdo e direito). Enquanto não está comendo, o filósofo pensa.

O desafio é coordenar o acesso aos garfos de forma que:
- Não ocorra **deadlock** (todos esperando indefinidamente)
- Não ocorra **starvation** (nenhum filósofo fica sem comer para sempre)

---

## 🛡️ Estratégia Anti-Deadlock

A solução utiliza **duas técnicas combinadas**:

1. **`tryAcquire()` não-bloqueante** — em vez de esperar indefinidamente por um garfo (o que causaria deadlock), o filósofo tenta pegá-lo. Se não conseguir, solta o que já segurava e volta a pensar.

2. **Ordem aleatória dos garfos** — a cada tentativa, o filósofo escolhe aleatoriamente se tenta pegar primeiro o garfo da esquerda ou da direita. Isso quebra a simetria que leva ao deadlock clássico, onde todos tentam pegar o garfo esquerdo ao mesmo tempo.

---

## 🗂️ Estrutura do Projeto

```
JantarDosFilosofos/
├── Main.java        # Ponto de entrada: configura semáforos, inicia threads e exibe estatísticas
└── Filosofo.java    # Thread de cada filósofo: ciclo pensar → pegar garfos → comer → soltar
```

---

## ⚙️ Como Executar

**Pré-requisito:** Java 8 ou superior.

```bash
# Compile
javac JantarDosFilosofos/*.java

# Execute
java JantarDosFilosofos.Main
```

Ao iniciar, o programa pedirá o modo de execução:

```
=== Jantar dos Filósofos ===
Escolha o modo de execução:
  1 - Modo TEMPO    (simulação por N segundos)
  2 - Modo RODADAS  (cada filósofo faz R refeições)
Opção:
```

---

## 🔧 Modos de Execução

| Modo | Descrição |
|------|-----------|
| **TEMPO** | A simulação roda por N segundos e é encerrada automaticamente |
| **RODADAS** | Cada filósofo realiza exatamente R refeições e a simulação termina |

---

## 📊 Saída do Programa

Durante a execução, cada ação dos filósofos é registrada em tempo real:

```
Filósofo 0 está pensando.
Filósofo 1 está pensando.
Filósofo 0 pegou o garfo esquerdo.
Filósofo 0 pegou o garfo direito.
Filósofo 0 está comendo.
Filósofo 0 terminou de comer. (refeição nº 1)
Filósofo 0 abaixou o garfo esquerdo.
Filósofo 0 abaixou o garfo direito.
```

Ao final, são exibidas as estatísticas da simulação:

```
╔══════════════════════════════════════╗
║       ESTATÍSTICAS FINAIS            ║
╠══════════════════════════════════════╣
║  Filósofo 0 comeu   12 vezes         ║
║  Filósofo 1 comeu   10 vezes         ║
...
╠══════════════════════════════════════╣
║  Total de refeições:    55           ║
║  Média por filósofo:    11.0         ║
║  Mais saciado:  Filósofo 0 (12×)     ║
║  Mais faminto:  Filósofo 1 (10×)     ║
╚══════════════════════════════════════╝
```

---

## 🧵 Conceitos de S.O. Aplicados

| Conceito | Como é usado |
|----------|-------------|
| **Semáforo binário** | Cada garfo é um `Semaphore(1)`, garantindo acesso exclusivo |
| **Thread** | Cada filósofo é uma `Thread` independente (`extends Thread`) |
| **Região crítica** | O momento entre pegar e soltar os garfos |
| **Deadlock** | Evitado com `tryAcquire()` e ordem aleatória dos garfos |
| **`volatile`** | A flag `executando` usa `volatile` para garantir visibilidade entre threads |

---
