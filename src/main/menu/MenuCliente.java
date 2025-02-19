package main.menu;

import main.controller.SistemaGerenciamentoClinica;
import main.entities.*;

import java.time.LocalDate;
import java.util.Scanner;
import main.services.*;

public class MenuCliente implements Menu {
    private final Scanner scanner;
    private final SistemaGerenciamentoClinica sistema;

    public MenuCliente(Scanner scanner, SistemaGerenciamentoClinica sistema) {
        this.scanner = scanner;
        this.sistema = sistema;
    }
    
    @Override
    public void exibirMenu() {
        System.out.println("\n=== Área do Cliente ===");
        if (sistema.getPacientes().isEmpty()) {
            System.out.println("Nenhum paciente cadastrado.");
            return;
        }

        System.out.println("Selecione seu nome: ");
        for (int i = 0; i < sistema.getPacientes().size(); i++) {
            System.out.println((i + 1) + ". " + sistema.getPacientes().get(i).getNome());
        }
        int pacienteIndex = scanner.nextInt() - 1;
        scanner.nextLine();
        Paciente paciente = sistema.getPacientes().get(pacienteIndex);

        exibirNotificacoes(paciente);
        return;
    }

    private void exibirNotificacoes(Paciente paciente) {
        LocalDate hoje = LocalDate.now();
        System.out.println("\n=== Notificações ===");

        boolean temNotificacoes = false;
        for (Consulta consulta : paciente.getHistoricoConsultas()) {
            if (consulta.getPaciente().equals(paciente) &&
                !consulta.getDataConsulta().isBefore(hoje) &&
                consulta.getDataConsulta().isBefore(hoje.plusDays(3))) {
                System.out.println("Lembrete: Você tem uma consulta com " + consulta.getMedico().getNome() +
                        " no dia " + consulta.getDataConsulta());
                temNotificacoes = true;
            }
        }


        for (Prescricao prescricao : paciente.getPrescricoes()) {
            if(prescricao instanceof Exame){
                Exame exame = (Exame) prescricao;
                if (exame.getPaciente().equals(paciente) &&
                    !exame.getDataValidade().isBefore(hoje) &&
                    exame.getDataValidade().isBefore(hoje.plusDays(3))) {
                    System.out.println("Lembrete: Seu exame de " + exame.getTipo() +
                            " deve ser realizado até " + exame.getDataValidade());
                    temNotificacoes = true;
                }
        }
        }

        

        if (!temNotificacoes) {
            System.out.println("Nenhuma notificação no momento.");
        }

    }
}
