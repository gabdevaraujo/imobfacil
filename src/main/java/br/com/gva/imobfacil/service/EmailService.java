package br.com.gva.imobfacil.service;

import br.com.gva.imobfacil.model.MensagemContato;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmailService {

    private final JavaMailSender mailSender;
    private final TemplateEngine templateEngine;

    @Value("${app.mail.from:noreply@imobfacil.com.br}")
    private String from;

    @Value("${app.mail.admin:contato@imobfacil.com.br}")
    private String adminEmail;

    @Async
    public void enviarNotificacaoLead(MensagemContato lead) {
        try {
            String destinatario = resolverDestinatario(lead);
            String html = buildHtml(lead);

            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
            helper.setFrom(from);
            helper.setTo(destinatario);
            helper.setSubject("Novo lead recebido — " + lead.getNome());
            helper.setText(html, true);

            mailSender.send(message);
            log.info("E-mail de notificação enviado para {} (lead ID: {})", destinatario, lead.getId());
        } catch (MessagingException e) {
            log.error("Falha ao enviar e-mail de notificação para lead ID {}: {}", lead.getId(), e.getMessage());
        }
    }

    private String resolverDestinatario(MensagemContato lead) {
        if (lead.getImovel() != null && lead.getImovel().getCorretor() != null) {
            return lead.getImovel().getCorretor().getEmail();
        }
        return adminEmail;
    }

    private String buildHtml(MensagemContato lead) {
        Context ctx = new Context();
        ctx.setVariable("lead", lead);
        ctx.setVariable("imovel", lead.getImovel());
        ctx.setVariable("corretor",
            lead.getImovel() != null ? lead.getImovel().getCorretor() : null);
        return templateEngine.process("email/notificacao-lead", ctx);
    }
}
