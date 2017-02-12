package com.leantaas.graph_logic.storage_medium_adapter;

import com.leantaas.graph_representation.GraphEdge;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Iterator;
import java.util.function.Function;

/**
 *
 * Created by boweiliu on 12/11/16.
 */
public class SimpleTextFileAdapter implements  MediumAdapter<File> {

  private final Function<String, String[]> lineParser;

  private File file;

  private final ClassLoader classLoader;

  public SimpleTextFileAdapter(Function<String, String[]> lineParserParam) {
      lineParser = lineParserParam;
      classLoader = SimpleTextFileAdapter.class.getClassLoader();
  }

  public SimpleTextFileAdapter(String separatorParam) {
    lineParser = s -> s.split(separatorParam);
    classLoader = SimpleTextFileAdapter.class.getClassLoader();
  }

  @Override
  public Iterator<GraphEdge> apply(File file) {
    try {
      Iterator<String> lineStrIter = Files.lines(file.toPath()).iterator();
      return new Iterator<GraphEdge>() {
        private String oneLineCache;
        private Iterator<String> lineStrIterInstance = lineStrIter;
        @Override
        public boolean hasNext() {
          do {
            if (lineStrIterInstance.hasNext()) {
              oneLineCache = lineStrIterInstance.next();
            } else {
              return false;
            }
          } while (oneLineCache.isEmpty() || oneLineCache.startsWith("#"));
          return true;
        }

        @Override
        public GraphEdge next() {
          String[] exploded = lineParser.apply(oneLineCache);
          if (exploded.length != 2 && exploded.length != 4) {
            throw new IllegalStateException("line:\""+oneLineCache+"\" should have either 2 entries or 4 entries");
          }
          GraphEdge edge = new GraphEdge();
          edge.setFromNodeId(exploded[0].trim());  // in one line, the token is fromNodeId
          edge.setToNodeId(exploded[1].trim());    // second token is toNodeId

          if (exploded.length == 4) {
            edge.setDescriptiveEdge(Boolean.parseBoolean(exploded[2].trim()));
            edge.setOperationName(exploded[3].trim());
          }
          return edge;
        }
      };
    } catch (IOException ex) {
      throw new RuntimeException(ex);
    }

  }

  public Iterator<GraphEdge> apply(String filePath) {
    if (filePath == null || filePath.isEmpty()) {
      throw new IllegalArgumentException("filePath argument do not accept null or empty string");
    }
    try {
      java.net.URL url = classLoader.getResource(filePath);
      if (url == null) {
        throw new FileNotFoundException(String.format("\"%s\" cannot be found", filePath));
      }
      file = new File(url.getPath());
      return apply(file);
    } catch (FileNotFoundException ex) {
      throw new IllegalArgumentException(ex);
    }
  }

  public Function<String, String[]> getLineParser() {
    return lineParser;
  }

  public File getFile() {
    return file;
  }

  public void setFile(File file) {
    this.file = file;
  }

}
