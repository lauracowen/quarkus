package io.quarkus.mailer.runtime;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Scanner;
import java.util.UUID;

import jakarta.mail.BodyPart;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import jakarta.mail.internet.MimeMultipart;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.subethamail.wiser.Wiser;
import org.subethamail.wiser.WiserMessage;

import io.quarkus.mailer.Mail;
import io.smallrye.mutiny.Multi;
import io.vertx.ext.mail.MailConfig;
import io.vertx.mutiny.core.Vertx;
import io.vertx.mutiny.ext.mail.MailClient;

class MailerImplTest {

    private static final String FROM = "test@test.org";
    private static final String TO = "foo@quarkus.io";
    private static final String TEXT_CONTENT_TYPE = "text/plain";

    private static Wiser wiser;
    private static Vertx vertx;
    private MutinyMailerImpl mailer;

    @BeforeAll
    static void startWiser() {
        wiser = Wiser.port(SocketUtil.findAvailablePort());
        wiser.start();

        vertx = Vertx.vertx();
    }

    @AfterAll
    static void stopWiser() {
        wiser.stop();
        vertx.close().await().indefinitely();
    }

    @BeforeEach
    void init() {
        mailer = new MutinyMailerImpl(vertx,
                MailClient.createShared(vertx,
                        new MailConfig().setPort(wiser.getServer().getPort())),
                null, FROM, null, false, List.of(), false, false, null);

        wiser.getMessages().clear();
    }

    @Test
    void testTextMail() throws MessagingException, IOException {
        String uuid = UUID.randomUUID().toString();
        mailer.send(Mail.withText(TO, "Test", uuid)).await().indefinitely();
        assertThat(wiser.getMessages()).hasSize(1);
        WiserMessage actual = wiser.getMessages().get(0);
        assertThat(getContent(actual)).contains(uuid);
        MimeMessage msg = actual.getMimeMessage();
        String content = (String) actual.getMimeMessage().getContent();
        assertThat(content).isEqualTo(uuid + "\r\n");
        assertThat(msg.getSubject()).isEqualTo("Test");
        assertThat(msg.getFrom()[0].toString()).isEqualTo(FROM);
        assertThat(msg.getAllRecipients()).hasSize(1).contains(new InternetAddress(TO));
    }

    @Test
    void testHTMLMail() throws MessagingException {
        String content = UUID.randomUUID().toString();
        mailer.send(Mail.withHtml(TO, "Test", "<h1>" + content + "</h1>")).await().indefinitely();
        assertThat(wiser.getMessages()).hasSize(1);
        WiserMessage actual = wiser.getMessages().get(0);
        assertThat(getContent(actual)).contains("<h1>" + content + "</h1>");
        List<String> types = Collections.singletonList(actual.getMimeMessage().getContentType());
        assertThat(types).containsExactly("text/html");
        MimeMessage msg = actual.getMimeMessage();
        assertThat(msg.getSubject()).isEqualTo("Test");
        assertThat(msg.getContentType()).startsWith("text/html");
        assertThat(msg.getFrom()[0].toString()).isEqualTo(FROM);
        assertThat(msg.getAllRecipients()).hasSize(1).contains(new InternetAddress(TO));
    }

    @Test
    void testWithSeveralMails() {
        Mail mail1 = Mail.withText(TO, "Mail 1", "Mail 1").addCc("cc@quarkus.io").addBcc("bcc@quarkus.io");
        Mail mail2 = Mail.withHtml(TO, "Mail 2", "<strong>Mail 2</strong>").addCc("cc2@quarkus.io").addBcc("bcc2@quarkus.io");
        mailer.send(mail1, mail2).await().indefinitely();
        assertThat(wiser.getMessages()).hasSize(6);
    }

    @Test
    void testHeaders() throws MessagingException {
        mailer.send(Mail.withText(TO, "Test", "testHeaders")
                .addHeader("X-header", "value")
                .addHeader("X-header-2", "value1", "value2"))
                .await().indefinitely();
        assertThat(wiser.getMessages()).hasSize(1);
        WiserMessage actual = wiser.getMessages().get(0);
        MimeMessage msg = actual.getMimeMessage();
        assertThat(msg.getSubject()).isEqualTo("Test");
        assertThat(msg.getFrom()[0].toString()).isEqualTo(FROM);
        assertThat(msg.getHeader("X-header")).hasSize(1).contains("value");
        assertThat(msg.getHeader("X-header-2")).hasSize(2).contains("value1", "value2");
    }

