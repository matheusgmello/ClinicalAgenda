package dev.matheus.ClinicalAgenda.infra.notifications;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Service
public class EmailService {

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy 'às' HH:mm");

    private final JavaMailSender mailSender;

    public EmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    @Async
    public void enviarConfirmacaoAgendamento(String pacienteEmail, String pacienteNome,
                                              String identificador, LocalDateTime dataInicio) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("noreply@clinicalagenda.com");
        message.setTo(pacienteEmail);
        message.setSubject("Consulta agendada — " + identificador);
        message.setText(
                "Olá, " + pacienteNome + "!\n\n" +
                "Sua consulta foi agendada com sucesso.\n" +
                "Identificador: " + identificador + "\n" +
                "Data e hora: " + dataInicio.format(FORMATTER) + "\n\n" +
                "Em caso de dúvidas, entre em contato conosco.\n\n" +
                "Clínica Agenda"
        );
        mailSender.send(message);
    }
}
