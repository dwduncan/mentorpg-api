package mil.decea.mentorpgapi.domain.daoservices.minio;

import io.minio.*;
import io.minio.errors.*;
import io.minio.http.Method;
import io.minio.messages.Item;
import jakarta.xml.bind.DatatypeConverter;
import org.springframework.stereotype.Service;

import java.io.*;
import java.nio.file.Files;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static mil.decea.mentorpgapi.etc.security.TokenService.EXPIRATION_TIME_IN_MINUTES;

@Service
public class ClienteMinio implements Serializable {

    //{"url":"http://127.0.0.1:9000","accessKey":"kCysFCUbsAocgVc3","secretKey":"SPrIbYCc0avGKxhd39JNRbmhrFOfNEVo","api":"s3v4","path":"auto"}
    public String minioURL = "http://127.0.0.1";//"http://mentor-minio
    public int port = 9000;
    public String accessKey = "NVuyrH1jYNxii6Xq";
    public String secretKey = "T4xSfW7DfkEhmKG26CwhEXTLB6ip0zmA";
    public int expiraEmSegundos = 60 * EXPIRATION_TIME_IN_MINUTES;

    public ClienteMinio() {}


    public ClienteMinio(int expiraEmSegundos) {
        this.expiraEmSegundos = expiraEmSegundos;
    }
    public ClienteMinio(String minioURL, int port, String accessKey, String secretKey, int expiraEmSegundos) {
        this(expiraEmSegundos);
        this.minioURL = minioURL;
        this.port = port;
        this.accessKey = accessKey;
        this.secretKey = secretKey;
    }

    private MinioClient minioClient() {
        return MinioClient.builder()
                .endpoint(minioURL, port, false)
                .credentials(accessKey, secretKey)
                .build();
    }

    public boolean bucketExists(MinioClient client, String bucketName)
            throws ClientMinioImplemantationException {
        try{
            return client.bucketExists(BucketExistsArgs.builder().bucket(bucketName).build());
        } catch(InvalidKeyException | ErrorResponseException | InsufficientDataException
                | InternalException | InvalidResponseException | NoSuchAlgorithmException
                | ServerException | XmlParserException | IllegalArgumentException | IOException ex){
            throw new ClientMinioImplemantationException(ex);
        }

    }

    /**
     *
     * @param source
     * @return true if any change is detected inside the source and the update is successful, false if update was not necessary
     * @throws ClientMinioImplemantationException if any problem/exception occurs during the attempt/transaction/operation with the Minio server
     */
    public boolean updateBucket(MinioStorage<?> source) throws ClientMinioImplemantationException {


        String base64 = source.getExternalData().getBase64Data() == null ? "" : source.getExternalData().getBase64Data();

        //base64 is always blank if no data needs to be updated on Minio
        if (base64.isBlank()) return false;



        try {

            MinioClient client = minioClient();
            //it is necessary to delete the file, otherwise it will become garbage
            if (source.hasPreviousStorageDestinationPath()){
                //createBucketIfNotExists(client, bucketName);
                client.removeObject(RemoveObjectArgs.builder()
                        .bucket(source.getBucket())
                        .object(source.getPreviousStorageDestinationPath())
                        .build());
            }


            String[] prefixSuffix = source.getExternalData().getFileNamePrefixSuffix();
            File tempFile = Files.createTempFile(prefixSuffix[0],prefixSuffix[1]).toFile();
            OutputStream outputStream = new BufferedOutputStream(new FileOutputStream(tempFile));
            outputStream.write(DatatypeConverter.parseBase64Binary(base64));
            outputStream.flush();
            outputStream.close();
            createBucketIfNotExists(client, source.getBucket());
            client.uploadObject(
                    UploadObjectArgs.builder()
                            .bucket(source.getBucket())
                            .object(source.getStorageDestinationPath())
                            .contentType(source.getExternalData().getFormato())
                            .filename(tempFile.getAbsolutePath())
                            .build());
            source.getExternalData().setBase64Data(null);
            return true;
        }
        catch(InvalidKeyException | ErrorResponseException | InsufficientDataException
              | InternalException | InvalidResponseException | NoSuchAlgorithmException
              | ServerException | XmlParserException | IllegalArgumentException | IOException ex) {
            System.err.println(ex.getLocalizedMessage());
            ex.printStackTrace();
            throw new ClientMinioImplemantationException(ex);
        }
    }
    public boolean updateObject(MinioStorage<?> source) throws ClientMinioImplemantationException {


        String base64 = source.getExternalData().getBase64Data() == null ? "" : source.getExternalData().getBase64Data();

        //base64 is always blank if no data needs to be updated on Minio
        if (base64.isBlank()) return false;

        try {

            MinioClient client = minioClient();
            //it is necessary to delete the file, otherwise it will become garbage
            if (source.hasPreviousStorageDestinationPath()){
                //createBucketIfNotExists(client, bucketName);
                client.removeObject(RemoveObjectArgs.builder()
                        .bucket(source.getBucket())
                        .object(source.getPreviousStorageDestinationPath())
                        .build());
            }


            createBucketIfNotExists(client, source.getBucket());

            byte[] data = Base64.getDecoder().decode(base64);
            InputStream is = new BufferedInputStream(new ByteArrayInputStream(data));

            client.putObject(
                    PutObjectArgs.builder()
                            .bucket(source.getBucket())
                            .object(source.getStorageDestinationPath())
                            .contentType(source.getExternalData().getFormato())
                            .stream(is,data.length,-1)//  .stream(is,data.length,-1) .stream(is,-1,10485760)
                            .build());
            is.close();
            source.getExternalData().setBase64Data(null);
            return true;
        }
        catch(InvalidKeyException | ErrorResponseException | InsufficientDataException
              | InternalException | InvalidResponseException | NoSuchAlgorithmException
              | ServerException | XmlParserException | IllegalArgumentException | IOException ex) {

            throw new ClientMinioImplemantationException(ex);
        }
    }

