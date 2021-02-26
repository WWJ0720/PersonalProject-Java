import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

public class Lib {
    public static Reader openInputFile(String fileName) throws FileNotFoundException {
        File file = new File(fileName);
        Reader reader = new InputStreamReader(new FileInputStream(file));
        return reader;
    }

    public static Writer openOutputFile(String fileName) throws FileNotFoundException {
        File file = new File(fileName);
        Writer writer = new OutputStreamWriter(new FileOutputStream(file));
        return writer;
    }

    //统计字符数，空格，水平制表符，换行符，均算字符
    public static int charactersCount(String inputFile, String outputFile) throws IOException {
        Reader reader = openInputFile(inputFile);
        int num = 0;
        int temp;
        while ((temp = reader.read()) != -1) {
            num++;
        }
        return num;
    }
    //统计单词总数,至少以4个英文字母开头，跟上字母数字符号，单词以分隔符分割，不区分大小写
    public static int wordsCount(String inputFile, String outputFile) throws IOException {
        Reader reader = openInputFile(inputFile);
        int temp;
        int num = 0;
        String word = "";
        while ((temp = reader.read()) != -1) {
            while (temp != -1 && ((char) temp != ' ') && ((char) temp != '\r') && (char) temp != '\n') {
                word += (char) temp;
                temp = reader.read();
            }
            while (((char) temp == ' ') || ((char) temp == '\r') || (char) temp == '\n') {//去除所有空白字符
                temp = reader.read();
            }
            if (word != "") {//如果单词不为空。单词数量++
                num++;
            }
            word = "" + (char) temp;
        }
        return num;
    }

    //统计行数，任何包含非空白字符的行，都需要统计。
    public static int linesCount(String inputFile, String outputFile) throws IOException {
        Reader reader = openInputFile(inputFile);
        int temp;
        int num = 1;
        while ((temp = reader.read()) != -1) {
            if ((char) temp == '\n') {
                num++;
            }
        }
        return num;
    }

    //统计单词的出现次数（对应输出接下来10行），最终只输出频率最高的10个。
    public static Map wordNum(String inputFile, String outputFile) throws IOException {
        Reader reader = openInputFile(inputFile);
        int temp;
        String word = "";
        Map<String, Integer> words = new HashMap<String, Integer>();
        while ((temp = reader.read()) != -1) {
            while (temp != -1 && ((char) temp != ' ') && ((char) temp != '\r') && (char) temp != '\n') {//读出完整单词
                word += (char) temp;
                temp = reader.read();
            }
            while (((char) temp == ' ') || ((char) temp == '\r') || ((char) temp == '\n')) {//去除所有空白字符
                temp = reader.read();
            }
            if (words.get(word) == null) {
                words.put(word, Integer.valueOf(1));
            } else {
                words.put(word, Integer.valueOf(words.get(word).intValue() + 1));
            }
            word = "" + (char) temp;
        }
        Map<String, Integer> result = words.entrySet().stream()
                .sorted(comparingByValue())
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue,
                        (oldValue, newValue) -> oldValue, LinkedHashMap::new));
        return result;
    }

    //比较函数
    public static <K, V extends Comparable<? super V>> Comparator<Map.Entry<K, V>> comparingByValue() {
        return (Comparator<Map.Entry<K, V>> & Serializable)
                (c1, c2) -> c2.getValue().compareTo(c1.getValue());
    }

    public static void printWords(Map<String, Integer> map) {
        int i = 0;
        for (Map.Entry<String, Integer> entry : map.entrySet()) {
            System.out.println(entry.getKey() + ":" + entry.getValue());
            if (i++ >= 9) {//打印频率前十的单词
                break;
            }
        }
    }
}