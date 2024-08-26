/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.internet.javaservices;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.io.IOUtils;
import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;

public class ConexionSFTPserverController {
    
    public static void main(String[] args) {

        //uploadFileToFTP(server, port, user, pass, localFilePath, remoteFilePath);

        //uploadFileToSFTP();

        try {
            FileInputStream fis = new FileInputStream("C:\\Users\\Usuario Java\\Desktop\\Programacion/fisica.pdf");
            
            byte[] bit = IOUtils.toByteArray(fis);
            
            ServerSFTPService.guardarArchivo(bit, UrpRepositorioENUM.PRODUCCION, "ejemplo.pdf");
            
            ServerSFTPService.consultarArchivoPresente(UrpRepositorioENUM.PRODUCCION, "ejemplo.pdf");
            
            ServerSFTPService.descargarArchivoEnByteArray(UrpRepositorioENUM.PRODUCCION, "ejemplo.pdf");
            
            ServerSFTPService.borrarArchivo(UrpRepositorioENUM.PRODUCCION, "ejemplo.pdf");
        } catch (FileNotFoundException ex) {
            Logger.getLogger(ConexionSFTPserverController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(ConexionSFTPserverController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public static void uploadFileToFTP() {
        String server = "127.0.0.1";
        int port = 21;
        String user = "Usuario Java";
        String pass = "0123456789";
        String localFilePath = "C:\\Users\\Usuario Java\\Desktop\\Programacion/fisica.pdf";
        String remoteFilePath = "/filezillaServer/pdf/seguro/nombreEnServidor.pdf";
        
        FTPClient ftpClient = new FTPClient();

        try {
            ftpClient.connect(server, port);
            ftpClient.login(user, pass);
            ftpClient.enterLocalPassiveMode();
            ftpClient.setFileType(FTP.BINARY_FILE_TYPE);

            try (FileInputStream fis = new FileInputStream(localFilePath)) {
                boolean done = ftpClient.storeFile(remoteFilePath, fis);
                if (done) {
                    System.out.println("File uploaded successfully!");
                } else {
                    System.out.println("Failed to upload file.");
                }
            }

            ftpClient.logout();
        } catch (IOException ex) {
            ex.printStackTrace();
            System.out.println("Error: " + ex.getMessage());
        } finally {
            try {
                if (ftpClient.isConnected()) {
                    ftpClient.disconnect();
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }
    
    public static void uploadFileToSFTP() {
        
        String host = "127.0.0.1";
        int port = 22;
        String user = "Usuario Java";
        String password = "0123456789";
        String localFilePath = "C:\\Users\\Usuario Java\\Desktop\\Programacion/fisica.pdf";
        String remoteDir = "/sftp/usuarios";
        
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

            try (FileInputStream fis = new FileInputStream(localFilePath)) {
                channelSftp.cd(remoteDir);
                channelSftp.put(fis, "sample.pdf");
                System.out.println("File uploaded successfully!");
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

    public static void main1(String[] args) {
        
        FTPClient ftpClient = new FTPClient();
        try {
            ftpClient.connect("servidor", 21);
            ftpClient.login("usuario", "contraseña");
            ftpClient.enterLocalPassiveMode();
            // Operaciones de FTP (subir, descargar archivos, etc.)
        } catch (IOException ex) {
            ex.printStackTrace();
        } finally {
            try {
                if (ftpClient.isConnected()) {
                    ftpClient.logout();
                    ftpClient.disconnect();
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }
}