    @Test
    void testAttachment() throws MessagingException, IOException {
        String payload = UUID.randomUUID().toString();
        mailer.send(Mail.withText(TO, "Test", "testAttachment")
                .addAttachment("my-file.txt", payload.getBytes(StandardCharsets.UTF_8), TEXT_CONTENT_TYPE))
                .await().indefinitely();
        assertThat(wiser.getMessages()).hasSize(1);
        WiserMessage actual = wiser.getMessages().get(0);
        assertThat(getContent(actual)).contains("testAttachment");
        MimeMessage msg = actual.getMimeMessage();
        assertThat(msg.getSubject()).isEqualTo("Test");
        assertThat(msg.getFrom()[0].toString()).isEqualTo(FROM);
        String value = getAttachment("my-file.txt", (MimeMultipart) actual.getMimeMessage().getContent());
        assertThat(value).isEqualTo(payload);
    }

    @Test
    void testAttachmentAsStream() throws MessagingException, IOException {
        String payload = UUID.randomUUID().toString();
        byte[] bytes = payload.getBytes(StandardCharsets.UTF_8);
        Iterable<Byte> iterable = () -> new Iterator<Byte>() {
            private int index = 0;

            @Override
            public boolean hasNext() {
                return bytes.length > index;
            }

            @Override
            public Byte next() {
                return bytes[index++];
            }
        };

        mailer.send(Mail.withText(TO, "Test", "testAttachmentAsStream")
                .addAttachment("my-file.txt", Multi.createFrom().iterable(iterable), TEXT_CONTENT_TYPE))
                .await().indefinitely();
        assertThat(wiser.getMessages()).hasSize(1);
        WiserMessage actual = wiser.getMessages().get(0);
        assertThat(getContent(actual)).contains("testAttachment");
        MimeMessage msg = actual.getMimeMessage();
        assertThat(msg.getSubject()).isEqualTo("Test");
        assertThat(msg.getFrom()[0].toString()).isEqualTo(FROM);
        String value = getAttachment("my-file.txt", (MimeMultipart) actual.getMimeMessage().getContent());
        assertThat(value).isEqualTo(payload);
    }

    @Test
    void testInlineAttachment() throws MessagingException, IOException {
        String cid = UUID.randomUUID() + "@acme";
        mailer.send(Mail.withHtml(TO, "Test", "testInlineAttachment")
                .addInlineAttachment("inline.txt", "my inlined text".getBytes(StandardCharsets.UTF_8), TEXT_CONTENT_TYPE, cid))
                .await().indefinitely();
        assertThat(wiser.getMessages()).hasSize(1);
        WiserMessage actual = wiser.getMessages().get(0);
        assertThat(getContent(actual)).contains("testInlineAttachment");
        MimeMessage msg = actual.getMimeMessage();
        assertThat(msg.getSubject()).isEqualTo("Test");
        assertThat(msg.getFrom()[0].toString()).isEqualTo(FROM);

        String value = getInlineAttachment("<" + cid + ">", (MimeMultipart) actual.getMimeMessage().getContent());
        assertThat(value).isEqualTo("my inlined text");
    }

    @Test
    void testAttachments() throws MessagingException, IOException {
        mailer.send(Mail.withText(TO, "Test", "Simple Test")
                .addAttachment("some-data.txt", "Hello".getBytes(StandardCharsets.UTF_8), TEXT_CONTENT_TYPE)
                .addAttachment("some-data-2.txt", "Hello 2".getBytes(StandardCharsets.UTF_8), TEXT_CONTENT_TYPE))
                .await().indefinitely();
        assertThat(wiser.getMessages()).hasSize(1);
        WiserMessage actual = wiser.getMessages().get(0);
        assertThat(getContent(actual)).contains("Simple Test");
        MimeMessage msg = actual.getMimeMessage();
        assertThat(msg.getSubject()).isEqualTo("Test");
        assertThat(msg.getFrom()[0].toString()).isEqualTo(FROM);
        String value = getAttachment("some-data.txt", (MimeMultipart) actual.getMimeMessage().getContent());
        assertThat(value).isEqualTo("Hello");
        value = getAttachment("some-data-2.txt", (MimeMultipart) actual.getMimeMessage().getContent());
        assertThat(value).isEqualTo("Hello 2");
    }

