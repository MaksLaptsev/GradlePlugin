package gradleplugin.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class StringBuilderFromStream {
    public static String getOutResult(Process process){
        String out = Stream.of(process.getErrorStream(), process.getInputStream())
                .parallel()
                .map((InputStream isForOutput) -> {
                    StringBuilder output = new StringBuilder();
                    try (BufferedReader br = new BufferedReader(new InputStreamReader(isForOutput))) {
                        String line;
                        while ((line = br.readLine()) != null) {
                            output.append(line);
                            output.append("\n");
                        }
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                    return output;
                }).collect(Collectors.joining());
        return out;
    }
}
