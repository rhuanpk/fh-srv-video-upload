package com.example.videoupload.application.service;

import com.example.videoupload.application.ports.UploadVideoPort;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.async.AsyncRequestBody;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3AsyncClient;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.sqs.SqsClient;
import software.amazon.awssdk.services.sqs.model.SendMessageRequest;
import software.amazon.awssdk.services.sqs.model.SqsException;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ExecutionException;

public class UploadVideo implements UploadVideoPort {

    private static final String QUEUE_URL = "https://sqs.us-east-1.amazonaws.com/621111424252/update-status";

    private final S3AsyncClient s3AsyncClient;
    private final SqsClient sqsClient;


    @Value("${aws.s3.bucket-name}")
    private String bucketName;

    public UploadVideo(S3AsyncClient s3AsyncClient, SqsClient sqsClient) {
        this.s3AsyncClient = s3AsyncClient;
        this.sqsClient = sqsClient;
    }


    @Override
    public String uploadVideo(MultipartFile file, String id, String email) throws IOException, InterruptedException, ExecutionException {
        // Diretório é o id recebido
        String fileName = id + "/" + file.getOriginalFilename();

        // Usar UUID para garantir nome único no arquivo temporário
        String tempFileName = UUID.randomUUID().toString() + "-" + file.getOriginalFilename();
        Path tempFile = Files.createTempFile(null, tempFileName);

        try {
            // Copiar o conteúdo do MultipartFile para o arquivo temporário
            Files.copy(file.getInputStream(), tempFile, StandardCopyOption.REPLACE_EXISTING);
            System.out.println("Arquivo temporário criado: " + tempFile);

            // Criando um Map para armazenar os metadados
            Map<String, String> metadata = new HashMap<>();
            metadata.put("id", id); // Adiciona o id como metadado
            metadata.put("email", email); // Adiciona o email como metadado

            // Cria o PutObjectRequest com a chave do arquivo, tipo de conteúdo e metadados
            PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                    .bucket(bucketName)
                    .key(fileName)
                    .contentType(file.getContentType())
                    .metadata(metadata) // Passando o Map de metadados
                    .build();

            // Usando AsyncRequestBody para criar o corpo assíncrono a partir do arquivo temporário
            AsyncRequestBody asyncRequestBody = AsyncRequestBody.fromFile(tempFile);
            System.out.println("Iniciando upload para S3...");

            // Realiza o upload do arquivo
            s3AsyncClient.putObject(putObjectRequest, asyncRequestBody).get();
            System.out.println("Upload concluído com sucesso!");



            return "https://" + bucketName + ".s3.amazonaws.com/" + fileName;
        } catch (IOException e) {
            System.err.println("Erro durante o envio do arquivo (IOException): " + e.getMessage());
            e.printStackTrace();  // Exibe a pilha de chamadas
            throw e;
        } finally {
            // Exclui o arquivo temporário após o upload
            try {
                Files.deleteIfExists(tempFile);
                System.out.println("Arquivo temporário excluído: " + tempFile);
            } catch (IOException e) {
                System.err.println("Erro ao excluir arquivo temporário: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }
}
