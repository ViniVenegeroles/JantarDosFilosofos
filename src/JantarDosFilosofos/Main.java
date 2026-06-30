package JantarDosFilosofos;

import java.util.Scanner;
import java.util.concurrent.Semaphore;

public class Main {

    private static final int NUM_FILOSOFOS = 5;

    public static void main(String[] args) throws InterruptedException {

        Scanner scanner = new Scanner(System.in);

        System.out.println("=== Jantar dos Filósofos ===");
        System.out.println("Escolha o modo de execução:");
        System.out.println("  1 - Modo TEMPO    (simulação por N segundos)");
        System.out.println("  2 - Modo RODADAS  (cada filósofo faz R refeições)");
        System.out.print("Opção: ");

        int modo = lerInteiroPositivo(scanner);
        while (modo > 2) {
            System.out.println("Numero invalido! Digite um modo disponivel:");
            modo = lerInteiroPositivo(scanner);
        }
        int duracaoSegundos = 0;
        int rodadas = 0;

        if (modo == 1) {
            System.out.print("Duração em segundos: ");
            duracaoSegundos = lerInteiroPositivo(scanner);
        } else {
            System.out.print("Número de rodadas (refeições por filósofo): ");
            rodadas = lerInteiroPositivo(scanner);
        }

        Semaphore[] garfos = new Semaphore[NUM_FILOSOFOS];
        for (int i = 0; i < NUM_FILOSOFOS; i++) {
            garfos[i] = new Semaphore(1);
        }

        Filosofo[] filosofos = new Filosofo[NUM_FILOSOFOS];
        for (int i = 0; i < NUM_FILOSOFOS; i++) {
            if (modo == 1) {
                filosofos[i] = new Filosofo(i, garfos[i], garfos[(i + 1) % NUM_FILOSOFOS]);
            } else {
                filosofos[i] = new Filosofo(i, garfos[i], garfos[(i + 1) % NUM_FILOSOFOS], rodadas);
            }
            filosofos[i].start();
        }

        System.out.println("\n[Simulação iniciada...]\n");

        if (modo == 1) {
            Thread.sleep(duracaoSegundos * 1000L);

            System.out.println("\n[Tempo esgotado. Encerrando filósofos...]\n");
            for (Filosofo f : filosofos) {
                f.pararExecucao();
            }
        }

        for (Filosofo f : filosofos) {
            f.join(2000);
        }

        exibirEstatisticas(filosofos);

        scanner.close();
    }

    private static void exibirEstatisticas(Filosofo[] filosofos) {
        System.out.println("╔══════════════════════════════════════╗");
        System.out.println("║       ESTATÍSTICAS FINAIS            ║");
        System.out.println("╠══════════════════════════════════════╣");

        int total = 0;
        int minRefeicoes = Integer.MAX_VALUE;
        int maxRefeicoes = Integer.MIN_VALUE;
        int filosofoMaisHambrento = -1;
        int filosofoMaisSaciado = -1;

        for (Filosofo f : filosofos) {
            int r = f.getContadorRefeicoes();
            System.out.format("║  Filósofo %d comeu %4d vezes         ║%n",
                    f.getNumeroFilosofo(), r);
            total += r;

            if (r < minRefeicoes) {
                minRefeicoes = r;
                filosofoMaisHambrento = f.getNumeroFilosofo();
            }
            if (r > maxRefeicoes) {
                maxRefeicoes = r;
                filosofoMaisSaciado = f.getNumeroFilosofo();
            }
        }

        double media = (double) total / filosofos.length;

        System.out.println("╠══════════════════════════════════════╣");
        System.out.format("║  Total de refeições: %5d            ║%n", total);
        System.out.format("║  Média por filósofo: %8.1f        ║%n", media);
        System.out.format("║  Mais saciado:  Filósofo %d (%d×)      ║%n",
                filosofoMaisSaciado, maxRefeicoes);
        System.out.printf("║  Mais faminto:  Filósofo %d (%d×)      ║%n",
                filosofoMaisHambrento, minRefeicoes);
        System.out.println("╚══════════════════════════════════════╝");
    }

    private static int lerInteiroPositivo(Scanner scanner) {
        while (true) {
            try {
                int valor = Integer.parseInt(scanner.nextLine().trim());
                if (valor > 0) {
                    return valor;
                }
                System.out.print("Digite um número maior que zero: ");
            } catch (NumberFormatException e) {
                System.out.print("Entrada inválida. Digite um número inteiro: ");
            }
        }
    }
}