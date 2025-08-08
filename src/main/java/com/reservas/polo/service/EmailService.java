package com.reservas.polo.service;

import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import com.reservas.polo.model.Pago;
import com.reservas.polo.model.Reserva;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

@Service
public class EmailService {
	
	private final JavaMailSender emailSender;
	
	
	public EmailService(final JavaMailSender emailSender) {
		this.emailSender = emailSender;
	}
	
	public void sendHtmlEmail(String to, String subject, String htmlContent) throws MessagingException {
        MimeMessage message = emailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, "utf-8");
        
        helper.setTo(to);
        helper.setSubject(subject);
        helper.setText(htmlContent, true);
        helper.setFrom("villdarkk@gmail.com");
        
        emailSender.send(message);
    }
	
	public void enviarReembolsoAdmin(String correoAdmin, Reserva reserva, Pago pago) throws MessagingException {
		String asunto = "📩 Solicitud de reembolso - Reserva ID: " + reserva.getId();

	    String html = """
	       <h2>Solicitud de reembolso recibida</h2>
	       <p>El cliente ha solicitado el reembolso de la siguiente reserva:</p>
	        <p><strong>🏠 Inmueble:</strong> %s</p>
	        <p><strong>👤 Cliente:</strong> %s %s</p>
	        <p><strong>📅 Fechas:</strong> %s a %s</p>
	        <p><strong>💳 Monto:</strong> S/ %.2f</p>
	        <p><strong>🧾 ID de pago Stripe:</strong> %s</p>
	        <hr>
	        <p>La reserva y el pago han sido eliminados del sistema como parte del reembolso.</p>
	    """.formatted(
	        reserva.getInmueble().getNombre(),
	        reserva.getCliente().getNombre(), reserva.getCliente().getApellido(),
	        reserva.getFechaInicio(), reserva.getFechaFin(),
	        pago.getMonto(),
	        pago.getStripePaymentId()
	    );

	    sendHtmlEmail(correoAdmin, asunto, html);
	}

	
	
  
}
