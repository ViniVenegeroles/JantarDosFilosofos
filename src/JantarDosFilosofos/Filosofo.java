package JantarDosFilosofos;

import java.util.concurrent.Semaphore;

public class Filosofo extends Thread {
    private final int id;
    private final Semaphore garfoEsquerdo;
    private final Semaphore garfoDireito;
    private final int maxRodadas;
    private int contadorRefeicoes;
    private volatile boolean executando = true;

    public Filosofo(int id, Semaphore garfoEsquerdo, Semaphore garfoDireito) {
        this.id = id;
        this.garfoEsquerdo = garfoEsquerdo;
        this.garfoDireito = garfoDireito;
        this.contadorRefeicoes = 0;
        this.maxRodadas = -1;
    }

    public Filosofo(int id, Semaphore garfoEsquerdo, Semaphore garfoDireito, int maxRodadas) {
        this.id = id;
        this.garfoEsquerdo = garfoEsquerdo;
        this.garfoDireito = garfoDireito;
        this.contadorRefeicoes = 0;
        this.maxRodadas = maxRodadas;
    }

    public void pararExecucao() {
        this.executando = false;
        this.interrupt();
    }

    @Override
    public void run() {
        try {
            while (executando && (maxRodadas < 0 || contadorRefeicoes < maxRodadas)) {

                pensar();
                boolean garfosPegos = false;
                try {
                    garfosPegos = pegueGarfos();

                    if (garfosPegos) {
                        comer();
                        contadorRefeicoes++;
                        System.out.println("Filósofo " + id + " terminou de comer. (refeição nº " + contadorRefeicoes + ")");
                    }
                } finally {
                    if (garfosPegos) {
                        abaixarGarfos();
                    }
                }
            }

        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    public int getContadorRefeicoes() {
        return contadorRefeicoes;
    }

    public int getNumeroFilosofo() {
        return id;
    }

    private void pensar() throws InterruptedException {
        System.out.println("Filósofo " + id + " está pensando.");
        Thread.sleep((long) (Math.random() * 1000));
    }

    private boolean pegueGarfos() throws InterruptedException {
        if (Math.random() < 0.5) {
            if (!garfoEsquerdo.tryAcquire()) return false;

            System.out.println("Filósofo " + id + " pegou o garfo esquerdo.");

            if (!garfoDireito.tryAcquire()) {
                garfoEsquerdo.release();
                System.out.println("Filósofo " + id + " soltou o esquerdo (direito indisponível).");
                return false;
            }
            System.out.println("Filósofo " + id + " pegou o garfo direito.");
        } else {
            if (!garfoDireito.tryAcquire()) return false;

            System.out.println("Filósofo " + id + " pegou o garfo direito.");

            if (!garfoEsquerdo.tryAcquire()) {
                garfoDireito.release();
                System.out.println("Filósofo " + id + " soltou o direito (esquerdo indisponível).");
                return false;
            }
            System.out.println("Filósofo " + id + " pegou o garfo esquerdo.");
        }
        return true;
    }

    private void comer() throws InterruptedException {
        System.out.println("Filósofo " + id + " está comendo.");
        Thread.sleep((long) (Math.random() * 1000));
    }

    private void abaixarGarfos() {
        garfoEsquerdo.release();
        System.out.println("Filósofo " + id + " abaixou o garfo esquerdo.");
        garfoDireito.release();
        System.out.println("Filósofo " + id + " abaixou o garfo direito.");
    }
}