    public void updateObject(Collection<? extends MinioStorage<?>> sources) throws ClientMinioImplemantationException {
        try {

            MinioClient client = minioClient();

            Map<String,Boolean> bucketExists = new HashMap<>();

            for(MinioStorage<?> source : sources) {

                String base64 = source.getExternalData().getBase64Data() == null ? "" : source.getExternalData().getBase64Data();

                //base64 is always blank if no data needs to be updated on Minio
                if (base64.isBlank()) continue;

                if (bucketExists.get(source.getBucket()) == null){
                    createBucketIfNotExists(client, source.getBucket());
                    bucketExists.put(source.getBucket(),true);
                }

                System.out.println(source.getPreviousStorageDestinationPath() + " / " + source.getStorageDestinationPath());
                //it is necessary to delete the file, otherwise it will become garbage
                if (source.hasPreviousStorageDestinationPath()) {
                    //createBucketIfNotExists(client, bucketName);
                    client.removeObject(RemoveObjectArgs.builder()
                            .bucket(source.getBucket())
                            .object(source.getPreviousStorageDestinationPath())
                            .build());
                }

                byte[] data = Base64.getDecoder().decode(base64);
                InputStream is = new BufferedInputStream(new ByteArrayInputStream(data));

                client.putObject(
                        PutObjectArgs.builder()
                                .bucket(source.getBucket())
                                .object(source.getStorageDestinationPath())
                                .contentType(source.getExternalData().getFormato())
                                .stream(is, data.length, -1)//  .stream(is,data.length,-1) .stream(is,-1,10485760)
                                .build());
                is.close();
                source.getExternalData().setBase64Data(null);
            }
        }
        catch(InvalidKeyException | ErrorResponseException | InsufficientDataException
              | InternalException | InvalidResponseException | NoSuchAlgorithmException
              | ServerException | XmlParserException | IllegalArgumentException | IOException ex) {

            throw new ClientMinioImplemantationException(ex);
        }

    }

    public boolean download(MinioStorage<?> source) {
        try {
            MinioClient client = minioClient();
            //createBucketIfNotExists(client, bucketName);

            client.downloadObject(DownloadObjectArgs.builder()
                    .bucket(source.getBucket())
                    .object(source.getStorageDestinationPath())
                    .filename(source.getExternalData().getNomeArquivo())
                    .build());
            return true;
        }
        catch(InvalidKeyException | ErrorResponseException | InsufficientDataException
              | InternalException | InvalidResponseException | NoSuchAlgorithmException
              | ServerException | XmlParserException | IllegalArgumentException | IOException ex) {
            System.err.println(ex.getLocalizedMessage());
            return false;
        }
    }

