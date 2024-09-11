/*
 * Axelor Business Solutions
 *
 * Copyright (C) 2022 Axelor (<http://axelor.com>).
 *
 * This program is free software: you can redistribute it and/or  modify
 * it under the terms of the GNU Affero General Public License, version 3,
 * as published by the Free Software Foundation.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.axelor.utils.helpers;

import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.ChannelSftp.LsEntry;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import com.jcraft.jsch.SftpException;
import com.jcraft.jsch.SftpProgressMonitor;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Vector;

public class SftpHelper {

  private SftpHelper() {
  }

  /**
   * Returns a new session created with given {@code host}, {@code port}, {@code username},
   * {@code password}, {@code privateKey} and optional {@code passphrase}.
   *
   * @param host               the host to connect to.
   * @param port               the port to connect to.
   * @param username           the username to use.
   * @param password           the password to use.
   * @param privateKeyFileName the private key file to use.
   * @param passphrase         the passphrase to use.
   * @return a new session.
   * @throws JSchException if {@code username} or {@code host} are invalid, or if {@code passphrase}
   *                       is not right.
   */
  public static Session createSession(
      String host,
      int port,
      String username,
      String password,
      String privateKeyFileName,
      String passphrase)
      throws JSchException {
    JSch jsch = new JSch();

    if (privateKeyFileName != null) {
      jsch.addIdentity(privateKeyFileName, passphrase);
    }

    Session session = jsch.getSession(username, host, port);
    session.setPassword(password);
    session.setConfig("StrictHostKeyChecking", "no");

    return session;
  }

  /**
   * Returns a new session created with given {@code host}, {@code port}, {@code username} and
   * {@code password}.
   *
   * @param host     the host to connect to.
   * @param port     the port to connect to.
   * @param username the username to use.
   * @param password the password to use.
   * @return a new session.
   * @throws JSchException if {@code username} or {@code host} are invalid.
   */
  public static Session createSession(String host, int port, String username, String password)
      throws JSchException {
    return createSession(host, port, username, password, null, null);
  }

  /**
   * Returns true if session is valid.
   *
   * @param session the session to check.
   * @return true if session is valid.
   * @throws JSchException if session is already connected.
   */
  public static boolean isValid(Session session) throws JSchException {
    boolean valid;

    session.connect();
    valid = session.isConnected();
    session.disconnect();

    return valid;
  }

  /**
   * Opens a SFTP channel with given {@code session}.
   *
   * @param session the session to open channel with.
   * @return a new SFTP channel.
   * @throws JSchException if session is not connected.
   */
  public static ChannelSftp openSftpChannel(Session session) throws JSchException {
    return (ChannelSftp) session.openChannel("sftp");
  }

  /**
   * Returns a list of all files in given directory {@code dir}.
   *
   * @param channel the channel to list files from.
   * @param dir     the directory to list files from.
   * @return a list of all files in given directory {@code dir}.
   * @throws SftpException if {@code dir} is invalid.
   */
  @SuppressWarnings("unchecked")
  public static List<LsEntry> getFiles(ChannelSftp channel, String dir) throws SftpException {
    List<LsEntry> files = new ArrayList<>((Vector<LsEntry>) channel.ls(dir));

    files.sort(
        Comparator.comparing((LsEntry file) -> file.getAttrs().getMTime())
            .thenComparing(LsEntry::getFilename));

    return files;
  }

  /**
   * Returns an {@link InputStream} corresponding to given {@code absoluteFilePath} in remote
   * server.
   *
   * @param channel          the channel to get file from.
   * @param absoluteFilePath the absolute file path to get.
   * @return an {@link InputStream} corresponding to given {@code absoluteFilePath} in remote
   * @throws SftpException if {@code absoluteFilePath} is invalid.
   */
  public static InputStream get(ChannelSftp channel, String absoluteFilePath) throws SftpException {
    return channel.get(absoluteFilePath);
  }

  /**
   * Returns an {@link InputStream} corresponding to given {@code absoluteFilePath} in remote
   * server.
   *
   * @param channel          the channel to get file from.
   * @param absoluteFilePath the absolute file path to get.
   * @param monitor          the monitor to use.
   * @return an {@link InputStream} corresponding to given {@code absoluteFilePath} in remote
   * @throws SftpException if {@code absoluteFilePath} is invalid.
   */
  public static InputStream get(
      ChannelSftp channel, String absoluteFilePath, SftpProgressMonitor monitor)
      throws SftpException {
    return channel.get(absoluteFilePath, monitor);
  }

  /**
   * Sends given {@code file} to given {@code absoluteFilePath} in remote server.
   *
   * @param channel          the channel to put file to.
   * @param file             the file to put.
   * @param absoluteFilePath the absolute file path to put.
   * @throws SftpException if {@code absoluteFilePath} is invalid.
   */
  public static void put(ChannelSftp channel, InputStream file, String absoluteFilePath)
      throws SftpException {
    channel.put(file, absoluteFilePath);
  }

  /**
   * Sends given {@code file} to given {@code absoluteFilePath} in remote server.
   * @param channel          the channel to put file to.
   * @param file             the file to put.
   * @param absoluteFilePath the absolute file path to put.
   * @param monitor          the monitor to use.
   * @throws SftpException if {@code absoluteFilePath} is invalid.
   */
  public static void put(
      ChannelSftp channel, InputStream file, String absoluteFilePath, SftpProgressMonitor monitor)
      throws SftpException {
    channel.put(file, absoluteFilePath, monitor);
  }

  /**
   * Moves given {@code src} file to given {@code dst} file, in remote server.
   * @param channel the channel to move file with.
   * @param src     the source file to move.
   * @param dst     the destination file to move.
   * @throws SftpException if {@code src} or {@code dst} are invalid.
   */
  public static void move(ChannelSftp channel, String src, String dst) throws SftpException {
    channel.rename(src, dst);
  }

  /**
   * @param file the file to check.
   * @return true if given {@code file} is a file.
   */
  public static boolean isFile(LsEntry file) {
    return !file.getAttrs().isDir() && !file.getAttrs().isLink();
  }
}
