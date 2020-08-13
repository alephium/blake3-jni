package org.alephium.blake3jni;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;

public final class Blake3LibLoader {

  private static final String osName = System.getProperty("os.name").toLowerCase();

  private static final String name = "blake3";
  private static final String extention = osName.contains("mac") ? ".dylib" : ".so";
  private static final String libName = "lib" + name;
  private static final String lib = libName + extention;

  private Blake3LibLoader() {
  }

  public static void loadLibrary() throws IOException {

    File tempFile = File.createTempFile(libName, extention);
    tempFile.deleteOnExit();

    try (final InputStream is = Blake3LibLoader.class.getClassLoader().getResourceAsStream(lib)) {
      Files.copy(is, tempFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
    } catch (NullPointerException e) {
      throw new FileNotFoundException("Library " + lib + " not found in JAR.");
    }

    System.load(tempFile.getAbsolutePath());
  }
}
