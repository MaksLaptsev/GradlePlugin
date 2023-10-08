package gradleplugin.utils;

import java.math.BigDecimal;

public class TagUtils {
    public static double getNumericTag(String s){
        double tag;
        if (!(s == null) && !s.equals("")){
            s = s.replace(")","").replace("(","")
                    .replace(":","").replace(" ","")
                    .replace("v","").replace("-","")
                    .replace("-","").replace("rc","")
                    .replace("SNAPSHOT","").replace(",","");
            tag = Double.parseDouble(s);
        }else tag = 0.0;

        return tag;
    }

    public static String createUncommittedTag(String s){
        return s.replace("\n","").substring(s.lastIndexOf('/') + 1) + ".uncommitted";
    }

    public static String getLastTagStr(Process process){
        String[] out = StringBuilderFromStream.getOutResult(process).split("\n");
        return String.valueOf(TagUtils.getNumericTag(out[out.length-1]));
    }

    public static String createTag(String lastTag, String branch){
        BigDecimal bigDecimal;
        switch (branch.replace("\n","")) {
            case "master" -> {
                bigDecimal = new BigDecimal(lastTag.split("\\.")[0].replace(".",""));
                return "v" + bigDecimal.add(new BigDecimal("1.0"));
            }
            case "stage" -> {
                bigDecimal = new BigDecimal(lastTag);
                return ("v" + bigDecimal.add(new BigDecimal("0.1")) + "-rc");
            }
            case "qa", "dev" -> {
                bigDecimal = new BigDecimal(lastTag);
                return "v" + bigDecimal.add(new BigDecimal("0.1"));
            }
            default -> {
                bigDecimal = new BigDecimal(lastTag);
                return ("v" + bigDecimal.add(new BigDecimal("0.1")) + "-SNAPSHOT");
            }
        }
    }
}
