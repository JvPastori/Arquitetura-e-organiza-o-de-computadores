import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class CicloInstrucao {

    private int contadorPrograma = 0;
    private int registroBufferMemoria = 0;
    private boolean flagZero = false;
    private boolean flagNegativo = false;
    private int[] memoria = new int[256];
    private List<String> instrucoes = new ArrayList<>();

    public static void main(String[] args) {
        CicloInstrucao ci = new CicloInstrucao();
        Scanner scanner = new Scanner(System.in);

        while (true) {
            menu();
            System.out.print("Digite a opcao: ");
            int opcao = scanner.nextInt();

            switch (opcao) {
                case 1:
                    ci.entradaUsuario(scanner);
                    break;
                case 2:
                    ci.verInstrucoes();
                    break;
                case 3:
                    ci.rodarInstrucoes();
                    break;
                case 4:
                    System.out.println("Encerrando");
                    return;
                default:
                    System.out.println("Opcao invalida");
                    break;
            }
        }
    }

    private static void menu() {
        System.out.println(String.join("", "=", "=============================="));
        System.out.println("1 - Inserir");
        System.out.println("2 - Ver Instrucoes");
        System.out.println("3 - Executar");
        System.out.println("4 - Sair");
        System.out.println(String.join("", "=", "=============================="));
    }

    private void entradaUsuario(Scanner scanner) {
        System.out.println("Digite as instrucoes do programa (4 para sair da insercao)");
        while (true) {
            System.out.print("Digite o codigo da instrucao: ");
            String instrucao = scanner.next();
            if (instrucao.equals("4")) {
                break;
            }
            String op1 = "", op2 = "";
            if (instrucao.matches("000001|001010|001011|001100")) {
                op1 = scanner.next();
            } else {
                op1 = scanner.next();
                if (!instrucao.matches("000011|000100|000101|000110|000111|001000|001001|001111")) {
                    op2 = scanner.next();
                }
            }
            if (instrucao.matches("000001|001010|001011|001100")) {
                instrucoes.add(String.format("%s %s", instrucao, op1));
            } else {
                instrucoes.add(String.format("%s %s %s", instrucao, op1, op2));
            }
        }
    }

    private void verInstrucoes() {
        System.out.println(String.join("", "=", "=============================="));
        System.out.println("= INSTRUCOES: =");
        System.out.println(String.join("", "=", "=============================="));
        System.out.printf("%-10s %-10s %-10s %-25s%n", "COD", "OP1", "OP2", "RESULTADOS");

        String[][] instrucoesDesc = {
            {"000001", "RBM <- #POS"},
            {"000010", "POS <- #DADO"},
            {"000011", "RBM <- RBM + #POS"},
            {"000100", "RBM <- RBM - #POS"},
            {"000101", "RBM <- RBM * #POS"},
            {"000110", "RBM <- RBM / #POS"},
            {"000111", "JUMP to #LIN"},
            {"001000", "JUMP IF Z to #LIN"},
            {"001001", "JUMP IF N to #LIN"},
            {"001010", "RBM <- sqrt(RBM)"},
            {"001011", "RBM <- -RBM"},
            {"001111", "#POS <- RBM"},
            {"001100", "NOP"}
        };

        for (String[] instrucao : instrucoesDesc) {
            System.out.printf("%-10s %-10s %-10s %-25s%n", instrucao[0], "-", "-", instrucao[1]);
        }

        for (String instrucao : instrucoes) {
            System.out.println(instrucao);
        }
    }

    private void rodarInstrucoes() {
        System.out.println(String.join("", "=", "=============================="));
        System.out.println("EXECUTANDO");
        System.out.println(String.join("", "=", "=============================="));

        while (contadorPrograma < instrucoes.size()) {
            String instrucao = instrucoes.get(contadorPrograma);
            executaInstrucoes(instrucao);
            exibeCiclo();
            System.out.println();
        }
    }

    private void executaInstrucoes(String instrucao) {
        String[] componentes = instrucao.split(" ");
        String opcode = componentes[0];
        int op1 = componentes.length > 1 ? Integer.parseInt(componentes[1]) : 0;
        int op2 = componentes.length > 2 ? Integer.parseInt(componentes[2]) : 0;

        switch (opcode) {
            case "000001":
                instrucaoCarregarMemoria(op1);
                break;
            case "000010":
                instrucaoSalvarMemoria(op1, op2);
                break;
            case "000011":
                instrucaoAdicionar(op1);
                break;
            case "000100":
                instrucaoSubtrair(op1);
                break;
            case "000101":
                instrucaoMultiplicar(op1);
                break;
            case "000110":
                instrucaoDividir(op1);
                break;
            case "000111":
                instrucaoPular(op1);
                break;
            case "001000":
                instrucaoPularSeZero(op1);
                break;
            case "001001":
                instrucaoPularSeNegativo(op1);
                break;
            case "001010":
                instrucaoRaizQuadrada();
                break;
            case "001011":
                instrucaoNegar();
                break;
            case "001111":
                instrucaoSalvarBufferMemoria(op1);
                break;
            case "001100":
                instrucaoNOP();
                break;
            default:
                System.out.println("Instrucao invalida");
                break;
        }

        contadorPrograma++;
    }

    private void exibeCiclo() {
        if (contadorPrograma <= 0) return;

        String[] componentes = instrucoes.get(contadorPrograma - 1).split(" ");
        String opcode = componentes[0];
        String op1 = componentes.length > 1 ? componentes[1] : "";
        String op2 = componentes.length > 2 ? componentes[2] : "";

        System.out.println(String.join("", "=", "=============================="));
        System.out.println("CALCULO DO ENDERECO DA INSTRUCAO:");
        System.out.printf("PC: %06d%n", contadorPrograma);
        System.out.println("\nBUSCANDO A INSTRUCAO:");
        System.out.printf("<OPCODE>: %s%n", opcode);
        System.out.printf("<OP1>: %s%n", op1);
        System.out.printf("<OP2>: %s%n", op2);
        System.out.println("\nDECODIFICANDO A INSTRUCAO:");

        System.out.println("OPERACAO DE DADOS:");
        switch (opcode) {
            case "000001":
                System.out.printf("RBM <- %s%n", op1);
                break;
            case "000010":
                System.out.printf("%s <- %s%n", op1, op2);
                break;
            case "000011":
                System.out.printf("RBM <- RBM + %s%n", op1);
                break;
            case "000100":
                System.out.printf("RBM <- RBM - %s%n", op1);
                break;
            case "000101":
                System.out.printf("RBM <- RBM * %s%n", op1);
                break;
            case "000110":
                System.out.printf("RBM <- RBM / %s%n", op1);
                break;
            case "000111":
                System.out.printf("JUMP to %s%n", op1);
                break;
            case "001000":
                System.out.printf("JUMP IF Z to %s%n", op1);
                break;
            case "001001":
                System.out.printf("JUMP IF N to %s%n", op1);
                break;
            case "001010":
                System.out.println("RBM <- sqrt(RBM)");
                break;
            case "001011":
                System.out.println("RBM <- -RBM");
                break;
            case "001111":
                System.out.printf("%s <- RBM%n", op1);
                break;
            case "001100":
                System.out.println("NOP");
                System.out.println("ENCERRANDO OPERACAO DE DADOS");
                System.out.println("OPERACAO FINALIZADA!");
                System.exit(0);
                break;
            default:
                break;
        }

        System.out.println(String.join("", "=", "=============================="));
    }

    private void instrucaoCarregarMemoria(int pos) {
        if (pos >= 0 && pos < memoria.length) {
            registroBufferMemoria = memoria[pos];
        } else {
            System.out.println("Endereco fora dos limites da memoria");
        }
        atualizaFlags();
    }

    private void instrucaoSalvarMemoria(int pos, int dado) {
        if (pos >= 0 && pos < memoria.length) {
            memoria[pos] = dado;
        } else {
            System.out.println("Endereco fora dos limites da memoria");
        }
    }

    private void instrucaoAdicionar(int pos) {
        if (pos >= 0 && pos < memoria.length) {
            registroBufferMemoria += memoria[pos];
        } else {
            System.out.println("Endereco fora dos limites da memoria");
        }
        atualizaFlags();
    }

    private void instrucaoSubtrair(int pos) {
        if (pos >= 0 && pos < memoria.length) {
            registroBufferMemoria -= memoria[pos];
        } else {
            System.out.println("Endereco fora dos limites da memoria");
        }
        atualizaFlags();
    }

    private void instrucaoMultiplicar(int pos) {
        if (pos >= 0 && pos < memoria.length) {
            registroBufferMemoria *= memoria[pos];
        } else {
            System.out.println("Endereco fora dos limites da memoria");
        }
        atualizaFlags();
    }

    private void instrucaoDividir(int pos) {
        if (pos >= 0 && pos < memoria.length) {
            if (memoria[pos] != 0) {
                registroBufferMemoria /= memoria[pos];
            } else {
                System.out.println("Divisao por zero");
            }
        } else {
            System.out.println("Endereco fora dos limites da memoria");
        }
        atualizaFlags();
    }

    private void instrucaoPular(int pos) {
        if (pos >= 0 && pos < instrucoes.size()) {
            contadorPrograma = pos;
        } else {
            System.out.println("Endereco de salto fora dos limites");
        }
    }

    private void instrucaoPularSeZero(int pos) {
        if (flagZero) {
            instrucaoPular(pos);
        }
    }

    private void instrucaoPularSeNegativo(int pos) {
        if (flagNegativo) {
            instrucaoPular(pos);
        }
    }

    private void instrucaoRaizQuadrada() {
        registroBufferMemoria = (int) Math.sqrt(registroBufferMemoria);
        atualizaFlags();
    }

    private void instrucaoNegar() {
        registroBufferMemoria = -registroBufferMemoria;
        atualizaFlags();
    }

    private void instrucaoSalvarBufferMemoria(int pos) {
        if (pos >= 0 && pos < memoria.length) {
            memoria[pos] = registroBufferMemoria;
        } else {
            System.out.println("Endereco fora dos limites da memoria");
        }
    }

    private void instrucaoNOP() {
        // No Operation
    }

    private void atualizaFlags() {
        flagZero = (registroBufferMemoria == 0);
        flagNegativo = (registroBufferMemoria < 0);
    }
}
