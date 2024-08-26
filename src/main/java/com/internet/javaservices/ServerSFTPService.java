package com.internet.javaservices;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.Base64;
import org.apache.commons.io.IOUtils;

public class ServerSFTPService {
    
    /**
     * Libreias a importar 
     * 
     * INNECESARIO
        <dependency>
            <groupId>commons-net</groupId>
            <artifactId>commons-net</artifactId>
            <version>3.8.0</version>
        </dependency>
     * 
     * NECESARIAS
        <dependency>
            <groupId>com.jcraft</groupId>
            <artifactId>jsch</artifactId>
            <version>0.1.55</version>
        </dependency>
        
        <dependency>
            <groupId>commons-io</groupId>
            <artifactId>commons-io</artifactId>
            <version>2.16.1</version>
        </dependency>
     * 
     * CONFIGURACIONES en: C:\ProgramData\ssh\sshd_config
     * 
        PubkeyAuthentication yes
        PasswordAuthentication yes
        PermitEmptyPasswords no
        Match User "Usuario Java"
            ChrootDirectory C:\SFTP\Repositories
            ForceCommand internal-sftp
            AllowTcpForwarding no
            X11Forwarding no
        AllowUsers "Usuario Java"
     * 
     */
    
    private static String host = "127.0.0.1";
    private static int port = 22;
    private static String user = "Usuario Java";
    private static String password = "0123456789";
    
    
    public static void guardarArchivo(byte[] fileArrayByte, UrpRepositorioENUM urpRepositorioENUM, String nombreFormato) {
        JSch jsch = new JSch();
        Session session = null;
        ChannelSftp channelSftp = null;

        try {
            session = jsch.getSession(user, host, port);
            session.setPassword(password);
            session.setConfig("StrictHostKeyChecking", "no");
            session.connect();

            Channel channel = session.openChannel("sftp");
            channel.connect();

            channelSftp = (ChannelSftp) channel;

            try (InputStream is = new ByteArrayInputStream(fileArrayByte)) {
                channelSftp.cd(UrpRepositorioENUM.PRODUCCION.getAddress());
                channelSftp.put(is, nombreFormato);
                System.out.println("Archivo subido correctamente");
            }

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error: " + e.getMessage());
        } finally {
            if (channelSftp != null) {
                channelSftp.disconnect();
            }
            if (session != null) {
                session.disconnect();
            }
        }
    }
    
    public static void guardarArchivo(String fileString, UrpRepositorioENUM urpRepositorioENUM, String nombreFormato) {
        JSch jsch = new JSch();
        Session session = null;
        ChannelSftp channelSftp = null;

        try {
            session = jsch.getSession(user, host, port);
            session.setPassword(password);
            session.setConfig("StrictHostKeyChecking", "no");
            session.connect();

            Channel channel = session.openChannel("sftp");
            channel.connect();

            channelSftp = (ChannelSftp) channel;

            try (InputStream is = new ByteArrayInputStream(Base64.getDecoder().decode(fileString))) {
                channelSftp.cd(UrpRepositorioENUM.PRODUCCION.getAddress());
                channelSftp.put(is, nombreFormato);
                System.out.println("Archivo subido correctamente");
            }

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error: " + e.getMessage());
        } finally {
            if (channelSftp != null) {
                channelSftp.disconnect();
            }
            if (session != null) {
                session.disconnect();
            }
        }
    }
    
    public static byte[] descargarArchivoEnByteArray(UrpRepositorioENUM urpRepositorioENUM, String nombreFormato) {
        JSch jsch = new JSch();
        Session session = null;
        ChannelSftp channelSftp = null;

        try {
            session = jsch.getSession(user, host, port);
            session.setPassword(password);
            session.setConfig("StrictHostKeyChecking", "no");
            session.connect();

            Channel channel = session.openChannel("sftp");
            channel.connect();

            channelSftp = (ChannelSftp) channel;
            
            try (InputStream is = channelSftp.get(urpRepositorioENUM.getAddress() + nombreFormato)) {
                System.out.println("Archivo descargado correctamente");
                return IOUtils.toByteArray(is);
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error: " + e.getMessage());
        } finally {
            if (channelSftp != null) {
                channelSftp.disconnect();
            }
            if (session != null) {
                session.disconnect();
            }
        }
        
        return null;
    }
    
    public static String descargarArchivoEnString(UrpRepositorioENUM urpRepositorioENUM, String nombreFormato) {
        JSch jsch = new JSch();
        Session session = null;
        ChannelSftp channelSftp = null;

        try {
            session = jsch.getSession(user, host, port);
            session.setPassword(password);
            session.setConfig("StrictHostKeyChecking", "no");
            session.connect();

            Channel channel = session.openChannel("sftp");
            channel.connect();

            channelSftp = (ChannelSftp) channel;
            
            try (InputStream is = channelSftp.get(urpRepositorioENUM.getAddress() + nombreFormato)) {
                System.out.println("Archivo descargado correctamente");
                return Base64.getEncoder().encodeToString(IOUtils.toByteArray(is));
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error: " + e.getMessage());
        } finally {
            if (channelSftp != null) {
                channelSftp.disconnect();
            }
            if (session != null) {
                session.disconnect();
            }
        }
        
        return null;
    }
    
    public static boolean consultarArchivoPresente(UrpRepositorioENUM urpRepositorioENUM, String nombreArchivo) {
        JSch jsch = new JSch();
        Session session = null;
        ChannelSftp channelSftp = null;

        try {
            session = jsch.getSession(user, host, port);
            session.setPassword(password);
            session.setConfig("StrictHostKeyChecking", "no");
            session.connect();

            Channel channel = session.openChannel("sftp");
            channel.connect();

            channelSftp = (ChannelSftp) channel;
            
            channelSftp.lstat(urpRepositorioENUM.getAddress() + nombreArchivo);
            
            System.out.println("Archivo se encuentra en el servidor");
            
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error: " + e.getMessage());
        } finally {
            if (channelSftp != null) {
                channelSftp.disconnect();
            }
            if (session != null) {
                session.disconnect();
            }
        }
        
        return false;
    }
    
    public static void borrarArchivo(UrpRepositorioENUM urpRepositorioENUM, String nombreArchivo) {
        JSch jsch = new JSch();
        Session session = null;
        ChannelSftp channelSftp = null;

        try {
            session = jsch.getSession(user, host, port);
            session.setPassword(password);
            session.setConfig("StrictHostKeyChecking", "no");
            session.connect();

            Channel channel = session.openChannel("sftp");
            channel.connect();

            channelSftp = (ChannelSftp) channel;
            
            channelSftp.rm(urpRepositorioENUM.getAddress() + nombreArchivo);
            
            System.out.println("Archivo borrado correctamente");
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error: " + e.getMessage());
        } finally {
            if (channelSftp != null) {
                channelSftp.disconnect();
            }
            if (session != null) {
                session.disconnect();
            }
        }
    }    
}
