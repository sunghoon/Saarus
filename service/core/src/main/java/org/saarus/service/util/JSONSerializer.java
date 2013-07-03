package org.saarus.service.util;

import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;

import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.map.DeserializationConfig;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.SerializationConfig;
import org.codehaus.jackson.map.annotate.JsonSerialize;
import org.codehaus.jackson.util.DefaultPrettyPrinter;
/**
 * $Author: Tuan Nguyen$ 
 **/
public class JSONSerializer {
  final static public JSONSerializer JSON_SERIALIZER = new JSONSerializer() ;

  private ObjectMapper mapper ;

  public JSONSerializer() {
    mapper = new ObjectMapper(); // can reuse, share globally
    mapper.configure(JsonParser.Feature.AUTO_CLOSE_SOURCE, false) ;
    SerializationConfig config = mapper.getSerializationConfig() ;
//    config = config.withSerializationInclusion(JsonSerialize.Inclusion.NON_NULL) ;
//    mapper.setSerializationConfig(config) ;
    config.setSerializationInclusion(JsonSerialize.Inclusion.NON_NULL) ;
    DefaultPrettyPrinter prettyPrinter = new DefaultPrettyPrinter() ;
    prettyPrinter.indentArraysWith(new DefaultPrettyPrinter.Lf2SpacesIndenter()) ;
    mapper.writer(prettyPrinter) ;
    mapper.configure(SerializationConfig.Feature.INDENT_OUTPUT, true) ;
  }

  public void setIgnoreUnknownProperty(boolean b) {
    mapper.configure(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES, !b);
  }

  public <T> byte[] toBytes(T idoc) throws IOException {
    StringWriter w = new StringWriter() ;
    mapper.writeValue(w, idoc);
    w.close() ;
    return w.getBuffer().toString().getBytes(StringUtil.UTF8) ;
  }

  public <T> T fromBytes(byte[] data, Class<T> type) throws IOException {
    StringReader reader = new StringReader(new String(data, StringUtil.UTF8)) ;
    return mapper.readValue(reader , type);
  }

  public <T> String toString(T idoc) throws IOException {
    StringWriter writer = new StringWriter() ;
    mapper.writeValue(writer, idoc);
    return writer.toString() ;
  }

  public  String toString(JsonNode node) throws IOException {
    StringWriter writer = new StringWriter() ;
    mapper.writeValue(writer, node);
    return writer.toString() ;
  }

  public <T> T fromString(String data, Class<T> type) throws IOException {
    StringReader reader = new StringReader(data) ;
    return mapper.readValue(reader , type);
  }
  
  public JsonNode fromString(String data) throws IOException {
    StringReader reader = new StringReader(data) ;
    return mapper.readTree(reader);
  }
}