    public GetObjectResponse getObjectResponse(String bucketName, String destPath) throws ClientMinioImplemantationException {
        try {
            return minioClient().getObject(GetObjectArgs.builder()
                    .bucket(bucketName)
                    .object(destPath)
                    .build());
        }

        catch(InvalidKeyException | ErrorResponseException | InsufficientDataException
              | InternalException | InvalidResponseException | NoSuchAlgorithmException
              | ServerException | XmlParserException | IllegalArgumentException | IOException ex) {


            throw new ClientMinioImplemantationException(ex);
        }
    }
    public boolean remove(MinioStorage<?> source) throws ClientMinioImplemantationException {
        try {
            MinioClient client = minioClient();
            //createBucketIfNotExists(client, bucketName);
            client.removeObject(RemoveObjectArgs.builder()
                    .bucket(source.getBucket())
                    .object(source.getStorageDestinationPath())
                    .build());
            return true;
        } catch(InvalidKeyException | ErrorResponseException | InsufficientDataException
                | InternalException | InvalidResponseException | NoSuchAlgorithmException
                | ServerException | XmlParserException | IllegalArgumentException | IOException ex) {

            throw new ClientMinioImplemantationException(ex);
        }
    }

    public List<String> list(String bucketName, String prefix) {
        try {
            MinioClient client = minioClient();
            //createBucketIfNotExists(client, bucketName);
            Iterable<Result<Item>> it = client.listObjects(ListObjectsArgs.builder()
                    .bucket(bucketName)
                    .prefix(prefix)
                    .build());
            return StreamSupport.stream(Spliterators.spliteratorUnknownSize(
                            it.iterator(),
                            Spliterator.ORDERED
                    ), false)
                    .map(e -> {
                        try {
                            return e.get().objectName();
                        } catch (InvalidKeyException | ErrorResponseException | IllegalArgumentException
                                 | InsufficientDataException | InternalException | InvalidResponseException
                                 | NoSuchAlgorithmException | ServerException | XmlParserException | IOException e1) {
                            e1.printStackTrace();
                            return "";
                        }
                    })
                    .filter(e -> !e.isEmpty())
                    .collect(Collectors.toList());
        }
        catch(IllegalArgumentException ex) {
            return Arrays.asList();
        }
    }

    public void insertSasUrl(MinioStorage<?> source) throws ClientMinioImplemantationException {
        insertSasUrl(source, expiraEmSegundos);
    }

    /**
     *
     * @return uma url com validade para expirar para acessar o arquivo diretamente no minio
     */
    public void insertSasUrl(MinioStorage<?> source, int expirationTime) throws ClientMinioImplemantationException {

        if (source.getExternalData().getNomeArquivo() == null || source.getExternalData().getNomeArquivo().isBlank()) return;

        try {
            MinioClient client = minioClient();
            //createBucketIfNotExists(client, source.getBucket());
            String url = client.getPresignedObjectUrl(GetPresignedObjectUrlArgs.builder()
                    .method(Method.GET)
                    .bucket(source.getBucket())
                    .object(source.getStorageDestinationPath())
                    .expiry(expirationTime)
                    .build());
            source.getExternalData().setArquivoUrl(url);
        }
        catch(InvalidKeyException | ErrorResponseException | InsufficientDataException
              | InternalException | InvalidResponseException | NoSuchAlgorithmException
              | ServerException | XmlParserException | IllegalArgumentException | IOException ex) {
            System.err.println(ex.getLocalizedMessage());
            throw new ClientMinioImplemantationException(ex);
        }
    }

    private boolean createBucketIfNotExists(MinioClient client, String bucketName) throws ClientMinioImplemantationException {


        try {
            boolean isAvailable = bucketExists(client, bucketName);
            if(!isAvailable) {
                client.makeBucket(MakeBucketArgs.builder().bucket(bucketName).build());
            }
            return !isAvailable;
        }
        catch(InvalidKeyException | ErrorResponseException | InsufficientDataException
              | InternalException | InvalidResponseException | NoSuchAlgorithmException
              | ServerException | XmlParserException | IllegalArgumentException | IOException ex) {
            throw new ClientMinioImplemantationException(ex);
        }


    }
}