    @Test
    void testReplyToHeaderIsSet() throws MessagingException {
        mailer.send(Mail.withText(TO, "Test", "testHeaders")
                .setReplyTo("reply-to@quarkus.io"))
                .await().indefinitely();
        assertThat(wiser.getMessages()).hasSize(1);
        WiserMessage actual = wiser.getMessages().get(0);
        MimeMessage msg = actual.getMimeMessage();
        assertThat(msg.getHeader("Reply-To")).containsExactly("reply-to@quarkus.io");
        assertThat(msg.getReplyTo()).containsExactly(InternetAddress.parse("reply-to@quarkus.io"));
    }

    @Test
    void testMultipleReplyToHeaderIsSet() throws MessagingException {
        mailer.send(Mail.withText(TO, "Test", "testHeaders")
                .setReplyTo("reply-to@quarkus.io", "another@quarkus.io"))
                .await().indefinitely();
        assertThat(wiser.getMessages()).hasSize(1);
        WiserMessage actual = wiser.getMessages().get(0);
        MimeMessage msg = actual.getMimeMessage();
        assertThat(msg.getHeader("Reply-To")).containsExactly("reply-to@quarkus.io,another@quarkus.io");
        assertThat(msg.getReplyTo()).hasSize(2).contains(InternetAddress.parse("reply-to@quarkus.io"))
                .contains(InternetAddress.parse("another@quarkus.io"));
    }

    private String getContent(WiserMessage msg) {
        try {
            Object content = msg.getMimeMessage().getContent();
            if (content instanceof String) {
                return content.toString();
            }
            return getTextFromMimeMultipart((MimeMultipart) content);
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
    }

    private String getAttachment(String name, MimeMultipart multipart) throws IOException, MessagingException {
        for (int i = 0; i < multipart.getCount(); i++) {
            BodyPart bodyPart = multipart.getBodyPart(i);
            if (bodyPart.getFileName() != null && bodyPart.getFileName().equalsIgnoreCase(name)) {
                assertThat(bodyPart.getContentType()).startsWith(TEXT_CONTENT_TYPE);
                return read(bodyPart);
            }
        }
        return null;
    }

    private String getInlineAttachment(String cid, MimeMultipart multipart) throws IOException, MessagingException {
        for (int i = 0; i < multipart.getCount(); i++) {
            BodyPart bodyPart = multipart.getBodyPart(i);
            if (bodyPart.getContent() instanceof MimeMultipart) {
                for (int j = 0; j < ((MimeMultipart) bodyPart.getContent()).getCount(); j++) {
                    BodyPart nested = ((MimeMultipart) bodyPart.getContent()).getBodyPart(j);
                    if (nested.getHeader("Content-ID") != null && nested.getHeader("Content-ID")[0].equalsIgnoreCase(cid)) {
                        assertThat(nested.getDisposition()).isEqualTo("inline");
                        assertThat(nested.getContentType()).startsWith(TEXT_CONTENT_TYPE);
                        return read(nested);
                    }
                }
            } else if (bodyPart.getContent() instanceof String) {
                if (bodyPart.getHeader("Content-ID") != null && bodyPart.getHeader("Content-ID")[0].equalsIgnoreCase(cid)) {
                    return (String) bodyPart.getContent();
                }
            }
        }
        return null;
    }

    private String read(BodyPart part) throws IOException, MessagingException {
        try (InputStream is = part.getInputStream()) {
            Scanner s = new Scanner(is, StandardCharsets.UTF_8).useDelimiter("\\A");
            return s.hasNext() ? s.next() : "";
        }
    }

    private String getTextFromMimeMultipart(
            MimeMultipart mimeMultipart) throws MessagingException, IOException {
        StringBuilder result = new StringBuilder();
        int count = mimeMultipart.getCount();
        for (int i = 0; i < count; i++) {
            BodyPart bodyPart = mimeMultipart.getBodyPart(i);
            if (bodyPart.isMimeType(TEXT_CONTENT_TYPE)) {
                result.append("\n").append(bodyPart.getContent());
                break; // without break same text appears twice in my tests
            } else if (bodyPart.isMimeType("text/html")) {
                result.append("\n").append(bodyPart.getContent());
            } else if (bodyPart.getContent() instanceof MimeMultipart) {
                result.append(getTextFromMimeMultipart((MimeMultipart) bodyPart.getContent()));
            }
        }
        return result.toString();
    }

}
